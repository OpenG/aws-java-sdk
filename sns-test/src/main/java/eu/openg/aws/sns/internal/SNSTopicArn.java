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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.openg.aws.sns.internal.SNSExceptionBuilder.buildInvalidArnParameterException;
import static eu.openg.aws.sns.internal.SNSExceptionBuilder.buildInvalidParameterException;

class SNSTopicArn {

    private final static Pattern PATTERN = Pattern.compile(
            "^([a-zA-Z0-9]*)" + // ARN Prefix
                    "(:[a-zA-Z0-9]*)?" + // Provider
                    "(:[a-zA-Z0-9]*)?" + // Service
                    "(:[-a-zA-Z0-9]+)?" + // Region
                    "(:[a-zA-Z0-9]+)?" + // Account TokenId
                    "(:[a-zA-Z0-9]+)?"); // Topic

    private static final List<String> REGIONS = new ArrayList<>();

    static {
        REGIONS.add("us-east-1");
        REGIONS.add("us-west-2");
        REGIONS.add("us-west-1");
        REGIONS.add("eu-west-1");
        REGIONS.add("eu-central-1");
        REGIONS.add("ap-southeast-1");
        REGIONS.add("ap-southeast-2");
        REGIONS.add("ap-northeast-1");
        REGIONS.add("sa-east-1");
    }

    private String service;
    private String region;
    private String clientTokenId;

    SNSTopicArn(String topicArn) {
        final Matcher matcher = PATTERN.matcher(topicArn);
        if (matcher.matches()) {
            if (!"arn".equals(matcher.group(1)))
                throw buildInvalidArnParameterException(topicArn + " does not start with arn");
            if (matcher.group(2) != null)
                validateProvider(matcher.group(2).substring(1), topicArn);
            if (matcher.group(3) != null)
                setService(matcher.group(3).substring(1));
            if (matcher.group(4) != null)
                setRegion(matcher.group(4).substring(1));
            if (matcher.group(5) != null)
                setClientTokenId(matcher.group(5).substring(1));
            if (service == null || region == null)
                throw buildInvalidParameterException("TopicArn");
        } else
            throw buildInvalidParameterException("TopicArn");
    }

    private static void validateProvider(String provider, String arn) {
        if (!"aws".equals(provider))
            throw buildInvalidArnParameterException(arn + " has invalid partition " + provider);
    }

    public void setService(String service) {
        checkAgainstARNSpecification(service);
        if ("sns".equals(service))
            this.service = service;
    }

    public void setRegion(String region) {
        checkAgainstARNSpecification(region);
        if (REGIONS.contains(region))
            this.region = region;
    }

    public String getClientTokenId() {
        return clientTokenId;
    }

    public void setClientTokenId(String clientTokenId) {
        checkAgainstARNSpecification(clientTokenId);
        this.clientTokenId = clientTokenId;
    }

    private static void checkAgainstARNSpecification(String token) {
        if (token.length() < 2)
            throw buildInvalidArnParameterException(token + " does not conform to the ARN specification");
    }
}
