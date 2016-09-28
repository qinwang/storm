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


import org.apache.storm.kafka.Broker;
import org.apache.storm.kafka.Partition;
import org.apache.storm.kafka.ZkHosts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SqeRawFullSchemeTest {
    private SqeRawFullScheme scheme;

    @Before
    public void setup() {
        scheme = new SqeRawFullScheme("test-topo", "test-stream", new ZkHosts("brokers", "path"));
    }

    @Test
    public void nullKeyTest() {
        List<Object> values = scheme.deserialize(null, null, new Partition(new Broker("host"), "topic", 1), 0);
        Assert.assertEquals(values.get(0), null);
        Assert.assertEquals(values.get(1), null);
    }
}
