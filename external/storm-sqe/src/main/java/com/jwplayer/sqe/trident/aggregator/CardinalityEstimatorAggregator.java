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

import org.apache.storm.tuple.Values;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import org.apache.storm.trident.operation.BaseAggregator;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

import java.io.IOException;


/* This aggregator is for creating new cardinality estimators and efficiently adding fields you want
 * to count. Once the object is created, you can use the associated CombinerAggregator for combing multiple
 * objects into a single bitmap. */
public abstract class CardinalityEstimatorAggregator extends BaseAggregator<ICardinality> {
    @Override
    public void aggregate(ICardinality cardinalityEstimator, TridentTuple tuple, TridentCollector collector) {
        // Replicate SQL COUNT(DISTINCT) functionality to not count NULLs as a countable value
        if(tuple.getValue(0) != null) cardinalityEstimator.offer(tuple.getValue(0));
    }

    @Override
    public void complete(ICardinality cardinalityEstimator, TridentCollector collector) {
        try {
            collector.emit(new Values(cardinalityEstimator.getBytes()));
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}