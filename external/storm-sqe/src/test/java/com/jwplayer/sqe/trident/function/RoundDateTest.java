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

import com.jwplayer.sqe.trident.SingleValuesCollector;
import org.junit.Test;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.trident.tuple.TridentTupleView;
import org.apache.storm.tuple.Fields;

import java.util.Date;


public class RoundDateTest {
    @Test
    public void testRoundSeconds() {
        RoundDate roundDate = new RoundDate();
        Date date = new Date(1000 * 105 + 5);
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields("Date", "Amount", "Unit"), date, 10, "Second");
        SingleValuesCollector collector = new SingleValuesCollector();

        roundDate.execute(tuple, collector);

        assertEquals(collector.values.get(0), new Date(1000 * 100));
    }

    @Test
    public void testRoundMinutes() {
        RoundDate roundDate = new RoundDate();
        Date date = new Date(1000 * 60 * 105 + 1000);
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields("Date", "Amount", "Unit"), date, 10, "Minute");
        SingleValuesCollector collector = new SingleValuesCollector();

        roundDate.execute(tuple, collector);

        assertEquals(collector.values.get(0), new Date(1000 * 60 * 100));
    }

    @Test
    public void testRoundHours() {
        RoundDate roundDate = new RoundDate();
        Date date = new Date(1000 * 60 * 60 * 105 + 1000 * 60);
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields("Date", "Amount", "Unit"), date, 10, "Hour");
        SingleValuesCollector collector = new SingleValuesCollector();

        roundDate.execute(tuple, collector);

        assertEquals(collector.values.get(0), new Date(1000 * 60 * 60 * 100));
    }

    @Test
    public void testRoundDays() {
        RoundDate roundDate = new RoundDate();
        Date date = new Date(1000l * 60l * 60l * 24l * 105l + 1000l * 60l * 60l);
        TridentTuple tuple = TridentTupleView.createFreshTuple(new Fields("Date", "Amount", "Unit"), date, 10, "Day");
        SingleValuesCollector collector = new SingleValuesCollector();

        roundDate.execute(tuple, collector);

        assertEquals(collector.values.get(0), new Date(1000l * 60l * 60l * 24l * 100l));
    }
}
