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

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

import static eu.openg.aws.s3.internal.FakeExceptionBuilder.buildNoSuchKeyException;

class FakeBucketsContainer {

    private final Clock clock;

    private final Bucket bucket;
    private final Map<String, FakeS3Object> objects = new HashMap<>();

    FakeBucketsContainer(Bucket bucket, Clock clock) {
        this.bucket = bucket;
        this.clock = clock;
    }

    ObjectMetadata getObjectMetadata(String key) {
        return getFakeObject(key).getMetadata();
    }

    S3Object getObject(String key) {
        return getFakeObject(key).toS3Object();
    }

    private FakeS3Object getFakeObject(String key) {
        if (!objects.containsKey(key))
            throw buildNoSuchKeyException(key);
        return objects.get(key);
    }

    PutObjectResult putObject(S3Object object) {
        object.setBucketName(bucket.getName());
        return putFakeObject(object.getKey(), new FakeS3Object(object, clock));
    }

    private PutObjectResult putFakeObject(String key, FakeS3Object object) {
        objects.put(key, object);
        return buildPutObjectResult(object);
    }

    private PutObjectResult buildPutObjectResult(FakeS3Object object) {
        PutObjectResult result = new PutObjectResult();
        result.setETag(object.getETag());
        result.setContentMd5(object.getMd5());
        return result;
    }

    void deleteObject(String key) {
        if (objects.containsKey(key))
            objects.remove(key);
    }

    Bucket toBucket() {
        return bucket;
    }
}
