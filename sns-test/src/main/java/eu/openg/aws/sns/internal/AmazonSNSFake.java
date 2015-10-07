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
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.ArrayList;
import java.util.List;

import static eu.openg.aws.sns.internal.SNSExceptionBuilder.*;
import static java.util.UUID.randomUUID;

public class AmazonSNSFake extends AbstractAmazonSNS {

    private final boolean authorized;
    private final String clientTokenId;

    private List<String> topics = new ArrayList<>();

    public AmazonSNSFake(String clientTokenId, boolean authorized) {
        this.authorized = authorized;
        this.clientTokenId = clientTokenId;
    }

    public AmazonSNSFake(String clientTokenId) {
        this(clientTokenId, true);
    }

    @Override
    public CreateTopicResult createTopic(String name) {
        if (!authorized)
            throw buildAuthorizationErrorException(name);
        if (!isValidTopicName(name))
            throw buildInvalidParameterException(name);
        topics.add(name);
        return new CreateTopicResult().withTopicArn("arn:aws:sns:us-east-1:" + clientTokenId + ":" + name);
    }

    private boolean isValidTopicName(String name) {
        return !name.contains(" ");
    }

    @Override
    public PublishResult publish(String topicArn, String message) throws AmazonClientException {
        final SNSTopicArn arn = new SNSTopicArn(topicArn);
        if (!clientTokenId.equals(arn.getClientTokenId()))
            throw buildAmazonServiceException("No account found for the given parameters", "InvalidClientTokenId", 403);
        if (!topics.contains(arn.getTopic()))
            throw buildNotFoundException("Topic does not exist");
        return new PublishResult().withMessageId(randomUUID().toString());
    }
}
