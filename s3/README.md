# Amazon S3 SDK for Java 8

[![Build Status](https://travis-ci.org/OpenG/aws-java-sdk.svg?branch=master)](https://travis-ci.org/OpenG/aws-java-sdk)

Amazon S3 (Simple Storage Service) SDK (Software Development Kit) for Java 8.

# SDK

## S3Service

Provides an interface for accessing the Amazon S3 web service.

### Methods

Name            | Description
----------------|------------------------------------------------------------------------------------------
doesBucketExist | Checks if the specified bucket exists
listBuckets     | Returns a list of all Amazon S3 buckets that the authenticated sender of the request owns
getBucket       | Returns Optional of the specified Bucket
createBucket    | Creates a new Amazon S3 bucket with the specified name in the default (US) region
deleteBucket    | Deletes the specified bucket

### Example

```java
S3Service service = new S3Service();
if (!service.doesBucketExist("bucket_name"))
    service.createBucket("bucket_name");
```

## S3Bucket

Provides an interface for manipulating objects in Amazon S3 Bucket.

### Methods

Name            | Description
----------------|------------------------------------------------------------------------------------
getObject       | Gets the object stored in current Amazon S3 bucket under the specified key
putObject       | Uploads the specified file to current Amazon S3 bucket under the specified key name
deleteObject    | Deletes the specified object from current Amazon S3 bucket

### Example

```java
S3Bucket bucket = service.getBucket("bucket_name");
if (bucket.isPresent())
    bucket.getObject("object_name");
```

If you want, you can also provide your own `AmazonS3` instance as a constructor parameter instead of allowing the
service to create a new instance of `AmazonS3Client`.

# License

Amazon S3 SDK for Java 8 is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
