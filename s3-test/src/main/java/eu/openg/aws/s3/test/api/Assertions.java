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

package eu.openg.aws.s3.test.api;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

public class Assertions {

    protected Assertions() {
    }

    public static BucketAssert assertThat(Bucket actual) {
        return new BucketAssert(actual);
    }

    public static OwnerAssert assertThat(Owner actual) {
        return new OwnerAssert(actual);
    }

    public static PutObjectResultAssert assertThat(PutObjectResult actual) {
        return new PutObjectResultAssert(actual);
    }

    public static S3ObjectAssert assertThat(S3Object actual) {
        return new S3ObjectAssert(actual);
    }
}
