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
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.kafka.trident.TridentKafkaEmitter;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.spout.IOpaquePartitionedTridentSpout;
import org.apache.storm.trident.spout.IPartitionedTridentSpout;
import org.apache.storm.trident.topology.TransactionAttempt;

import java.util.List;
import java.util.Map;


public class FilteredTridentKafkaEmitter {
    private TridentKafkaEmitter emitter;
    private long hwmTtl;

    public FilteredTridentKafkaEmitter(Map conf, TopologyContext context, TridentKafkaConfig config, String topologyInstanceId, long hwmTtl) {
        this.emitter = new TridentKafkaEmitter(conf, context, config, topologyInstanceId);
        this.hwmTtl = hwmTtl;
    }

    private static class OpaqueEmitter implements IOpaquePartitionedTridentSpout.Emitter<List<GlobalPartitionInformation>, Partition, Map> {
        private IOpaquePartitionedTridentSpout.Emitter emitter;
        private long hwmTtl;

        private OpaqueEmitter(TridentKafkaEmitter tridentKafkaEmitter, long hwmTtl) {
            this.emitter = tridentKafkaEmitter.asOpaqueEmitter();
            this.hwmTtl = hwmTtl;
        }

        @SuppressWarnings("unchecked")
        public Map emitPartitionBatch(TransactionAttempt transactionAttempt, TridentCollector collector, Partition partition, Map lastMetadata) {
            FilteredTridentCollector filteredCollector = new FilteredTridentCollector(collector, 0, hwmTtl, lastMetadata); // TODO: keyIndex is hardcoded to 0 here
            Map newMetadata = (Map) emitter.emitPartitionBatch(transactionAttempt, filteredCollector, partition, lastMetadata);
            return filteredCollector.resolveMetadata(newMetadata);
        }

        @SuppressWarnings("unchecked")
        public void refreshPartitions(List<Partition> partitions) {
            emitter.refreshPartitions(partitions);
        }

        @SuppressWarnings("unchecked")
        public List<Partition> getOrderedPartitions(List<GlobalPartitionInformation> partitionInformation) {
            return emitter.getOrderedPartitions(partitionInformation);
        }

        public void close() {
            emitter.close();
        }
    }

    public IOpaquePartitionedTridentSpout.Emitter<List<GlobalPartitionInformation>, Partition, Map> asOpaqueEmitter() {
        return new OpaqueEmitter(emitter, hwmTtl);
    }

    private static class TransactionalEmitter implements IPartitionedTridentSpout.Emitter<List<GlobalPartitionInformation>, Partition, Map> {
        private IPartitionedTridentSpout.Emitter emitter;
        private long hwmTtl;

        private TransactionalEmitter(TridentKafkaEmitter tridentKafkaEmitter, long hwmTtl) {
            this.emitter = tridentKafkaEmitter.asTransactionalEmitter();
            this.hwmTtl = hwmTtl;
        }

        @SuppressWarnings("unchecked")
        public Map emitPartitionBatchNew(TransactionAttempt attempt, TridentCollector collector, Partition partition, Map lastMetadata) {
            FilteredTridentCollector filteredCollector = new FilteredTridentCollector(collector, 0, hwmTtl, lastMetadata); // TODO: keyIndex is hardcoded to 0 here
            Map newMetadata = (Map) emitter.emitPartitionBatchNew(attempt, collector, partition, lastMetadata);
            return filteredCollector.resolveMetadata(newMetadata);
        }

        @SuppressWarnings("unchecked")
        public void emitPartitionBatch(TransactionAttempt attempt, TridentCollector collector, Partition partition, Map lastMetadata) {
            FilteredTridentCollector filteredCollector = new FilteredTridentCollector(collector, 0, hwmTtl, lastMetadata); // TODO: keyIndex is hardcoded to 0 here
            emitter.emitPartitionBatch(attempt, filteredCollector, partition, lastMetadata);
        }

        @SuppressWarnings("unchecked")
        public void refreshPartitions(List<Partition> partitions) {
            emitter.refreshPartitions(partitions);
        }

        @SuppressWarnings("unchecked")
        public List<Partition> getOrderedPartitions(List<GlobalPartitionInformation> partitionInformation) {
            return emitter.getOrderedPartitions(partitionInformation);
        }

        public void close() {
            emitter.close();
        }
    }

    public IPartitionedTridentSpout.Emitter asTransactionalEmitter() {
        return new TransactionalEmitter(emitter, hwmTtl);
    }
}
