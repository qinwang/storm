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

import com.jwplayer.sqe.language.expression.transform.predicate.LogicalOperatorType;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;


public class ProcessLogicalOperator extends BaseFunction {
    private LogicalOperatorType operatorType = null;

    public ProcessLogicalOperator(LogicalOperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public void execute(TridentTuple tuple, TridentCollector collector) {
        Boolean retVal = tuple.getBoolean(0);

        switch(operatorType) {
            case And:
                for(int i = 1; i < tuple.size(); i++) {
                    retVal = retVal && tuple.getBoolean(i);
                }
                break;
            case Not:
                retVal = !retVal;
                break;
            case Or:
                for(int i = 1; i < tuple.size(); i++) {
                    retVal = retVal || tuple.getBoolean(i);
                }
                break;
            case Xor:
                for(int i = 1; i < tuple.size(); i++) {
                    retVal = retVal ^ tuple.getBoolean(i);
                }
                break;
            default:
                throw new UnsupportedOperationException(operatorType.toString() + " is not supported");
        }

        collector.emit(new Values(retVal));
    }
}
