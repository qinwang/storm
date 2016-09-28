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
package com.jwplayer.sqe.language.state;

import com.jwplayer.sqe.language.state.kafka.KafkaStateAdapter;
import com.jwplayer.sqe.language.state.kafka.KafkaStateOptions;
import com.jwplayer.sqe.language.state.mongodb.MongoDBStateAdapter;
import com.jwplayer.sqe.language.state.redis.RedisStateAdapter;
import com.jwplayer.sqe.language.state.redis.RedisStateOptions;
import com.jwplayer.sqe.trident.state.mongodb.MongoDBStateOptions;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.state.StateFactory;
import org.apache.storm.trident.state.StateType;
import org.apache.storm.tuple.Fields;

import java.util.List;
import java.util.Map;


public abstract class StateAdapter {
    public abstract StateFactory makeFactory(String objectName, List<String> keyFields, String valueField, StateType stateType, StateOperationType stateOperationType);

    public TridentState partitionPersist(Stream stream, StateFactory stateFactory, Fields keyFields) {
        throw new UnsupportedOperationException("This adapter does not support partition persist");
    }

    public static StateAdapter makeAdapter(String stateName, Map options) {
        switch (stateName) {
            case "mongo":
                MongoDBStateOptions mongoDBStateOptions = MongoDBStateOptions.parse(options);
                return new MongoDBStateAdapter(mongoDBStateOptions);
            case "redis":
                RedisStateOptions redisStateOptions = RedisStateOptions.parse(options);
                return new RedisStateAdapter(redisStateOptions);
            case "kafka":
                KafkaStateOptions kafkaStateOptions = KafkaStateOptions.parse(options);
                return new KafkaStateAdapter(kafkaStateOptions);
            default:
                throw new RuntimeException("Unknown state name: " + stateName);
        }
    }
}
