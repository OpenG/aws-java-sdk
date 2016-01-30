# Amazon SNS (Simple Notification Service) Test Utils

A set of classes to help in writing tests when developing using Amazon SNS.

# Classes

## AmazonSNSFake

A class that implements amazons provided AmazonSNS interface. It tries to mimic Amazon responses without calling the
actual service making it convenient when writing unit tests.

### Example

```java
public class TestService {
    
    private static final String PREFIX = "my-unique-prefix-";
    
    private final AmazonSNS sns;
    private final String arn;
    
    public TestService(AmazonSNS sns, String arn) {
        this.sns = sns;
        this.arn = arn;
    }
    
    public void publish(String message) {
        sns.publish(arn, message);
    }
}
```

```java
public class TestServiceTest {
    
    private AmazonSNS sns;
    private TestService service;
    
    @Before
    public void setUp() {
        sns = new AmazonSNSFake("12345");
        service = new TestService("test", sns);
    }
    
    @Test
    public void messagesAreSent() {
        assertThat(service.publish("Hello World!").getMessageId()).isNotEmpty();
    }
}
```

### Advantages of using a fake instead of the actual service

* Speed up your tests (no network latency due to in-memory implementation)
* Increase your security (fakes do not need to connect to Amazon SNS in order to run meaning you do not need to supply
  your CI server with AWS credentials in order to run your tests)

## AWSThrowableAssert

A basic assert helper based on [AssertJ](http://assertj.org) for testing out Amazon exceptions.

### Example

```java
public class AmazonExceptionTest {
    
    @Test
    public void publishToNonExistingTopic() {
        AmazonSNS sns = new AmazonSNSFake("12345");
        AWSThrowableAssert.assertThatThrownBy(() -> sns.publish("random_topic", "message"))
            .hasStatusCode(404);
    }
}
```

# License

Amazon SNS Test Utils are licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
