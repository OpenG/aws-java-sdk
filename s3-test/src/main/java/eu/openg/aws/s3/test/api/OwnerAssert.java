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

import com.amazonaws.services.s3.model.Owner;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import static eu.openg.aws.s3.test.error.ShouldHaveDisplayName.shouldHaveDisplayName;
import static org.assertj.core.util.Objects.areEqual;

public class OwnerAssert extends AbstractAssert<OwnerAssert, Owner> {

    @VisibleForTesting
    private Objects objects = Objects.instance();

    @VisibleForTesting
    private Failures failures = Failures.instance();

    protected OwnerAssert(Owner actual) {
        super(actual, OwnerAssert.class);
    }

    public OwnerAssert hasDisplayName(String expected) {
        objects.assertNotNull(info, actual);
        if (!areEqual(actual.getDisplayName(), expected))
            throw failures.failure(info, shouldHaveDisplayName(actual, expected, actual.getDisplayName()));
        return null;
    }
}
