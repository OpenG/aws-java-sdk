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

import com.amazonaws.services.s3.model.S3Object;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import java.io.InputStream;
import java.util.Map;

import static eu.openg.aws.s3.test.error.ShouldHaveBucketName.shouldHaveBucketName;
import static eu.openg.aws.s3.test.error.ShouldHaveKey.shouldHaveKey;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Objects.areEqual;

public class S3ObjectAssert extends AbstractAssert<S3ObjectAssert, S3Object> {

    @VisibleForTesting
    private Objects objects = Objects.instance();

    @VisibleForTesting
    private Failures failures = Failures.instance();

    @VisibleForTesting
    private InputStreams inputStreams = InputStreams.instance();

    protected S3ObjectAssert(S3Object actual) {
        super(actual, S3ObjectAssert.class);
    }

    public S3ObjectAssert hasKey(String expected) {
        objects.assertNotNull(info, actual);
        if (!areEqual(actual.getKey(), expected))
            throw failures.failure(info, shouldHaveKey(actual, expected));
        return myself;
    }

    public S3ObjectAssert hasBucketName(String expected) {
        objects.assertNotNull(info, actual);
        if (!areEqual(actual.getBucketName(), expected))
            throw failures.failure(info, shouldHaveBucketName(actual, expected));
        return myself;
    }

    public S3ObjectAssert hasSameContentAs(InputStream expected) {
        inputStreams.assertSameContentAs(info, actual.getObjectContent(), expected);
        return myself;
    }
}
