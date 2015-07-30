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

package eu.openg.aws.s3.internal;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Before;

import java.time.Clock;
import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AmazonS3FakeTest {

    protected Clock clock;
    protected AmazonS3 s3;

    @Before
    public void prepareFakeService() {
        clock = mock(Clock.class);
        when(clock.instant()).thenReturn(Instant.now());
        s3 = new AmazonS3Fake(clock);
    }
}
