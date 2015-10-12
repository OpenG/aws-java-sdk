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

package eu.openg.aws.s3.test.api;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import static eu.openg.aws.s3.test.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssertionsTest {

    @Test
    public void createAssertions() {
        new Assertions();
    }

    @Test
    public void incorrectBucketName() {
        assertThatThrownBy(() -> assertThat(mock(Bucket.class)).hasName("test"))
                .hasMessageContaining("have name");
    }

    @Test
    public void incorrectOwnerDisplayName() {
        assertThatThrownBy(() -> assertThat(mock(Owner.class)).hasDisplayName("test"))
                .hasMessageContaining("have displayName");
    }

    @Test
    public void missingPutObjectResultETag() {
        assertThatThrownBy(() -> assertThat(mock(PutObjectResult.class)).hasETag("test"))
                .hasMessageContaining("have ETag");
    }

    @Test
    public void incorrectPutObjectResultContentMd5() {
        assertThatThrownBy(() -> assertThat(mock(PutObjectResult.class)).hasContentMd5("test"))
                .hasMessageContaining("Expecting contentMd5 of");
    }

    @Test
    public void unexpectedPutObjectResultContentMd5() {
        PutObjectResult result = mock(PutObjectResult.class);
        when(result.getContentMd5()).thenReturn("contentMd5");

        assertThatThrownBy(() -> assertThat(result).hasContentMd5(null))
                .hasMessageContaining("not to have ContentMd5 but had");
    }

    @Test
    public void incorrectS3ObjectKey() {
        assertThatThrownBy(() -> assertThat(mock(S3Object.class)).hasKey("key"))
                .hasMessageContaining("Expecting key of");
    }

    @Test
    public void unexpectedS3ObjectKey() {
        S3Object object = mock(S3Object.class);
        when(object.getKey()).thenReturn("key");

        assertThatThrownBy(() -> assertThat(object).hasKey(null))
                .hasMessageContaining("not to have key but had");
    }

    @Test
    public void incorrectS3ObjectBucketName() {
        assertThatThrownBy(() -> assertThat(mock(S3Object.class)).hasBucketName("bucket"))
                .hasMessageContaining("Expecting bucketName of");
    }

    @Test
    public void unexpectedS3ObjectBucketName() {
        S3Object object = mock(S3Object.class);
        when(object.getBucketName()).thenReturn("bucketName");

        assertThatThrownBy(() -> assertThat(object).hasBucketName(null))
                .hasMessageContaining("not to have bucketName but had");
    }

    private AbstractThrowableAssert<?, ?> assertThatThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return org.assertj.core.api.Assertions.assertThatThrownBy(shouldRaiseThrowable)
                .isInstanceOf(AssertionError.class);
    }
}
