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

package eu.openg.aws.s3.test.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveDisplayName extends BasicErrorMessageFactory {

    public static ErrorMessageFactory shouldHaveDisplayName(Object actual, String expected, String found) {
        return new ShouldHaveDisplayName(actual, expected, found);
    }

    private ShouldHaveDisplayName(Object actual, String expected, String found) {
        super("%nExpecting%n  <%s>%nto have displayName:%n  <%s>%nbut had:%n  <%s>", actual, expected, found);
    }
}
