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
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;

import java.util.ArrayList;
import java.util.List;

import static eu.openg.aws.sns.internal.SNSExceptionBuilder.*;
import static java.util.UUID.randomUUID;

public class AmazonSNSFake implements AmazonSNS {

    private static final String NOT_IMPLEMENTED = "Extend AbstractAmazonSNS to provide an implementation";

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
    public void setEndpoint(String endpoint) throws IllegalArgumentException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setRegion(Region region) throws IllegalArgumentException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ConfirmSubscriptionResult confirmSubscription(ConfirmSubscriptionRequest confirmSubscriptionRequest) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public CreatePlatformApplicationResult createPlatformApplication(
            CreatePlatformApplicationRequest createPlatformApplicationRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public GetTopicAttributesResult getTopicAttributes(GetTopicAttributesRequest getTopicAttributesRequest) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public SubscribeResult subscribe(SubscribeRequest subscribeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteEndpoint(DeleteEndpointRequest deleteEndpointRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setTopicAttributes(SetTopicAttributesRequest setTopicAttributesRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteTopic(DeleteTopicRequest deleteTopicRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void removePermission(RemovePermissionRequest removePermissionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public GetEndpointAttributesResult getEndpointAttributes(
            GetEndpointAttributesRequest getEndpointAttributesRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public GetPlatformApplicationAttributesResult getPlatformApplicationAttributes(
            GetPlatformApplicationAttributesRequest getPlatformApplicationAttributesRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListSubscriptionsResult listSubscriptions(ListSubscriptionsRequest listSubscriptionsRequest) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setSubscriptionAttributes(SetSubscriptionAttributesRequest setSubscriptionAttributesRequest) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setPlatformApplicationAttributes(
            SetPlatformApplicationAttributesRequest setPlatformApplicationAttributesRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public CreatePlatformEndpointResult createPlatformEndpoint(
            CreatePlatformEndpointRequest createPlatformEndpointRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void addPermission(AddPermissionRequest addPermissionRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public GetSubscriptionAttributesResult getSubscriptionAttributes(
            GetSubscriptionAttributesRequest getSubscriptionAttributesRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public CreateTopicResult createTopic(CreateTopicRequest createTopicRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListTopicsResult listTopics(ListTopicsRequest listTopicsRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListEndpointsByPlatformApplicationResult listEndpointsByPlatformApplication(
            ListEndpointsByPlatformApplicationRequest listEndpointsByPlatformApplicationRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deletePlatformApplication(DeletePlatformApplicationRequest deletePlatformApplicationRequest) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListPlatformApplicationsResult listPlatformApplications(
            ListPlatformApplicationsRequest listPlatformApplicationsRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setEndpointAttributes(SetEndpointAttributesRequest setEndpointAttributesRequest) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void unsubscribe(UnsubscribeRequest unsubscribeRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListSubscriptionsByTopicResult listSubscriptionsByTopic(
            ListSubscriptionsByTopicRequest listSubscriptionsByTopicRequest
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public PublishResult publish(PublishRequest publishRequest) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListSubscriptionsResult listSubscriptions() throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListTopicsResult listTopics() throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListPlatformApplicationsResult listPlatformApplications() throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ConfirmSubscriptionResult confirmSubscription(
            String topicArn, String token, String authenticateOnUnsubscribe
    ) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ConfirmSubscriptionResult confirmSubscription(String topicArn, String token) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public GetTopicAttributesResult getTopicAttributes(String topicArn) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public SubscribeResult subscribe(String topicArn, String protocol, String endpoint) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setTopicAttributes(String topicArn, String attributeName, String attributeValue) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteTopic(String topicArn) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void removePermission(String topicArn, String label) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListSubscriptionsResult listSubscriptions(String nextToken) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void setSubscriptionAttributes(String subscriptionArn, String attributeName, String attributeValue) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void addPermission(String topicArn, String label, List<String> aWSAccountIds, List<String> actionNames) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public GetSubscriptionAttributesResult getSubscriptionAttributes(String subscriptionArn) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public CreateTopicResult createTopic(String name) {
        if (!authorized)
            throw newAuthorizationErrorException(name).build();
        if (!isValidTopicName(name))
            throw newInvalidParameterException(name).build();
        topics.add(name);
        return new CreateTopicResult().withTopicArn("arn:aws:sns:us-east-1:" + clientTokenId + ":" + name);
    }

    private boolean isValidTopicName(String name) {
        return !name.contains(" ");
    }

    @Override
    public ListTopicsResult listTopics(String nextToken) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void unsubscribe(String subscriptionArn) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListSubscriptionsByTopicResult listSubscriptionsByTopic(String topicArn, String nextToken) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ListSubscriptionsByTopicResult listSubscriptionsByTopic(String topicArn) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public PublishResult publish(String topicArn, String message) throws AmazonClientException {
        final SNSTopicArn arn = new SNSTopicArn(topicArn);
        if (!clientTokenId.equals(arn.getClientTokenId()))
            throw newAmazonServiceException(
                    "No account found for the given parameters",
                    "InvalidClientTokenId",
                    403
            ).build();
        if (!topics.contains(arn.getTopic()))
            throw newNotFoundException("Topic does not exist").build();
        return new PublishResult().withMessageId(randomUUID().toString());
    }

    @Override
    public PublishResult publish(String topicArn, String message, String subject) throws AmazonClientException {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
