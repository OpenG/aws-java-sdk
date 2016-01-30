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
import com.amazonaws.services.sns.model.CreateTopicResult;
import eu.openg.aws.sns.internal.AmazonSNSFake;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SNSServiceTest {

    private static final String TOPIC_NAME = "topic_name";

    private AmazonSNS sns;
    private SNSService service;

    @Before
    public void setUp() throws Exception {
        sns = spy(new AmazonSNSFake("12345"));
        service = new SNSService(sns);
    }

    @Test
    public void createService() {
        assertThat(new SNSService()).isNotNull();
    }

    @Test
    public void listTopics() {
        service.listTopics().iterator();
        verify(sns).listTopics();
    }

    @Test
    public void whenFetchingFromExhaustedIteratorExceptionShouldBeThrown() {
        assertThatThrownBy(() -> service.listTopics().iterator().next())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void whenListingTopicsNextBatchIsQueriedAutomatically() {
        for (int i = 0; i < 120; i++)
            sns.createTopic("topic" + i);
        assertThat(service.listTopics()).hasSize(120);
        verify(sns).listTopics();
        verify(sns).listTopics(anyString());
    }

    @Test
    public void createTopic() {
        CreateTopicResult result = mock(CreateTopicResult.class);
        when(sns.createTopic(anyString())).thenReturn(result);

        assertThat(service.createTopic(TOPIC_NAME)).isEqualTo(result);
        verify(sns).createTopic(TOPIC_NAME);
    }

    @Test
    public void publish() {
        final String topicArn = sns.createTopic(TOPIC_NAME).getTopicArn();
        service.publish(topicArn, "message");
        verify(sns).publish(topicArn, "message");
    }
}
