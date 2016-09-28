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
package com.jwplayer.sqe.trident.spout.kafka;

import org.apache.storm.kafka.Partition;
import org.apache.storm.kafka.trident.GlobalPartitionInformation;
import org.apache.storm.kafka.trident.TransactionalTridentKafkaSpout;
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.spout.IPartitionedTridentSpout;
import org.apache.storm.tuple.Fields;

import java.util.Map;


public class FilteredTransactionalTridentKafkaSpout implements IPartitionedTridentSpout<GlobalPartitionInformation, Partition, Map> {
    private TridentKafkaConfig config;
    private long hwmTtl;
    private TransactionalTridentKafkaSpout spout;

    public FilteredTransactionalTridentKafkaSpout(TridentKafkaConfig config, long hwmTtl) {
        this.config = config;
        this.hwmTtl = hwmTtl;
        this.spout = new TransactionalTridentKafkaSpout(config);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return spout.getComponentConfiguration();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Coordinator<GlobalPartitionInformation> getCoordinator(Map conf, TopologyContext context) {
        return spout.getCoordinator(conf, context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Emitter<GlobalPartitionInformation, Partition, Map> getEmitter(Map conf, TopologyContext context) {
        return (new FilteredTridentKafkaEmitter(conf, context, config, context.getStormId(), hwmTtl)).asTransactionalEmitter();
    }

    @Override
    public Fields getOutputFields() {
        return spout.getOutputFields();
    }
}
