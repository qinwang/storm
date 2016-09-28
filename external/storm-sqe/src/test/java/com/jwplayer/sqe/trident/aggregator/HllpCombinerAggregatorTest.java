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
package com.jwplayer.sqe.trident.aggregator;

import static org.junit.Assert.*;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import org.junit.Before;
import org.junit.Test;
import org.apache.storm.trident.tuple.TridentTupleView;
import org.apache.storm.tuple.Fields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HllpCombinerAggregatorTest {
    private CardinalityEstimatorCombinerAggregator aggregator;

    @Before
    public void setup() throws IOException {
        aggregator = new HllpCombinerAggregator(11);
    }

    @Test
    public void testAggregator() throws IOException {
        HyperLogLogPlus hllp = new HyperLogLogPlus(11);

        hllp.offer("a");
        hllp.offer("b");
        hllp.offer("c");

        byte[] hllpBytes = aggregator.zero();

        assertEquals(HyperLogLogPlus.Builder.build(hllpBytes).cardinality(), 0);

        List<Object> list = new ArrayList<>();
        list.add(hllpBytes);

        assertEquals(HyperLogLogPlus.Builder.build(aggregator.init(TridentTupleView.createFreshTuple(new Fields("hllp"), list))).cardinality(), 0);

        hllpBytes = aggregator.combine(hllpBytes, hllp.getBytes());

        assertEquals(HyperLogLogPlus.Builder.build(hllpBytes).cardinality(), 3);
    }
}