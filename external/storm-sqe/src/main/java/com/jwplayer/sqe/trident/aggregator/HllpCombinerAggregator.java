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

import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;

import java.io.IOException;

public class HllpCombinerAggregator extends CardinalityEstimatorCombinerAggregator {
    private int log2m;

    public HllpCombinerAggregator(int log2m) {
        this.log2m = log2m;
    }

    @Override
    public byte[] combine(byte[] bitmap1, byte[] bitmap2) {
        try {
            HyperLogLogPlus hllp1 = HyperLogLogPlus.Builder.build(bitmap1);
            HyperLogLogPlus hllp2 = HyperLogLogPlus.Builder.build(bitmap2);
            hllp1.addAll(hllp2);
            return hllp1.getBytes();
        } catch (CardinalityMergeException |IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] zero() {
        try {
            return (new HyperLogLogPlus(log2m, log2m)).getBytes();
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
