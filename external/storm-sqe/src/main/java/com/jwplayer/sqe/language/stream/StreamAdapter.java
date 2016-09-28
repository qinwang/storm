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
package com.jwplayer.sqe.language.stream;

import com.clearspring.analytics.hash.MurmurHash;
import com.jwplayer.sqe.language.stream.kafka.KafkaStreamAdapter;
import com.jwplayer.sqe.language.stream.kafka.KafkaStreamAdapterOptions;
import com.jwplayer.sqe.language.stream.testing.FixedBatchSpoutOptions;
import com.jwplayer.sqe.language.stream.testing.FixedBatchSpoutStreamAdapter;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.state.StateType;

import java.util.Map;


public abstract class StreamAdapter {
    public abstract Stream makeStream(TridentTopology topology, String topologyName, String streamName, String objectName, StateType spoutType);

    public static final String STREAM_METADATA_FIELD = "_stream_metadata";

    public static Long createPid(String topologyName, String streamName, String secondaryID) {
        return MurmurHash.hash64(topologyName + streamName + secondaryID);
    }

    public static StreamAdapter makeAdapter(String spoutName, Map options) {
        switch(spoutName.toLowerCase()) {
            case "fixed":
                FixedBatchSpoutOptions fixedOptions = FixedBatchSpoutOptions.parse(options);
                return new FixedBatchSpoutStreamAdapter(fixedOptions);
            case "kafka":
                KafkaStreamAdapterOptions kafkaOptions = KafkaStreamAdapterOptions.parse(options);
                return new KafkaStreamAdapter(kafkaOptions);
            default:
                throw new RuntimeException("Unknown spout name: " + spoutName);
        }
    }
}
