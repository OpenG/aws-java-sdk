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
import org.assertj.core.api.AWSThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static org.assertj.core.api.AWSThrowableAssert.assertThatThrownBy;
import static org.assertj.core.api.StrictAssertions.assertThat;

public class AmazonSNSFakeTest {

    private AmazonSNS sns;

    @Before
    public void prepareFakeService() {
        sns = new AmazonSNSFake("12345");
    }

    @Test
    public void fakeHasAllAmazonSNSMethods() {
        assertThat(new AmazonSNSFake("12345")).isInstanceOf(AmazonSNS.class);
    }

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

    @Test
    public void publishPlainTopicName() {
        assertSNSParamThrownBy(() -> sns.publish("test", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: test does not start with arn");
    }

    @Test
    public void publishWithOnlyArnPrefix() {
        assertSNSParamThrownBy(() -> sns.publish("arn", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
    }

    @Test
    public void publishWithInvalidArnProvider() {
        assertSNSParamThrownBy(() -> sns.publish("arn:test", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: arn:test has invalid partition test");
        assertSNSParamThrownBy(() -> sns.publish("arn:test123", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: arn:test123 has invalid partition test123");
        assertSNSParamThrownBy(() -> sns.publish("arn:Tst123Tst", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: arn:Tst123Tst has invalid partition Tst123Tst");
    }

    @Test
    public void publishWithOnlyArnPrefixAndProvider() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
    }

    @Test
    public void publishWithInvalidService() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:a", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: a does not conform to the ARN specification");
    }

    @Test
    public void publishWithPrefixProviderAndService() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
    }

    @Test
    public void publishWithInvalidRegion() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:a", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: a does not conform to the ARN specification");
    }

    @Test
    public void publishWithPrefixProviderServiceAndRegion() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:ab", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
    }

    @Test
    public void publishWithInvalidUID() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:ab:a", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn Reason: a does not conform to the ARN specification");
    }

    @Test
    public void publishWithPrefixProviderServiceRegionAndUID() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:ab:ab", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:ab:ab:ab:", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
    }

    @Test
    public void publishWithNonExistentAccountId() {
        assertSNSThrownBy(() -> sns.publish("arn:aws:sns:us-east-1:ab:a", "message"))
                .hasErrorCode("InvalidClientTokenId")
                .hasErrorMessage("No account found for the given parameters")
                .hasStatusCode(403);
    }

    private static AWSThrowableAssert assertSNSParamThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return assertSNSThrownBy(shouldRaiseThrowable)
                .isInstanceOf(InvalidParameterException.class)
                .hasErrorCode("InvalidParameter")
                .hasStatusCode(400);
    }

    private static AWSThrowableAssert assertSNSThrownBy(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return assertThatThrownBy(shouldRaiseThrowable)
                .hasRequestId()
                .hasServiceName("AmazonSNS")
                .hasErrorType(Client);
    }
}
