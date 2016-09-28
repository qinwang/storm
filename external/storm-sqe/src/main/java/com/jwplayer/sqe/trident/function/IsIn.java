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

import org.apache.storm.tuple.Values;
import clojure.lang.Numbers;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

public class IsIn extends BaseFunction {
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        if(tuple.size() < 2) throw new RuntimeException("IfIn function expects more than 1 value on the tuple");

        Object value = tuple.get(0);
        Boolean valueFound = false;

        if(value instanceof Number) {
            Number number = (Number) value;

            for(int i = 1; i < tuple.size(); i++) {
                if(Numbers.equiv(number, (Number) tuple.get(i))) valueFound = true;
            }
        } else {
            for(int i = 1; i < tuple.size(); i++) {
                if(value.equals(tuple.get(i))) valueFound = true;
            }
        }

        collector.emit(new Values(valueFound));
    }
}
