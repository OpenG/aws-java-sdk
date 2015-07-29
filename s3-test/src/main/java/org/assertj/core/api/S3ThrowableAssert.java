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

import com.amazonaws.AmazonServiceException.ErrorType;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import static org.assertj.core.api.StrictAssertions.catchThrowable;

public class S3ThrowableAssert extends AbstractThrowableAssert<S3ThrowableAssert, AmazonS3Exception> {

    protected S3ThrowableAssert(AmazonS3Exception actual) {
        super(actual, S3ThrowableAssert.class);
    }

    public static S3ThrowableAssert assertThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
        return new S3ThrowableAssert((AmazonS3Exception) new ThrowableAssert(catchThrowable(shouldRaiseThrowable))
                .hasBeenThrown()
                .isInstanceOf(AmazonS3Exception.class)
                .actual);
    }

    public S3ThrowableAssert containAdditionalDetail(String key, String value) {
        Assertions.assertThat(actual.getAdditionalDetails()).as(key).containsEntry(key, value);
        return myself;
    }

    public S3ThrowableAssert containAdditionalDetailWithKey(String key) {
        Assertions.assertThat(actual.getAdditionalDetails()).containsKey(key);
        return myself;
    }

    public S3ThrowableAssert hasErrorCode(String errorCode) {
        Assertions.assertThat(actual.getErrorCode()).isEqualTo(errorCode);
        return myself;
    }

    public S3ThrowableAssert hasErrorType(ErrorType errorType) {
        Assertions.assertThat(actual.getErrorType()).isEqualTo(errorType);
        return myself;
    }

    public S3ThrowableAssert hasErrorMessage(String errorMessage) {
        Assertions.assertThat(actual.getErrorMessage()).isEqualTo(errorMessage);
        return myself;
    }

    public S3ThrowableAssert hasStatusCode(int statusCode) {
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(statusCode);
        return myself;
    }

    public S3ThrowableAssert hasServiceName(String serviceName) {
        Assertions.assertThat(actual.getServiceName()).isEqualTo(serviceName);
        return myself;
    }
}
