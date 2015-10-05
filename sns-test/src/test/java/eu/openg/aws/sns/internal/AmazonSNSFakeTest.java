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

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.AuthorizationErrorException;
import com.amazonaws.services.sns.model.InvalidParameterException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AWSThrowableAssert.assertThatThrownBy;
import static org.assertj.core.api.StrictAssertions.assertThat;

public class AmazonSNSFakeTest {

    private AmazonSNS sns;

    @Before
    public void prepareFakeService() {
        sns = new AmazonSNSFake();
    }

    @Test
    public void fakeHasAllAmazonSNSMethods() {
        assertThat(new AmazonSNSFake()).isInstanceOf(AmazonSNS.class);
    }

    @Test
    public void createTopicWithoutAuthorization() {
        sns = new AmazonSNSFake(false);
        assertThatThrownBy(() -> sns.createTopic("test"))
                .isInstanceOf(AuthorizationErrorException.class)
                .hasRequestId()
                .hasErrorCode("AuthorizationError")
                .hasErrorMessage("User: arn:aws:iam::0:user/test " +
                        "is not authorized to perform: SNS:CreateTopic " +
                        "on resource: arn:aws:sns:us-east-1:0:test")
                .hasStatusCode(403)
                .hasServiceName("AmazonSNS");
    }

    @Test
    public void createTopicWithInvalidName() {
        assertThatThrownBy(() -> sns.createTopic("test topic"))
                .isInstanceOf(InvalidParameterException.class)
                .hasRequestId()
                .hasErrorCode("InvalidParameter")
                .hasErrorMessage("Invalid parameter: test topic")
                .hasStatusCode(400)
                .hasServiceName("AmazonSNS");
    }

    @Test
    public void createTopic() {
        assertThat(sns.createTopic("test").getTopicArn()).isEqualTo("arn:aws:sns:us-east-1:12345:test");
    }
}
