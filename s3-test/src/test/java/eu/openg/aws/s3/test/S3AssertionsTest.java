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

package eu.openg.aws.s3.test;

import com.amazonaws.services.s3.model.PutObjectResult;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static eu.openg.aws.s3.test.S3Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;

public class S3AssertionsTest {

    @Test
    public void s3AssertionsShouldBringStandardAssertionsAlong() {
        assertThat(new S3Assertions()).isInstanceOf(Assertions.class);
    }

    @Test
    public void missingPutObjectResultETag() {
        assertThatThrownBy(() -> assertThat(new PutObjectResult()).hasETag())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting ETag not to be null");
    }

    @Test
    public void incorrectPutObjectResultContentMd5() {
        assertThatThrownBy(() -> assertThat(new PutObjectResult()).hasContentMd5("test"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting ContentMd5 of");
    }

    @Test
    public void unexpectedPutObjectResultContentMd5() {
        PutObjectResult result = new PutObjectResult();
        result.setContentMd5("contentMd5");

        assertThatThrownBy(() -> assertThat(result).hasContentMd5(null))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("not to have ContentMd5");
    }
}
