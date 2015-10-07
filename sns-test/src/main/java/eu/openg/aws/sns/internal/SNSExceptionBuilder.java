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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.model.AuthorizationErrorException;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static java.util.UUID.randomUUID;

class SNSExceptionBuilder {

    static AmazonServiceException buildAmazonServiceException(String message, String errorCode, int statusCode) {
        final AmazonServiceException exception = new AmazonServiceException(message);
        assignCommonExceptionValues(exception);
        exception.setErrorCode(errorCode);
        exception.setStatusCode(statusCode);
        return exception;
    }

    static AmazonClientException buildAuthorizationErrorException(String topic) {
        final AmazonServiceException exception = new AuthorizationErrorException(
                "User: arn:aws:iam::0:user/test is not authorized " +
                        "to perform: SNS:CreateTopic " +
                        "on resource: arn:aws:sns:us-east-1:0:" + topic);
        assignCommonExceptionValues(exception);
        exception.setErrorCode("AuthorizationError");
        exception.setStatusCode(403);
        return exception;
    }

    static AmazonClientException buildInvalidParameterException(String reason) {
        final AmazonServiceException exception = new InvalidParameterException("Invalid parameter: " + reason);
        assignCommonExceptionValues(exception);
        exception.setErrorCode("InvalidParameter");
        exception.setStatusCode(400);
        return exception;
    }

    static AmazonClientException buildInvalidArnParameterException(String reason) {
        return buildInvalidParameterException("TopicArn Reason: " + reason);
    }

    static NotFoundException buildNotFoundException(String message) {
        final NotFoundException exception = new NotFoundException(message);
        assignCommonExceptionValues(exception);
        exception.setErrorCode("NotFound");
        exception.setStatusCode(404);
        return exception;
    }

    private static void assignCommonExceptionValues(AmazonServiceException exception) {
        exception.setRequestId(randomUUID().toString());
        exception.setServiceName("AmazonSNS");
        exception.setErrorType(Client);
    }
}
