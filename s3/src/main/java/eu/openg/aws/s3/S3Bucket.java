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
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;

public class S3Bucket {

    private final AmazonS3 s3;
    private final String bucketName;

    public S3Bucket(AmazonS3 s3, String bucketName) {
        this.s3 = s3;
        this.bucketName = bucketName;
    }

    S3Object getObject(String key) {
        return s3.getObject(bucketName, key);
    }

    public PutObjectResult putObject(String key, File file) {
        return s3.putObject(bucketName, key, file);
    }

    public void deleteObject(String key) {
        s3.deleteObject(bucketName, key);
    }
}
