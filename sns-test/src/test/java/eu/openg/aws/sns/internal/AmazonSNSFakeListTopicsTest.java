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

import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.Topic;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AmazonSNSFakeListTopicsTest extends AbstractSNSFakeTest {

    @Test
    public void listTopicsFromEmptyRepository() {
        final ListTopicsResult result = sns.listTopics();

        assertThat(result.getNextToken()).isNull();
        assertThat(result.getTopics()).isEmpty();
    }

    @Test
    public void listSingleTopic() {
        sns.createTopic("test");
        final ListTopicsResult result = sns.listTopics();

        assertThat(result.getNextToken()).isNull();
        assertThat(arnValues(result.getTopics())).containsExactly("arn:aws:sns:us-east-1:12345:test");
    }

    @Test
    public void listMultiple() {
        final String[] topics = new String[5];
        for (int i = 0; i < 5; i++)
            topics[i] = sns.createTopic("topic" + i).getTopicArn();
        final ListTopicsResult result = sns.listTopics();

        assertThat(result.getNextToken()).isNull();
        assertThat(arnValues(result.getTopics())).containsExactly(topics);
    }

    @Test
    public void topicsAreReturnedInChunksOf100() {
        for (int i = 0; i < 120; i++)
            sns.createTopic("topic" + i);
        ListTopicsResult result = sns.listTopics();

        final String token = result.getNextToken();
        assertThat(token).isNotNull();
        assertThat(arnValues(result.getTopics()))
                .hasSize(100)
                .contains(
                        "arn:aws:sns:us-east-1:12345:topic0",
                        "arn:aws:sns:us-east-1:12345:topic1",
                        "arn:aws:sns:us-east-1:12345:topic10",
                        "arn:aws:sns:us-east-1:12345:topic100",
                        "arn:aws:sns:us-east-1:12345:topic8",
                        "arn:aws:sns:us-east-1:12345:topic80")
                .doesNotContain(
                        "arn:aws:sns:us-east-1:12345:topic9");

        result = sns.listTopics(token);
        assertThat(result.getNextToken()).isNull();
        assertThat(arnValues(result.getTopics()))
                .hasSize(20)
                .contains(
                        "arn:aws:sns:us-east-1:12345:topic81",
                        "arn:aws:sns:us-east-1:12345:topic9",
                        "arn:aws:sns:us-east-1:12345:topic90",
                        "arn:aws:sns:us-east-1:12345:topic99")
                .doesNotContain(
                        "arn:aws:sns:us-east-1:12345:topic120");
    }

    @Test
    public void topicsShouldBeReturnedInOrder() {
        final String[] topics = new String[4];
        topics[1] = sns.createTopic("10").getTopicArn();
        topics[0] = sns.createTopic("1").getTopicArn();
        topics[3] = sns.createTopic("8").getTopicArn();
        topics[2] = sns.createTopic("17").getTopicArn();
        final ListTopicsResult result = sns.listTopics();

        assertThat(result.getNextToken()).isNull();
        assertThat(arnValues(result.getTopics())).containsExactly(topics);
    }

    public List<String> arnValues(List<Topic> topics) {
        return topics.stream().map(Topic::getTopicArn).collect(Collectors.toList());
    }
}
