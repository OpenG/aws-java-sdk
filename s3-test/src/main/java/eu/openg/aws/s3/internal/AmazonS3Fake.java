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
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.amazonaws.services.s3.Headers.ETAG;
import static eu.openg.aws.s3.internal.AmazonS3FakeUtils.createExtendedId;
import static eu.openg.aws.s3.internal.AmazonS3FakeUtils.createId;
import static eu.openg.aws.s3.internal.FakeExceptionBuilder.*;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

public class AmazonS3Fake extends AbstractAmazonS3 {

    private static final Owner OWNER = new Owner(createExtendedId(), "test");

    private final Clock clock;

    private final Map<String, FakeBucketsContainer> buckets = new HashMap<>();
    private final Map<String, S3Object> objects = new HashMap<>();

    public AmazonS3Fake(Clock clock) {
        this.clock = clock;
    }

    public AmazonS3Fake() {
        this(Clock.systemDefaultZone());
    }

    @Override
    public Owner getS3AccountOwner() {
        return OWNER;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        return bucketName.startsWith("existing") || buckets.containsKey(bucketName);
    }

    @Override
    public List<Bucket> listBuckets() {
        return buckets.values().stream().map(FakeBucketsContainer::toBucket).collect(Collectors.toList());
    }

    @Override
    public Bucket createBucket(String bucketName) {
        if (doesBucketExist(bucketName))
            throw buildBucketAlreadyExistsException(bucketName);
        addBucket(bucketName);
        return new Bucket(bucketName);
    }

    private void addBucket(String bucketName) {
        Bucket bucket = new Bucket(bucketName);
        bucket.setOwner(OWNER);
        bucket.setCreationDate(Date.from(clock.instant()));
        buckets.put(bucketName, new FakeBucketsContainer(bucket));
    }

    @Override
    public S3Object getObject(String bucketName, String key) {
        return getBucketsContainer(bucketName).getObject(key);
    }

    @Override
    public void deleteBucket(String bucketName) {
        checkBucketAccess(bucketName);
        buckets.remove(bucketName);
    }

    @Override
    public PutObjectResult putObject(String bucketName, String key, File file) {
        try {
            return getBucketsContainer(bucketName).putObject(buildS3Object(bucketName, key, file));
        } catch (IOException e) {
            throw new AmazonClientException(e);
        }
    }

    private S3Object buildS3Object(String bucketName, String key, File file) throws IOException {
        S3Object object = new S3Object();
        object.setBucketName(bucketName);
        object.setKey(key);
        object.setObjectContent(new FileInputStream(file));
        fillMetadata(object.getObjectMetadata(), file);
        return object;
    }

    private void fillMetadata(ObjectMetadata metadata, File file) {
        metadata.setHeader("Accept-Ranges", "bytes");
        metadata.setHeader(ETAG, encodeHexString(createId().getBytes()));
        metadata.setLastModified(Date.from(clock.instant()));
        metadata.setContentLength(file.length());
        metadata.setContentType("text/plain");
    }

    @Override
    public void deleteObject(String bucketName, String key) {
        getBucketsContainer(bucketName).deleteObject(key);
    }

    private FakeBucketsContainer getBucketsContainer(String bucketName) {
        checkBucketAccess(bucketName);
        return buckets.get(bucketName);
    }

    private void checkBucketAccess(String bucketName) {
        if (!doesBucketExist(bucketName))
            throw buildNoSuchBucketException(bucketName);
        if (bucketName.startsWith("existing"))
            throw buildAllAccessDisabledException();
    }
}
