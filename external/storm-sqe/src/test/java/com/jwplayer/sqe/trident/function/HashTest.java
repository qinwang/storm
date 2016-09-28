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

import com.google.common.primitives.Longs;
import com.jwplayer.sqe.trident.SingleValuesCollector;
import org.junit.Test;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.trident.tuple.TridentTupleView;
import org.apache.storm.tuple.Fields;

import static org.junit.Assert.*;


public class HashTest {
    @Test
    public void testMurmur2Hash() {
        Hash hash = new Hash();
        SingleValuesCollector collector = new SingleValuesCollector();
        TridentTuple tuple =
                TridentTupleView.createFreshTuple(new Fields("HashName", "ValueToHash"), "Murmur2", "ABCDEF");

        hash.execute(tuple, collector);
        assertEquals(Longs.fromByteArray((byte[]) collector.values.get(0)), 0xF6106105304A543l);
    }
}
