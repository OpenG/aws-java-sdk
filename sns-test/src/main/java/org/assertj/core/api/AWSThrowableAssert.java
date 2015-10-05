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

package org.assertj.core.api;

import com.amazonaws.AmazonServiceException;

import static org.assertj.core.api.StrictAssertions.catchThrowable;

public class AWSThrowableAssert extends AbstractThrowableAssert<AWSThrowableAssert, AmazonServiceException> {

    protected AWSThrowableAssert(AmazonServiceException actual) {
        super(actual, AWSThrowableAssert.class);
    }

    public static AWSThrowableAssert assertThatThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return new AWSThrowableAssert((AmazonServiceException) new ThrowableAssert(catchThrowable(shouldRaiseThrowable))
                .hasBeenThrown()
                .isInstanceOf(AmazonServiceException.class)
                .actual);
    }

    public AWSThrowableAssert hasRequestId() {
        Assertions.assertThat(actual.getRequestId()).isNotEmpty();
        return myself;
    }

    public AWSThrowableAssert hasErrorCode(String errorCode) {
        Assertions.assertThat(actual.getErrorCode()).isEqualTo(errorCode);
        return myself;
    }

    public AWSThrowableAssert hasErrorMessage(String errorMessage) {
        Assertions.assertThat(actual.getErrorMessage()).isEqualTo(errorMessage);
        return myself;
    }

    public AWSThrowableAssert hasStatusCode(int statusCode) {
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(statusCode);
        return myself;
    }

    public AWSThrowableAssert hasServiceName(String serviceName) {
        Assertions.assertThat(actual.getServiceName()).isEqualTo(serviceName);
        return myself;
    }
}
