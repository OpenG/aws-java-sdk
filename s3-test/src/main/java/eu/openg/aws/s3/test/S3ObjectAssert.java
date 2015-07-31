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

import com.amazonaws.services.s3.model.S3Object;
import eu.openg.aws.s3.test.internal.S3Objects;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.VisibleForTesting;

import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Arrays.array;

public class S3ObjectAssert extends AbstractAssert<S3ObjectAssert, S3Object> {

    @VisibleForTesting
    private S3Objects s3Objects = S3Objects.instance();

    @VisibleForTesting
    InputStreams inputStreams = InputStreams.instance();

    @VisibleForTesting
    private Maps maps = Maps.instance();

    protected S3ObjectAssert(S3Object actual) {
        super(actual, S3ObjectAssert.class);
    }

    public S3ObjectAssert hasKey(String expected) {
        s3Objects.assertHasKey(info, actual, expected);
        return myself;
    }

    public S3ObjectAssert hasBucketName(String expected) {
        s3Objects.assertHasBucketName(info, actual, expected);
        return myself;
    }

    public S3ObjectAssert containsMetadataEntry(String key, Object value) {
        maps.assertContains(info, extractMetadata(actual), array(entry(key, value)));
        return myself;
    }

    public S3ObjectAssert containsMetadataKey(String key) {
        maps.assertContainsKeys(info, extractMetadata(actual), key);
        return myself;
    }

    private Map<String, Object> extractMetadata(S3Object actual) {
        return actual.getObjectMetadata().getRawMetadata();
    }

    public S3ObjectAssert hasSameContentAs(InputStream expected) {
        inputStreams.assertSameContentAs(info, actual.getObjectContent(), expected);
        return myself;
    }
}
