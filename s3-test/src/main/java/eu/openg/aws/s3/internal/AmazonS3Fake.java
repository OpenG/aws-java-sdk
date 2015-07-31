/*
 * Copyright 2015 OpenG (Atvira Karta, LLC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.openg.aws.s3.internal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AbstractAmazonS3;
import com.amazonaws.services.s3.internal.AmazonS3ExceptionBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.*;

import static com.amazonaws.util.BinaryUtils.toBase64;
import static com.amazonaws.util.Md5Utils.md5AsBase64;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

public class AmazonS3Fake extends AbstractAmazonS3 {

    private static final Owner owner = new Owner(createExtendedId(), "test");

    private final Clock clock;

    private Map<String, Bucket> buckets = new HashMap<>();

    public AmazonS3Fake(Clock clock) {
        this.clock = clock;
    }

    public AmazonS3Fake() {
        this(Clock.systemDefaultZone());
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        return bucketName.startsWith("existing") || buckets.containsKey(bucketName);
    }

    @Override
    public List<Bucket> listBuckets() {
        return new ArrayList<>(buckets.values());
    }

    @Override
    public Bucket createBucket(String bucketName) {
        if (doesBucketExist(bucketName))
            throw buildException(
                    "The requested bucket name is not available. " +
                            "The bucket namespace is shared by all users of the system. " +
                            "Please select a different name and try again.",
                    "BucketAlreadyExists", 409,
                    new HashMap<String, String>() {{
                        put("BucketName", bucketName);
                    }});
        addBucket(bucketName);
        return new Bucket(bucketName);
    }

    private void addBucket(String bucketName) {
        Bucket bucket = new Bucket(bucketName);
        bucket.setOwner(owner);
        bucket.setCreationDate(Date.from(clock.instant()));
        buckets.put(bucketName, bucket);
    }

    @Override
    public void deleteBucket(String bucketName) {
        checkBucketAccess(bucketName);
        buckets.remove(bucketName);
    }

    @Override
    public PutObjectResult putObject(String bucketName, String key, File file) {
        checkBucketAccess(bucketName);
        try {
            PutObjectResult result = new PutObjectResult();
            result.setETag(encodeHexString(createId().getBytes()));
            result.setContentMd5(md5AsBase64(file));
            return result;
        } catch (IOException e) {
            throw new AmazonClientException(e);
        }
    }

    private void checkBucketAccess(String bucketName) {
        if (!doesBucketExist(bucketName))
            throw buildException(
                    "The specified bucket does not exist",
                    "NoSuchBucket", 404,
                    new HashMap<String, String>() {{
                        put("BucketName", bucketName);
                    }});
        if (bucketName.startsWith("existing"))
            throw buildException("All access to this object has been disabled", "AllAccessDisabled", 403);
    }

    private static AmazonS3Exception buildException(String message, String errorCode, int statusCode) {
        return buildException(message, errorCode, statusCode, null);
    }

    private static AmazonS3Exception buildException(
            String message, String errorCode, int statusCode, Map<String, String> additionalDetails
    ) {
        AmazonS3Exception exception = newException(message, errorCode, statusCode, additionalDetails);
        exception.setServiceName("Amazon S3");
        return exception;
    }

    private static AmazonS3Exception newException(
            String message, String errorCode, int statusCode, Map<String, String> additionalDetails
    ) {
        AmazonS3ExceptionBuilder builder = new AmazonS3ExceptionBuilder();
        builder.setRequestId(createId());
        builder.setErrorCode(errorCode);
        builder.setErrorMessage(message);
        builder.setStatusCode(statusCode);
        builder.setExtendedRequestId(toBase64(createExtendedId().getBytes()));
        builder.setAdditionalDetails(additionalDetails);
        builder.addAdditionalDetail("Error", builder.getExtendedRequestId());
        builder.setErrorResponseXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        return builder.build();
    }

    private static String createId() {
        return new BigInteger(80, new SecureRandom()).toString(32);
    }

    private static String createExtendedId() {
        return new BigInteger(320, new SecureRandom()).toString(32);
    }
}
