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

import com.amazonaws.services.s3.internal.AmazonS3ExceptionBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.services.s3.internal.Constants.*;
import static com.amazonaws.util.BinaryUtils.toBase64;
import static eu.openg.aws.s3.internal.AmazonS3Fake.createExtendedId;

class S3ExceptionBuilder {

    static AmazonS3Exception buildAllAccessDisabledException() {
        return buildException(
                "All access to this object has been disabled",
                "AllAccessDisabled",
                BUCKET_ACCESS_FORBIDDEN_STATUS_CODE);
    }

    static AmazonS3Exception buildNoSuchBucketException(String bucketName) {
        return buildException(
                "The specified bucket does not exist",
                "NoSuchBucket",
                NO_SUCH_BUCKET_STATUS_CODE,
                new HashMap<String, String>() {{
                    put("BucketName", bucketName);
                }});
    }

    static AmazonS3Exception buildBucketAlreadyExistsException(String bucketName) {
        return buildException(
                "The requested bucket name is not available. " +
                        "The bucket namespace is shared by all users of the system. " +
                        "Please select a different name and try again.",
                "BucketAlreadyExists", 409,
                new HashMap<String, String>() {{
                    put("BucketName", bucketName);
                }});
    }

    static AmazonS3Exception buildNoSuchKeyException(String key) {
        return buildException("The specified key does not exist.", "NoSuchKey", 404, new HashMap<String, String>() {{
            put("Key", key);
        }});
    }

    private static AmazonS3Exception buildException(String message, String errorCode, int statusCode) {
        return buildException(message, errorCode, statusCode, null);
    }

    private static AmazonS3Exception buildException(
            String message, String errorCode, int statusCode, Map<String, String> additionalDetails
    ) {
        AmazonS3Exception exception = newException(message, errorCode, statusCode, additionalDetails);
        exception.setServiceName(S3_SERVICE_DISPLAY_NAME);
        return exception;
    }

    private static AmazonS3Exception newException(
            String message, String errorCode, int statusCode, Map<String, String> additionalDetails
    ) {
        AmazonS3ExceptionBuilder builder = new AmazonS3ExceptionBuilder();
        builder.setRequestId(new BigInteger(80, new SecureRandom()).toString(32));
        builder.setErrorCode(errorCode);
        builder.setErrorMessage(message);
        builder.setStatusCode(statusCode);
        builder.setExtendedRequestId(toBase64(createExtendedId().getBytes()));
        builder.setAdditionalDetails(additionalDetails);
        builder.addAdditionalDetail("Error", builder.getExtendedRequestId());
        builder.setErrorResponseXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        return builder.build();
    }
}
