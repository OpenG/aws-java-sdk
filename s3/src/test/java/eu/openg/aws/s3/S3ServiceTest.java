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
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class S3ServiceTest {

    private AmazonS3 s3;
    private S3Service service;

    @Before
    public void setUp() {
        s3 = mock(AmazonS3.class);
        service = new S3Service(s3);
    }

    @Test
    public void createService() {
        assertThat(new S3Service()).isNotNull();
    }

    @Test
    public void doesBucketExist() {
        service.doesBucketExist("bucket_name");
        verify(s3).doesBucketExist("bucket_name");
    }

    @Test
    public void listBuckets() {
        service.listBuckets();
        verify(s3).listBuckets();
    }

    @Test
    public void createBucket() {
        service.createBucket("new_bucket");
        verify(s3).createBucket("new_bucket");
    }

    @Test
    public void deleteBucket() {
        service.deleteBucket("bucket_name");
        verify(s3).deleteBucket("bucket_name");
    }
}
