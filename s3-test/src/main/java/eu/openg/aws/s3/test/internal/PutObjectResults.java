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

import com.amazonaws.services.s3.model.PutObjectResult;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import static eu.openg.aws.s3.test.error.s3.ShouldHaveContentMd5.shouldHaveContentMd5;
import static eu.openg.aws.s3.test.error.s3.ShouldHaveETag.shouldHaveETag;
import static org.assertj.core.util.Objects.areEqual;

public class PutObjectResults {

    private static final PutObjectResults INSTANCE = new PutObjectResults();

    @VisibleForTesting
    Failures failures = Failures.instance();

    public static PutObjectResults instance() {
        return INSTANCE;
    }

    PutObjectResults() {
    }

    public void assertHasETag(AssertionInfo info, PutObjectResult actual) {
        assertNotNull(info, actual);
        if (actual.getETag() == null)
            throw failures.failure(info, shouldHaveETag());
    }

    public void assertHasContentMd5(AssertionInfo info, PutObjectResult actual, String expected) {
        assertNotNull(info, actual);
        if (!areEqual(actual.getContentMd5(), expected))
            throw failures.failure(info, shouldHaveContentMd5(actual, expected));
    }

    private static void assertNotNull(AssertionInfo info, Object actual) {
        Objects.instance().assertNotNull(info, actual);
    }
}
