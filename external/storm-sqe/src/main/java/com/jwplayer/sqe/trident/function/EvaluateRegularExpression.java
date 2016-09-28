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

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class EvaluateRegularExpression extends BaseFunction {
    Map<String, Pattern> patternCache = new HashMap<>();

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        Pattern pattern;
        String regex = tuple.getString(1);

        if(patternCache.containsKey(regex)) {
            pattern = patternCache.get(regex);
        } else {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }

        collector.emit(new Values(pattern.matcher(tuple.getString(0)).matches()));
    }
}
