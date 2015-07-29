# Amazon S3 Test Utils

A set of classes to help writing tests when developing using Amazon S3.

# Classes

## AmazonS3Fake (eu.openg.aws.s3.internal.AmazonS3Fake)

A class that implements amazons provided AmazonS3 interface. It is basically a simplified in-memory implementation of
the S3 storage that can be used when writing unit tests.

### Advantages of using a fake instead of the actual service

* Speed up your tests (no network latency due to in-memory implementation)
* Make your tests more repeatable (no cleanup is needed after tests since all the data is discarded together with the
  fake itself meaning your tests will run on exactly the same environment every time)
* Increase your security (fakes do not need to connect to Amazon S3 in order to run meaning you do not need to supply
  your CI server with AWS credentials in order to run your tests)

### Usage

```java
public class TestService {
    
    private static final String PREFIX = "my-unique-prefix-";
    
    private final AmazonS3 s3;
    
    public TestService(AmazonS3 s3) {
        this.s3 = s3;
    }
    
    public void createBucket(String name) {
        s3.createBucket(getUniqueBucketName(name));
    }
    
    public S3Object getObject(String bucketName, String key) {
        return s3.getObject(getUniqueBucketName(bucketName), key);
    }
    
    private static String getUniqueBucketName(String name) {
        return PREFIX + name;
    }
}
```

```java
public class TestServiceTest {
    
    private AmazonS3 s3;
    private TestService service;
    
    @Before
    public void setUp() {
        s3 = new AmazonS3Fake();
        service = new TestService(s3);
    }
    
    @Test
    public void bucketsAreCreatedWithPrefix() {
        service.createBucket("magic-bucket");
        assertThat(s3.doesBucketExist("my-unique-prefix-magic-bucket"), is(true));
    }
}
```

### Note

Buckets prefixed with keyword `existing` (i.e., `existing_bucket`) are considered already created.

## S3ThrowableAssert

A basic assert helper based on [AssertJ](http://assertj.org) for testing out Amazon exceptions.

### Usage

```java
public class AmazonExceptionTest {
    
    @Test
    public void createExistingBucket() {
        AmazonS3 s3 = new AmazonS3Fake();
        S3ThrowableAssert.assertThatThrownBy(() -> s3.createBucket("existing_bucket"))
            .hasErrorCode("BucketAlreadyExists");
    }
}
```

# Notice

Since this is pretty much a 3rd party library, there is no way to guarantee that it will work identically to how the
actual service does. Though it should be close enough for basic unit testing.

# License

Gradle Swift Plugin is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
