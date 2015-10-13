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
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.Topic;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SNSTopicIterator implements Iterator<Topic> {

    private final AmazonSNS sns;

    private ListTopicsResult result;
    private List<Topic> topics;
    private int cursor = 0;

    SNSTopicIterator(AmazonSNS sns) {
        this.sns = sns;
        this.result = sns.listTopics();
        this.topics = result.getTopics();
    }

    @Override
    public boolean hasNext() {
        return cursor != topics.size() || result.getNextToken() != null;
    }

    @Override
    public Topic next() {
        if (cursor == topics.size() && result.getNextToken() != null) {
            result = sns.listTopics(result.getNextToken());
            topics = result.getTopics();
            cursor = 0;
        }
        try {
            return topics.get(cursor++);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }
}
