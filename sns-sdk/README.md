# Amazon S3 SDK for Java 8

Amazon SNS (Simple Notification Service) SDK (Software Development Kit) for Java 8.

# SDK

## SNSService

Provides an interface for accessing the Amazon SNS web service.

### Methods

Name        | Description
------------|---------------------------------------------------------
listTopics  | Returns a list of the requester's topics
createTopic | Creates a topic to which notifications can be published
publish     | Sends a message to all of a topic's subscribed endpoints

### Example

```java
SNSService service = new SNSService();
String topicArn = service.createTopic("topic_name").getTopicArn();
service.publish(topicArn, "Hello World!");
```

## S3Bucket

Provides an interface for manipulating objects in Amazon S3 Bucket.

### Methods

Name              | Description
------------------|-------------------------------------------------------------------------------------------------
getName           | Gets the bucket name
getObjectMetadata | Gets the metadata for the specified Amazon S3 object without actually fetching the object itself
getObject         | Gets the object stored in Amazon S3 bucket under the specified key
putObject         | Uploads the specified object to Amazon S3 bucket under the specified key name
deleteObject      | Deletes the specified object from Amazon S3 bucket

### Example

```java
S3Bucket bucket = service.getBucket("bucket_name");
if (bucket.isPresent())
    bucket.getObject("object_name");
```

#### Notice

If you want, you can also provide your own `AmazonSNSClient` instance as a constructor parameter instead of allowing the
service to create a new instance of `AmazonSNSClient`.

# License

Amazon S3 SDK for Java 8 is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
