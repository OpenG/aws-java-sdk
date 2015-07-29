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

import com.amazonaws.services.s3.AbstractAmazonS3;
import com.amazonaws.services.s3.internal.AmazonS3ExceptionBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.*;

import static com.amazonaws.util.BinaryUtils.toBase64;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class AmazonS3Fake extends AbstractAmazonS3 {

    private static final Owner owner = new Owner(md5Hex(getRandomString()) + md5Hex(getRandomString()), "test");

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
            throw buildException(bucketName);
        addBucket(bucketName);
        return new Bucket(bucketName);
    }

    private void addBucket(String bucketName) {
        Bucket bucket = new Bucket(bucketName);
        bucket.setOwner(owner);
        bucket.setCreationDate(Date.from(clock.instant()));
        buckets.put(bucketName, bucket);
    }

    private static AmazonS3Exception buildException(String bucketName) {
        AmazonS3Exception exception = buildException(
                bucketName, toBase64(new BigInteger(384, new SecureRandom()).toByteArray()));
        exception.setServiceName("Amazon S3");
        return exception;
    }

    private static AmazonS3Exception buildException(String bucketName, String extendedRequestId) {
        AmazonS3ExceptionBuilder builder = new AmazonS3ExceptionBuilder();

        builder.setErrorMessage("The requested bucket name is not available. " +
                "The bucket namespace is shared by all users of the system. " +
                "Please select a different name and try again.");
        builder.setErrorResponseXml("");

        builder.setStatusCode(409);
        builder.setExtendedRequestId(extendedRequestId);
        builder.setRequestId(getRandomString());
        builder.setErrorCode("BucketAlreadyExists");

        builder.addAdditionalDetail("BucketName", bucketName);
        builder.addAdditionalDetail("Error", extendedRequestId);

        return builder.build();
    }

    private static String getRandomString() {
        return new BigInteger(80, new SecureRandom()).toString(32);
    }
}
