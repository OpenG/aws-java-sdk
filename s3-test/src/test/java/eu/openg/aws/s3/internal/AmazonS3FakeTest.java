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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.S3ThrowableAssert.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AmazonS3FakeTest {

    private Clock clock;
    private AmazonS3 s3;

    @Before
    public void setUp() {
        clock = mock(Clock.class);
        when(clock.instant()).thenReturn(Instant.now());
        s3 = new AmazonS3Fake(clock);
    }

    @Test
    public void createFake() {
        assertThat(new AmazonS3Fake()).isInstanceOf(AmazonS3.class);
    }

    @Test
    public void checkForMissingBucket() {
        assertThat(s3.doesBucketExist("missing_bucket")).isFalse();
    }

    @Test
    public void checkForExistingBucket() {
        assertThat(s3.doesBucketExist("existing_bucket")).isTrue();
    }

    @Test
    public void createABucketThatAlreadyExists() {
        assertThatThrownBy(() -> s3.createBucket("existing_bucket"))
                .hasErrorMessage(
                        "The requested bucket name is not available. " +
                                "The bucket namespace is shared by all users of the system. " +
                                "Please select a different name and try again.")
                .containAdditionalDetail("BucketName", "existing_bucket")
                .containAdditionalDetailWithKey("Error")
                .hasErrorCode("BucketAlreadyExists")
                .hasStatusCode(409)
                .hasServiceName("Amazon S3")
                .hasErrorType(Client)
                .matches(e -> e.getRequestId() != null && e.getExtendedRequestId() != null);
    }

    @Test
    public void createANewBucket() {
        assertThat(s3.createBucket("new_bucket")).isEqualToComparingFieldByField(new Bucket("new_bucket"));
        assertThat(s3.doesBucketExist("new_bucket")).isTrue();
    }

    @Test
    public void listCurrentBuckets() {
        assertThat(s3.listBuckets()).hasSize(0);
        s3.createBucket("new_bucket");
        assertThat(s3.listBuckets()).hasSize(1);
    }

    @Test
    public void checkNewlyAddedBuckets() {
        Instant addedTime1 = LocalDateTime.of(2015, 10, 15, 12, 25).toInstant(ZoneOffset.UTC);
        Instant addedTime2 = LocalDateTime.of(2015, 10, 15, 13, 30).toInstant(ZoneOffset.UTC);
        when(clock.instant()).thenReturn(addedTime1, addedTime2);

        s3.createBucket("new_bucket_1");
        s3.createBucket("new_bucket_2");
        List<Bucket> buckets = s3.listBuckets();

        assertThat(buckets).hasSize(2);
        assertThat(getBucketWithName("new_bucket_1", buckets).getCreationDate()).isEqualTo(Date.from(addedTime1));
        assertThat(getBucketWithName("new_bucket_2", buckets).getCreationDate()).isEqualTo(Date.from(addedTime2));

        buckets.forEach(bucket -> {
            Owner owner = bucket.getOwner();
            assertThat(owner.getDisplayName()).isEqualTo("test");
            assertThat(owner.getId()).isNotEmpty();
        });
    }

    private Bucket getBucketWithName(String name, List<Bucket> buckets) {
        return buckets.stream().filter(b -> Objects.equals(b.getName(), name)).findFirst().get();
    }
}
