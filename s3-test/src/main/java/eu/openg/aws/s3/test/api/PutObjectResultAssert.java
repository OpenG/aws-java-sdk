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

import com.amazonaws.services.s3.model.PutObjectResult;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import static eu.openg.aws.s3.test.error.ShouldHaveContentMd5.shouldHaveContentMd5;
import static eu.openg.aws.s3.test.error.ShouldHaveETag.shouldHaveETag;
import static org.assertj.core.util.Objects.areEqual;

public class PutObjectResultAssert extends AbstractAssert<PutObjectResultAssert, PutObjectResult> {

    @VisibleForTesting
    private Objects objects = Objects.instance();

    @VisibleForTesting
    private Failures failures = Failures.instance();

    protected PutObjectResultAssert(PutObjectResult actual) {
        super(actual, PutObjectResultAssert.class);
    }

    public PutObjectResultAssert hasETag(String expected) {
        objects.assertNotNull(info, actual);
        if (!areEqual(actual.getETag(), expected))
            throw failures.failure(info, shouldHaveETag(actual, expected, actual.getETag()));
        return myself;
    }

    public PutObjectResultAssert hasContentMd5(String expected) {
        objects.assertNotNull(info, actual);
        if (!areEqual(actual.getContentMd5(), expected))
            throw failures.failure(info, shouldHaveContentMd5(actual, expected));
        return myself;
    }
}
