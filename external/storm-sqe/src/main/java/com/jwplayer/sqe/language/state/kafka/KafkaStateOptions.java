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
package com.jwplayer.sqe.language.state.kafka;

import com.google.common.base.Joiner;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class KafkaStateOptions implements Serializable {
    public String serializerClass = "org.apache.kafka.common.serialization.ByteArraySerializer";
    public String metadataBrokerList;
    public String producerType = "async";
    public String requestRequiredAcks = "1";
    public String partitionClass = "org.apache.kafka.clients.producer.internals.DefaultPartitioner";
    public String KeyserializerClass = "org.apache.kafka.common.serialization.ByteArraySerializer";
    public String KeyType = "messagehash";

    @SuppressWarnings("unchecked")
    public static KafkaStateOptions parse(Map map) {
        KafkaStateOptions options = new KafkaStateOptions();

        if (map.containsKey("jw.sqe.state.kafka.brokers"))
            options.metadataBrokerList =
                    Joiner.on(',').join((List<String>) map.get("jw.sqe.state.kafka.brokers"));

        if (map.containsKey("jw.sqe.state.kafka.serializerClass"))
            options.serializerClass =
                    (String) map.get("jw.sqe.state.kafka.serializerClass");

        if (map.containsKey("jw.sqe.state.kafka.key.serializerClass"))
            options.KeyserializerClass =
                    (String) map.get("jw.sqe.state.kafka.key.serializerClass");

        if (map.containsKey("jw.sqe.state.kafka.partitionClass"))
            options.partitionClass =
                    (String) map.get("jw.sqe.state.kafka.partitionClass");

        if (map.containsKey("jw.sqe.state.kafka.producerType"))
            options.producerType =
                    (String) map.get("jw.sqe.state.kafka.producerType");

        if (map.containsKey("jw.sqe.state.kafka.request.requiredAcks"))
            options.requestRequiredAcks =
                    (String) map.get("jw.sqe.state.kafka.request.requiredAcks");

        if (map.containsKey("jw.sqe.state.kafka.keytype"))
            options.KeyType = ((String) map.get("jw.sqe.state.kafka.keytype")).toLowerCase();

        return options;
    }
}
