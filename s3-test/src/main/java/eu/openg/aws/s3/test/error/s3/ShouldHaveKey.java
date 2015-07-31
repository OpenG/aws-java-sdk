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

package eu.openg.aws.s3.test.error.s3;

import com.amazonaws.services.s3.model.S3Object;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveKey extends BasicErrorMessageFactory {

    public static ErrorMessageFactory shouldHaveKey(S3Object actual, String expected) {
        return expected == null ? new ShouldHaveKey(actual) : new ShouldHaveKey(actual, expected);
    }

    private ShouldHaveKey(S3Object actual, String expected) {
        super("%nExpecting key of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>", actual, expected, actual.getKey());
    }

    private ShouldHaveKey(S3Object actual) {
        super("%nExpecting S3Object:%n  <%s>%nnot to have key but had:%n  <%s>", actual, actual.getKey());
    }
}
