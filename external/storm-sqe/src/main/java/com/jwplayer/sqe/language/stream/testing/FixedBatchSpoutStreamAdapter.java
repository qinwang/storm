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
package com.jwplayer.sqe.language.stream.testing;

import com.jwplayer.sqe.language.stream.StreamAdapter;
import com.jwplayer.sqe.trident.StreamMetadata;
import com.jwplayer.sqe.trident.function.AddField;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.state.StateType;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;

import java.util.List;


public class FixedBatchSpoutStreamAdapter extends StreamAdapter {
    FixedBatchSpoutOptions options;

    public FixedBatchSpoutStreamAdapter(FixedBatchSpoutOptions options) {
        this.options = options;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream makeStream(TridentTopology topology, String topologyName, String streamName, String objectName, StateType spoutType) {
        List<Object>[] values = (List<Object>[]) options.values.toArray(new List[options.values.size()]);
        FixedBatchSpout spout = new FixedBatchSpout(options.fields, options.values.size(), values);
        Stream stream = topology.newStream(topologyName + "/" + streamName, spout);
        long pid = StreamAdapter.createPid(topologyName, streamName, "");
        StreamMetadata metadata = new StreamMetadata(pid, 0, 0L);
        stream = stream.each(new Fields(), new AddField(metadata), new Fields(StreamAdapter.STREAM_METADATA_FIELD));
        return stream;
    }
}
