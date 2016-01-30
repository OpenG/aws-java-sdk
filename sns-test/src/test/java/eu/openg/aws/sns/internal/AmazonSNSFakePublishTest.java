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

import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;
import org.assertj.core.api.AWSThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import static com.amazonaws.AmazonServiceException.ErrorType.Client;
import static org.assertj.core.api.AWSThrowableAssert.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public class AmazonSNSFakePublishTest extends AbstractSNSFakeTest {

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

    @Test
    public void publishWithoutATopic() {
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:sns:us-east-1:12345", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
        assertSNSParamThrownBy(() -> sns.publish("arn:aws:sns:us-east-1:12345:", "message"))
                .hasErrorMessage("Invalid parameter: TopicArn");
    }

    @Test
    public void publishToNonExistentTopic() {
        assertSNSThrownBy(() -> sns.publish("arn:aws:sns:us-east-1:12345:a", "message"))
                .isInstanceOf(NotFoundException.class)
                .hasErrorCode("NotFound")
                .hasErrorMessage("Topic does not exist")
                .hasStatusCode(404);
    }

    @Test
    public void publishToACreatedTopic() {
        assertThat(sns.publish(sns.createTopic("test").getTopicArn(), "message").getMessageId()).isNotNull();
        sns = new AmazonSNSFake("54321");
        assertThat(sns.publish(sns.createTopic("test").getTopicArn(), "message").getMessageId()).isNotEmpty();
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
