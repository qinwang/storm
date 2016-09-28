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

import com.jwplayer.sqe.language.expression.transform.predicate.NumberComparisonType;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;


public class CompareObjects extends BaseFunction {
    private CompareNumbers numberComparator = new CompareNumbers(NumberComparisonType.Equal);

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        Object value1 = tuple.get(0);
        Object value2 = tuple.get(1);

        if (value1 instanceof Number && value2 instanceof Number) {
            numberComparator.execute(tuple, collector);
        } else {
            if(value1 == null) collector.emit(new Values(value2 == null));
            else collector.emit(new Values(value1.equals(value2)));
        }
    }
}
