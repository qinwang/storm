/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jwplayer.sqe.language.serde;

import com.jwplayer.sqe.language.serde.avro.AvroDeserializer;
import com.jwplayer.sqe.language.serde.avro.AvroDeserializerOptions;
import org.apache.storm.trident.Stream;
import org.apache.storm.tuple.Fields;

import java.util.Map;


public abstract class BaseDeserializer {
    public abstract Stream deserialize(Stream stream, Fields requiredFields);

    public static BaseDeserializer makeDeserializer(String deserializerName, Map deserializerOptions) {
        switch(deserializerName.toLowerCase()) {
            case "avro":
                AvroDeserializerOptions avroOptions = AvroDeserializerOptions.parse(deserializerOptions);
                return new AvroDeserializer(avroOptions);
            case "identity":
                return new IdentityDeserializer();
            default:
                throw new RuntimeException(deserializerName + " is not a supported deserializer");
        }
    }
}
