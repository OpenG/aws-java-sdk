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

package eu.openg.aws.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicResult;

public class SNSService {

    private final AmazonSNS sns;

    public SNSService(AmazonSNS sns) {
        this.sns = sns;
    }

    public SNSService() {
        this(new AmazonSNSClient());
    }

    public SNSTopics listTopics() {
        return new SNSTopics(sns);
    }

    public CreateTopicResult createTopic(String name) {
        return sns.createTopic(name);
    }

    public void publish(String topicArn, String message) {
        sns.publish(topicArn, message);
    }
}
