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

import com.jwplayer.sqe.trident.SingleValuesCollector;
import org.junit.Before;
import org.junit.Test;
import org.apache.storm.trident.tuple.TridentTupleView;
import org.apache.storm.tuple.Fields;



public class GetMaximumAggregatorTest {
    private GetMaximumAggregator aggregator;
    private SingleValuesCollector collector;

    @Before
    public void setup() {
        aggregator = new GetMaximumAggregator();
        collector = new SingleValuesCollector();
    }

    @Test
    public void testNumbers() {
        GetMaximumAggregator.MaximumState state = aggregator.init(null, collector);

        assertEquals(state.comparable, null);

        aggregator.aggregate(state, TridentTupleView.createFreshTuple(new Fields("Object"), 1), collector);
        aggregator.aggregate(state, TridentTupleView.createFreshTuple(new Fields("Object"), 2.0f), collector);
        aggregator.aggregate(state, TridentTupleView.createFreshTuple(new Fields("Object"), 3l), collector);

        assertEquals(state.comparable, 3l);

        state.comparable = aggregator.combine(state.comparable, null);

        assertEquals(state.comparable, 3l);

        aggregator.complete(state, collector);

        assertEquals(collector.values.get(0), 3l);
    }

    @Test
    public void testStrings() {
        GetMaximumAggregator.MaximumState state = aggregator.init(null, collector);

        assertEquals(state.comparable, null);

        aggregator.aggregate(state, TridentTupleView.createFreshTuple(new Fields("Object"), "a"), collector);
        aggregator.aggregate(state, TridentTupleView.createFreshTuple(new Fields("Object"), "b"), collector);
        aggregator.aggregate(state, TridentTupleView.createFreshTuple(new Fields("Object"), "c"), collector);

        assertEquals(state.comparable, "c");

        state.comparable = aggregator.combine(state.comparable, null);

        assertEquals(state.comparable, "c");

        aggregator.complete(state, collector);

        assertEquals(collector.values.get(0), "c");
    }
}
