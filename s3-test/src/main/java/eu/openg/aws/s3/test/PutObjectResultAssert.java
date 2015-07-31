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

import com.amazonaws.services.s3.model.PutObjectResult;
import org.assertj.core.api.AbstractAssert;
import eu.openg.aws.s3.test.internal.PutObjectResults;
import org.assertj.core.util.VisibleForTesting;

public class PutObjectResultAssert extends AbstractAssert<PutObjectResultAssert, PutObjectResult> {

    @VisibleForTesting
    private PutObjectResults putObjectResults = PutObjectResults.instance();

    protected PutObjectResultAssert(PutObjectResult actual) {
        super(actual, PutObjectResultAssert.class);
    }

    public PutObjectResultAssert hasETag() {
        putObjectResults.assertHasETag(info, actual);
        return myself;
    }

    public PutObjectResultAssert hasContentMd5(String expected) {
        putObjectResults.assertHasContentMd5(info, actual, expected);
        return myself;
    }
}
