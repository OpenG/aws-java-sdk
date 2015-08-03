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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class S3BucketTest {

    private static final String BUCKET_NAME = "bucket_name";
    private static final String OBJECT_KEY = "object_key";

    private AmazonS3 s3;
    private S3Bucket bucket;

    @Before
    public void setUp() {
        s3 = mock(AmazonS3.class);
        bucket = new S3Bucket(s3, BUCKET_NAME);
    }

    @Test
    public void getObject() {
        S3Object result = mock(S3Object.class);
        when(s3.getObject(anyString(), anyString())).thenReturn(result);

        assertThat(bucket.getObject(OBJECT_KEY)).isEqualTo(result);
        verify(s3).getObject(BUCKET_NAME, OBJECT_KEY);
    }

    @Test
    public void putFile() {
        File file = mock(File.class);
        PutObjectResult result = mock(PutObjectResult.class);
        when(s3.putObject(anyString(), anyString(), any())).thenReturn(result);

        assertThat(bucket.putObject(OBJECT_KEY, file)).isEqualTo(result);
        verify(s3).putObject(BUCKET_NAME, OBJECT_KEY, file);
    }

    @Test
    public void putObjectWithMetadata() {
        InputStream input = mock(InputStream.class);
        PutObjectResult result = mock(PutObjectResult.class);
        ObjectMetadata metadata = mock(ObjectMetadata.class);
        when(s3.putObject(anyString(), anyString(), any(), any())).thenReturn(result);

        assertThat(bucket.putObject(OBJECT_KEY, input, metadata)).isEqualTo(result);
        verify(s3).putObject(BUCKET_NAME, OBJECT_KEY, input, metadata);
    }

    @Test
    public void deleteObject() {
        bucket.deleteObject(OBJECT_KEY);
        verify(s3).deleteObject(BUCKET_NAME, OBJECT_KEY);
    }
}
