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

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static eu.openg.aws.s3.test.api.Assertions.assertThat;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.S3ThrowableAssert.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class AmazonS3FakeObjectTest extends AmazonS3FakeTest {

    private static final String OBJECT_NAME = "new_object";

    private static final String FIXTURE_NAME = "fixtures/testFile.txt";

    private static final File FIXTURE = new File(getResource(FIXTURE_NAME).getFile());

    @Before
    public void createTestBucket() throws Exception {
        s3.createBucket(BUCKET_NAME);
    }

    @Test
    public void putObjectToAMissingBucket() {
        assertThatNoSuchBucketExists(() -> s3.putObject("missing_bucket", OBJECT_NAME, FIXTURE));
    }

    @Test
    public void putObjectToOtherOwnersBucket() {
        assertThatAllAccessIsDisabled(() -> s3.putObject("existing_bucket", OBJECT_NAME, FIXTURE));
    }

    @Test
    public void putObjectToABucket() {
        assertThat(s3.putObject(BUCKET_NAME, OBJECT_NAME, FIXTURE))
                .hasETag("c6b0eaa7399bc30123c429c09cc2dad8")
                .hasContentMd5("xrDqpzmbwwEjxCnAnMLa2A==");
    }

    @Test
    public void putObjectWithMetadataToABucket() throws IOException {
        s3.putObject(BUCKET_NAME, OBJECT_NAME, getResourceAsStream("fixtures/testFile.txt"), new ObjectMetadata() {{
            addUserMetadata("test", "value");
            addUserMetadata("someValue", "withCamelCase");
            addUserMetadata("and_another", "with_underscores");
        }});
        assertThat(s3.getObjectMetadata(BUCKET_NAME, OBJECT_NAME))
                .containsUserEntry("test", "value")
                .containsUserEntry("somevalue", "withCamelCase")
                .containsUserEntry("and_another", "with_underscores");
    }

    @Test
    public void getMissingObjectFromABucket() {
        assertThatThrownBy(() -> s3.getObject(BUCKET_NAME, OBJECT_NAME))
                .hasRequestId()
                .hasErrorCode("NoSuchKey")
                .hasErrorMessage("The specified key does not exist.")
                .hasStatusCode(404)
                .hasExtendedRequestId()
                .containAdditionalDetail("Key", OBJECT_NAME)
                .containAdditionalDetailWithKey("Error")
                .hasServiceName("Amazon S3")
                .hasErrorType(Client);
    }

    @Test
    public void getObjectFromOtherOwnersBucket() {
        assertThatAllAccessIsDisabled(() -> s3.getObject("existing_bucket", OBJECT_NAME));
    }

    @Test
    public void getObjectFromABucket() throws IOException {
        Instant addedTime = LocalDateTime.of(2015, 9, 10, 6, 15).toInstant(ZoneOffset.UTC);
        when(clock.instant()).thenReturn(addedTime);

        s3.putObject(BUCKET_NAME, OBJECT_NAME, FIXTURE);
        S3Object object = s3.getObject(BUCKET_NAME, OBJECT_NAME);

        assertThat(object)
                .hasKey(OBJECT_NAME)
                .hasBucketName(BUCKET_NAME)
                .hasSameContentAs(new FileInputStream(FIXTURE));

        assertThat(object.getObjectMetadata())
                .containsEntry("Accept-Ranges", "bytes")
                .containsEntry("ETag", "c6b0eaa7399bc30123c429c09cc2dad8")
                .containsEntry("Last-Modified", Date.from(addedTime))
                .containsEntry("Content-Length", 29l)
                .containsEntry("Content-Type", "text/plain");
    }

    @Test
    public void deleteObjectFromOtherOwnersBucket() {
        assertThatAllAccessIsDisabled(() -> s3.deleteObject("existing_bucket", OBJECT_NAME));
    }

    @Test
    public void deleteMissingObjectFromABucket() {
        assertThatThrownBy(() -> s3.getObject(BUCKET_NAME, OBJECT_NAME));
        s3.deleteObject(BUCKET_NAME, OBJECT_NAME);
    }

    @Test
    public void deleteObjectFromABucket() {
        s3.putObject(BUCKET_NAME, OBJECT_NAME, FIXTURE);
        assertThat(s3.getObject(BUCKET_NAME, OBJECT_NAME)).isNotNull();

        s3.deleteObject(BUCKET_NAME, OBJECT_NAME);

        assertThatThrownBy(() -> s3.getObject(BUCKET_NAME, OBJECT_NAME));
    }

    private InputStream getResourceAsStream(String name) {
        return currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    private static URL getResource(String name) {
        return currentThread().getContextClassLoader().getResource(name);
    }
}
