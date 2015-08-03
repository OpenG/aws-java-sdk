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

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Arrays.array;

public class ObjectMetadataAssert extends AbstractAssert<ObjectMetadataAssert, ObjectMetadata> {

    @VisibleForTesting
    private Maps maps = Maps.instance();

    protected ObjectMetadataAssert(ObjectMetadata actual) {
        super(actual, ObjectMetadataAssert.class);
    }

    public ObjectMetadataAssert containsEntry(String key, Object value) {
        maps.assertContains(info, actual.getRawMetadata(), array(entry(key, value)));
        return myself;
    }

    public ObjectMetadataAssert containsKey(String key) {
        maps.assertContainsKeys(info, actual.getRawMetadata(), key);
        return myself;
    }

    public ObjectMetadataAssert containsUserEntry(String key, String value) {
        maps.assertContains(info, actual.getUserMetadata(), array(entry(key, value)));
        return myself;
    }
}
