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

package eu.openg.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;
import java.util.Optional;

public class S3Service {

    private final AmazonS3 s3;

    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    public S3Service() {
        this(new AmazonS3Client());
    }

    public boolean doesBucketExist(String bucketName) {
        return s3.doesBucketExist(bucketName);
    }

    public List<Bucket> listBuckets() {
        return s3.listBuckets();
    }

    public S3Bucket getBucket(String bucketName) {
        return new S3Bucket(s3, bucketName);
    }

    public Bucket createBucket(String bucketName) {
        return s3.createBucket(bucketName);
    }

    public void deleteBucket(String bucketName) {
        s3.deleteBucket(bucketName);
    }
}
