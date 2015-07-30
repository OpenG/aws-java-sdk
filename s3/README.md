# Amazon S3 SDK for Java 8

Amazon S3 (Simple Storage Service) SDK (Software Development Kit) for Java 8.

# SDK

## S3Service

Provides an interface for accessing the Amazon S3 web service.

### Methods

Name            | Parameters         | Type         | Description
----------------+--------------------+--------------+------------------------------------------------------------------------------------------
doesBucketExist | bucketName: String | boolean      | Checks if the specified bucket exists
listBuckets     |                    | List<Bucket> | Returns a list of all Amazon S3 buckets that the authenticated sender of the request owns
createBucket    | bucketName: String | Bucket       | Creates a new Amazon S3 bucket with the specified name in the default (US) region
deleteBucket    | bucketName: String |              | Deletes the specified bucket

### Example

```java
S3Service service = new S3Service();
if (!service.doesBucketExist("bucket_name))
    service.createBucket("bucket_name");
```

If you want, you can also provide your own `AmazonS3` instance as a constructor parameter instead of allowing the
service to create a new instance of `AmazonS3Client`.

# License

Amazon S3 SDK for Java 8 is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
