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

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static eu.openg.aws.s3.test.S3Assertions.assertThat;

public class AmazonS3FakeObjectTest extends AmazonS3FakeTest {

    @Before
    public void createTestBucket() throws Exception {
        s3.createBucket(BUCKET_NAME);
    }

    @Test
    public void putObjectToAMissingBucket() {
        assertThatNoSuchBucketExists(() ->
                s3.putObject("missing_bucket", "new_object", getResourceAsFile("fixtures/testFile.txt")));
    }

    @Test
    public void putObjectToOtherOwnersBucket() {
        assertThatAllAccessIsDisabled(() ->
                s3.putObject("existing_bucket", "new_object", getResourceAsFile("fixtures/testFile.txt")));
    }

    @Test
    public void putObjectToABucket() {
        assertThat(s3.putObject(BUCKET_NAME, "new_object", getResourceAsFile("fixtures/testFile.txt")))
                .hasETag()
                .hasContentMd5("xrDqpzmbwwEjxCnAnMLa2A==");
    }

    private File getResourceAsFile(String name) {
        try {
            return new File(getResource(name).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getResource(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }
}
