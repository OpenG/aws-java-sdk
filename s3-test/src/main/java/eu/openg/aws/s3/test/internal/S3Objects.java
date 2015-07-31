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

package eu.openg.aws.s3.test.internal;

import com.amazonaws.services.s3.model.S3Object;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import static eu.openg.aws.s3.test.error.s3.ShouldHaveBucketName.shouldHaveBucketName;
import static eu.openg.aws.s3.test.error.s3.ShouldHaveKey.shouldHaveKey;
import static org.assertj.core.util.Objects.areEqual;

public class S3Objects {

    private static final S3Objects INSTANCE = new S3Objects();

    @VisibleForTesting
    Failures failures = Failures.instance();

    public static S3Objects instance() {
        return INSTANCE;
    }

    S3Objects() {
    }

    public void assertHasKey(AssertionInfo info, S3Object actual, String expected) {
        assertNotNull(info, actual);
        if (!areEqual(actual.getKey(), expected))
            throw failures.failure(info, shouldHaveKey(actual, expected));
    }

    public void assertHasBucketName(AssertionInfo info, S3Object actual, String expected) {
        assertNotNull(info, actual);
        if (!areEqual(actual.getBucketName(), expected))
            throw failures.failure(info, shouldHaveBucketName(actual, expected));
    }

    private static void assertNotNull(AssertionInfo info, Object actual) {
        Objects.instance().assertNotNull(info, actual);
    }
}
