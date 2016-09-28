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
package com.jwplayer.sqe.language.stream.kafka;

import com.jwplayer.sqe.language.stream.StreamAdapter;
import com.jwplayer.sqe.trident.spout.kafka.FilteredOpaqueTridentKafkaSpout;
import com.jwplayer.sqe.trident.spout.kafka.FilteredTransactionalTridentKafkaSpout;
import org.apache.storm.kafka.trident.OpaqueTridentKafkaSpout;
import org.apache.storm.kafka.trident.TransactionalTridentKafkaSpout;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.state.StateType;


public class KafkaStreamAdapter extends StreamAdapter {
    KafkaStreamAdapterOptions options;

    public KafkaStreamAdapter(KafkaStreamAdapterOptions options) {
        this.options = options;
    }

    @Override
    public Stream makeStream(TridentTopology topology, String topologyName, String streamName, String objectName, StateType spoutType) {
        String txID = topologyName + "/" + streamName;

        if (options.filterReplays) {
            switch (spoutType) {
                case TRANSACTIONAL:
                    FilteredTransactionalTridentKafkaSpout tSpout =
                            new FilteredTransactionalTridentKafkaSpout(options.getKafkaConfig(topologyName, streamName, objectName), options.filterReplaysMetadataTtl);
                    return topology.newStream(txID, tSpout);
                case OPAQUE:
                    FilteredOpaqueTridentKafkaSpout oSpout =
                            new FilteredOpaqueTridentKafkaSpout(options.getKafkaConfig(topologyName, streamName, objectName), options.filterReplaysMetadataTtl);
                    return topology.newStream(txID, oSpout);
                default:
                    throw new RuntimeException(spoutType.toString() + " is not a supported state type");
            }
        } else {
            switch (spoutType) {
                case TRANSACTIONAL:
                    TransactionalTridentKafkaSpout tSpout =
                            new TransactionalTridentKafkaSpout(options.getKafkaConfig(topologyName, streamName, objectName));
                    return topology.newStream(txID, tSpout);
                case OPAQUE:
                    OpaqueTridentKafkaSpout oSpout =
                            new OpaqueTridentKafkaSpout(options.getKafkaConfig(topologyName, streamName, objectName));
                    return topology.newStream(txID, oSpout);
                default:
                    throw new RuntimeException(spoutType.toString() + " is not a supported state type");
            }
        }
    }
}
