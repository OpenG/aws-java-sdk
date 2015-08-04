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
import java.io.InputStream;
import java.time.Clock;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static com.amazonaws.services.s3.Headers.ETAG;
import static com.amazonaws.util.Base64.encodeAsString;
import static com.amazonaws.util.IOUtils.toByteArray;
import static com.amazonaws.util.Md5Utils.computeMD5Hash;
import static org.apache.commons.codec.binary.Hex.encodeHex;

class FakeS3Object {

    private final Clock clock;

    private final S3Object object;
    private byte[] content;

    private String md5;

    FakeS3Object(S3Object object, Clock clock) {
        this.clock = clock;
        this.object = object;
        setContent(object.getObjectContent());
        updateMetadata(object.getObjectMetadata());
    }

    private void setContent(InputStream content) {
        try {
            this.content = toByteArray(content);
            updateContentMetadata(this.content);
        } catch (IOException e) {
            throw new AmazonClientException(e);
        }
    }

    private void updateContentMetadata(byte[] content) {
        ObjectMetadata metadata = object.getObjectMetadata();
        setMd5(computeMD5Hash(content));
        metadata.setContentType("text/plain");
        metadata.setContentLength(content.length);
        metadata.setLastModified(Date.from(clock.instant()));
    }

    private void updateMetadata(ObjectMetadata metadata) {
        metadata.setHeader("Accept-Ranges", "bytes");
        metadata.setUserMetadata(serializeUserMetadata(metadata.getUserMetadata()));
    }

    private static Map<String, String> serializeUserMetadata(Map<String, String> metadata) {
        return metadata.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().toLowerCase(),
                Map.Entry::getValue));
    }

    String getMd5() {
        return md5;
    }

    private void setMd5(byte[] md5) {
        this.md5 = encodeAsString(md5);
        object.getObjectMetadata().setHeader(ETAG, new String(encodeHex(md5)));
    }

    String getETag() {
        return object.getObjectMetadata().getETag();
    }

    S3Object toS3Object() {
        object.setObjectContent(new ByteArrayInputStream(content));
        return object;
    }

    ObjectMetadata getMetadata() {
        return object.getObjectMetadata();
    }
}
