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

import com.clearspring.analytics.stream.cardinality.HyperLogLog;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

import java.io.IOException;


public class GetCardinalityEstimation extends BaseFunction {
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        Object object = tuple.get(0);
        HyperLogLog hll;

        if(object instanceof HyperLogLog) {
            hll = (HyperLogLog) object;
        }
        else if(object instanceof byte[]) {
            try {
                hll = HyperLogLog.Builder.build((byte[]) object);
            } catch(IOException ex) {
                throw new RuntimeException("Could not build HyperLogLog object from byte array", ex);
            }
        } else {
            throw new RuntimeException(object.getClass() + " is not an appropriate HLL object or byte array");
        }

        collector.emit(new Values(hll.cardinality()));
    }
}