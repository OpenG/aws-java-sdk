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

import com.amazonaws.services.sns.model.CreateTopicResult;

import static eu.openg.aws.sns.internal.SNSExceptionBuilder.buildAuthorizationException;
import static eu.openg.aws.sns.internal.SNSExceptionBuilder.buildInvalidParameterException;

public class AmazonSNSFake extends AbstractAmazonSNS {

    private final boolean authorized;

    public AmazonSNSFake(boolean authorized) {
        this.authorized = authorized;
    }

    public AmazonSNSFake() {
        this(true);
    }

    @Override
    public CreateTopicResult createTopic(String name) {
        if (!authorized)
            throw buildAuthorizationException(name);
        if (!isValidTopicName(name))
            throw buildInvalidParameterException(name);
        return new CreateTopicResult().withTopicArn("arn:aws:sns:us-east-1:12345:" + name);
    }

    private boolean isValidTopicName(String name) {
        return !name.contains(" ");
    }
}
