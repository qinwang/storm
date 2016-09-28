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
package com.jwplayer.sqe.trident.function;

import static org.junit.Assert.*;

import com.clearspring.analytics.stream.cardinality.HyperLogLog;
import com.jwplayer.sqe.trident.SingleValuesCollector;
import org.junit.Test;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.trident.tuple.TridentTupleView;
import org.apache.storm.tuple.Fields;

import java.io.IOException;
import java.util.Arrays;


public class AddFieldTest {
    @Test
    public void testAddInteger() {
        AddField addField = new AddField(1);
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields());
        SingleValuesCollector collector = new SingleValuesCollector();

        addField.execute(tuple, collector);

        assertEquals(collector.values.get(0), 1);
    }

    @Test
    public void testAddString() {
        AddField addField = new AddField("Abc");
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields());
        SingleValuesCollector collector = new SingleValuesCollector();

        addField.execute(tuple, collector);

        assertEquals(collector.values.get(0), "Abc");
    }

    @Test
    public void testAddBitmap() throws IOException {
        HyperLogLog hll = new HyperLogLog(9);
        hll.offer("a");
        hll.offer("b");
        hll.offer("C");
        AddField addField = new AddField(hll.getBytes());
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields());
        SingleValuesCollector collector = new SingleValuesCollector();

        addField.execute(tuple, collector);
        assertTrue(Arrays.equals((byte[]) collector.values.get(0), hll.getBytes()));
    }
}
