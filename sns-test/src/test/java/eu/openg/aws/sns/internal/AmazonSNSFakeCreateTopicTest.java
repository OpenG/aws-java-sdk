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

import com.amazonaws.services.sns.model.AuthorizationErrorException;
import com.amazonaws.services.sns.model.InvalidParameterException;
import org.assertj.core.api.AWSThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static org.assertj.core.api.AWSThrowableAssert.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public class AmazonSNSFakeCreateTopicTest extends AbstractSNSFakeTest {

    @Test
    public void createTopicWithoutAuthorization() {
        sns = new AmazonSNSFake("12345", false);
        assertSNSThrownBy(() -> sns.createTopic("test"))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasErrorCode("AuthorizationError")
                .hasErrorMessage("User: arn:aws:iam::0:user/test " +
                        "is not authorized to perform: SNS:CreateTopic " +
                        "on resource: arn:aws:sns:us-east-1:0:test")
                .hasStatusCode(403);
    }

    @Test
    public void createTopicWithInvalidName() {
        assertSNSThrownBy(() -> sns.createTopic("test topic"))
                .isInstanceOf(InvalidParameterException.class)
                .hasErrorCode("InvalidParameter")
                .hasErrorMessage("Invalid parameter: test topic")
                .hasStatusCode(400);
    }

    @Test
    public void createTopic() {
        assertThat(sns.createTopic("test").getTopicArn()).isEqualTo("arn:aws:sns:us-east-1:12345:test");
    }

    private static AWSThrowableAssert assertSNSThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return assertThatThrownBy(shouldRaiseThrowable)
                .hasRequestId()
                .hasServiceName("AmazonSNS")
                .hasErrorType(Client);
    }
}
