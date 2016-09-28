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
package com.jwplayer.sqe.language.expression.aggregation;

import com.jwplayer.sqe.language.expression.BaseExpression;
import com.jwplayer.sqe.language.expression.FieldExpression;
import com.jwplayer.sqe.language.expression.FunctionExpression;
import com.jwplayer.sqe.language.expression.FunctionType;
import org.apache.storm.trident.fluent.ChainedFullAggregatorDeclarer;
import org.apache.storm.trident.fluent.ChainedPartitionAggregatorDeclarer;
import org.apache.storm.trident.fluent.GroupedStream;
import org.apache.storm.trident.state.StateFactory;
import org.apache.storm.tuple.Fields;

import java.util.ArrayList;
import java.util.List;


public abstract class AggregationExpression extends FunctionExpression {
    public AggregationExpression() {
    }

    public abstract ChainedFullAggregatorDeclarer aggregate(ChainedFullAggregatorDeclarer stream,
                                                   Fields inputFields, Fields outputFields);

    @Override
    public FunctionType getFunctionType() {
        return FunctionType.Aggregation;
    }

    public abstract ChainedPartitionAggregatorDeclarer partitionAggregate(ChainedPartitionAggregatorDeclarer stream,
                                                                 Fields inputFields, Fields outputFields);

    public abstract void persistentAggregate(GroupedStream stream, Fields inputFields, StateFactory factory,
                                             Fields functionFields);

    // HACK: We override this for aggregation expressions so we only unroll the first argument so we can keep
    // constants in the 2nd (or 3rd, etc.) positions available to be pulled from the arguments and not the stream.
    // This is needed because aggregators instantiate the object they are using to aggregate before access to the
    // stream. This is particularly problematic with HllCreate, where we need a log2m value to create the HLL
    // object, but want to have this be an argument of the expression. In general, aggregation expressions should
    // only take a single argument, except in cases like this where the 2nd argument seemed a natural position
    // for such a value.
    @Override
    public List<BaseExpression> unRoll() {
        List<BaseExpression> retVal = new ArrayList<>();
        List<BaseExpression> expressionSet = getArguments().get(0).unRoll();

        if(expressionSet != null) {
            expressionSet.add(getArguments().get(0));
            getArguments().set(0, new FieldExpression(getArguments().get(0).getOutputFieldName()));
            retVal.addAll(expressionSet);
        }

        return retVal;
    }
}
