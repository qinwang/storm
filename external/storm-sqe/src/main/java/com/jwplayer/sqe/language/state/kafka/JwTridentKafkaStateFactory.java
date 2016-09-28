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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.storm.kafka.trident.TridentKafkaState;
import org.apache.storm.kafka.trident.mapper.TridentTupleToKafkaMapper;
import org.apache.storm.kafka.trident.selector.KafkaTopicSelector;
import org.apache.storm.trident.state.State;
import org.apache.storm.trident.state.StateFactory;
import org.apache.storm.task.IMetricsContext;


public class JwTridentKafkaStateFactory implements StateFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JwTridentKafkaStateFactory.class);

    private TridentTupleToKafkaMapper mapper;
    private KafkaTopicSelector topicSelector;
    public KafkaStateOptions kafka_options;

    public JwTridentKafkaStateFactory withTridentTupleToKafkaMapper(TridentTupleToKafkaMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public JwTridentKafkaStateFactory withKafkaOptions(KafkaStateOptions options) {
        kafka_options = options;
        return this;
    }

    public JwTridentKafkaStateFactory withKafkaTopicSelector(KafkaTopicSelector selector) {
        this.topicSelector = selector;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        LOG.info("makeState(partitonIndex={}, numpartitions={}", partitionIndex, numPartitions);

        TridentKafkaState state = new TridentKafkaState()
                .withKafkaTopicSelector(this.topicSelector)
                .withTridentTupleToKafkaMapper(this.mapper);
        Properties kafkaConf = new Properties();
        for(Map.Entry entry: (Set<Map.Entry>) this.getconf(conf).entrySet()) {
            if(entry.getValue() != null) {
                kafkaConf.put(entry.getKey(), entry.getValue());
            }
        }
        state.prepare(kafkaConf);
        return state;
    }

    @SuppressWarnings("unchecked")
    public Map getconf(Map conf) {
        KafkaStateOptions sqeKafkaOptions = kafka_options;
        Map kafkaConf = new HashMap();
        kafkaConf.putAll(conf);
        // Storm 1.0 uses the Kafka 0.9 producer, which has different names for the configs
        kafkaConf.put("bootstrap.servers", sqeKafkaOptions.metadataBrokerList);
        kafkaConf.put("acks", sqeKafkaOptions.requestRequiredAcks);
        kafkaConf.put("producer.type",  sqeKafkaOptions.producerType);
        kafkaConf.put("key.serializer",sqeKafkaOptions.KeyserializerClass);
        kafkaConf.put("value.serializer", sqeKafkaOptions.serializerClass);
        kafkaConf.put("partitioner.class", sqeKafkaOptions.partitionClass);

        return kafkaConf;
    }
}
