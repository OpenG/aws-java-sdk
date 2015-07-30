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

import com.amazonaws.services.s3.model.PutObjectResult;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveContentMd5 extends BasicErrorMessageFactory {

    public static ErrorMessageFactory shouldHaveContentMd5(PutObjectResult actual, String expectedContentMd5) {
        return expectedContentMd5 == null
                ? new ShouldHaveContentMd5(actual)
                : new ShouldHaveContentMd5(actual, expectedContentMd5);
    }

    private ShouldHaveContentMd5(PutObjectResult actual, String expectedContentMd5) {
        super("%nExpecting ContentMd5 of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>", actual, expectedContentMd5,
                actual.getContentMd5());
    }

    private ShouldHaveContentMd5(PutObjectResult actual) {
        super("%nExpecting PutObjectResult:%n  <%s>%nnot to have ContentMd5 but had:%n  <%s>", actual,
                actual.getContentMd5());
    }
}
