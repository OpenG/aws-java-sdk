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

package eu.openg.aws.sns.internal;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.model.AuthorizationErrorException;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static java.util.UUID.randomUUID;

class SNSExceptionBuilder {

    private final AmazonServiceException exception;

    private SNSExceptionBuilder(AmazonServiceException exception, String errorCode, int statusCode) {
        this.exception = exception;
        this.exception.setErrorCode(errorCode);
        this.exception.setStatusCode(statusCode);
    }

    static SNSExceptionBuilder newAmazonServiceException(String message, String errorCode, int statusCode) {
        return new SNSExceptionBuilder(new AmazonServiceException(message), errorCode, statusCode);
    }

    static SNSExceptionBuilder newAuthorizationErrorException(String topic) {
        return new SNSExceptionBuilder(
                new AuthorizationErrorException(
                        "User: arn:aws:iam::0:user/test is not authorized " +
                                "to perform: SNS:CreateTopic " +
                                "on resource: arn:aws:sns:us-east-1:0:" + topic),
                "AuthorizationError",
                403);
    }

    static SNSExceptionBuilder newInvalidParameterException(String reason) {
        return new SNSExceptionBuilder(
                new InvalidParameterException("Invalid parameter: " + reason),
                "InvalidParameter",
                400);
    }

    static SNSExceptionBuilder newInvalidArnParameterException(String reason) {
        return newInvalidParameterException("TopicArn Reason: " + reason);
    }

    static SNSExceptionBuilder newNotFoundException(String message) {
        return new SNSExceptionBuilder(
                new NotFoundException(message),
                "NotFound",
                404);
    }

    AmazonServiceException build() {
        exception.setRequestId(randomUUID().toString());
        exception.setServiceName("AmazonSNS");
        exception.setErrorType(Client);
        return exception;
    }
}
