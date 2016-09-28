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

import com.jwplayer.sqe.language.stream.StreamAdapter;
import com.jwplayer.sqe.trident.StreamMetadata;
import org.apache.storm.kafka.FullScheme;
import org.apache.storm.kafka.Partition;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.RawScheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.nio.ByteBuffer;
import java.util.List;


public class SqeRawFullScheme extends RawScheme implements FullScheme {
    public static final String KEY_FIELD = "_key";
    public static final String VALUE_FIELD = "_value";
    public final long pid;

    public SqeRawFullScheme(String topologyName, String streamName, ZkHosts zkHosts) {
        pid = StreamAdapter.createPid(topologyName, streamName, zkHosts.brokerZkStr + zkHosts.brokerZkPath);
    }

    @Override
    public List<Object> deserialize(ByteBuffer key, ByteBuffer value, Partition partition, long offset) {
        byte[] keyBytes = key == null ? null : Utils.toByteArray(key);
        byte[] valueBytes = value == null ? null : Utils.toByteArray(value);

        return new Values(keyBytes, valueBytes, new StreamMetadata(pid, partition.partition, offset));
    }

    @Override
    public Fields getOutputFields() {
        return new Fields(KEY_FIELD, VALUE_FIELD, StreamAdapter.STREAM_METADATA_FIELD);
    }
}
