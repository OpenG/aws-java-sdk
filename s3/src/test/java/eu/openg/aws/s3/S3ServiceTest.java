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

import eu.openg.aws.s3.internal.AmazonS3Fake;
import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class S3ServiceTest {

    private S3Service service = new S3Service(new AmazonS3Fake());

    @Test
    public void createService() {
        assertThat(new S3Service()).isNotNull();
    }

    @Test
    public void checkForMissingBucket() {
        assertThat(service.doesBucketExist("missing_bucket")).isFalse();
    }

    @Test
    public void checkForExistingBucket() {
        assertThat(service.doesBucketExist("existing_bucket")).isTrue();
    }
}
