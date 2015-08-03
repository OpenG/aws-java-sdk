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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.amazonaws.util.IOUtils.toByteArray;
import static com.amazonaws.util.Md5Utils.md5AsBase64;

class FakeS3Object {

    private final S3Object object;
    private byte[] content;

    FakeS3Object(S3Object object) {
        try {
            this.object = object;
            this.content = toByteArray(object.getObjectContent());
            updateMetadata(object.getObjectMetadata());
        } catch (IOException e) {
            throw new AmazonClientException(e);
        }
    }

    String getMd5() {
        return md5AsBase64(content);
    }

    ObjectMetadata getMetadata() {
        return object.getObjectMetadata();
    }

    S3Object toS3Object() {
        object.setObjectContent(new ByteArrayInputStream(content));
        return object;
    }

    private void updateMetadata(ObjectMetadata metadata) {
        metadata.setContentLength(content.length);
    }
}
