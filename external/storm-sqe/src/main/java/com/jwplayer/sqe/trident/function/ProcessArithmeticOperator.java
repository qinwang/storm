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

import clojure.lang.Numbers;
import clojure.lang.Ratio;
import com.jwplayer.sqe.language.expression.transform.ArithmeticOperatorType;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;


public class ProcessArithmeticOperator extends BaseFunction {
    private ArithmeticOperatorType operatorType = null;

    public ProcessArithmeticOperator(ArithmeticOperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public void execute(TridentTuple tuple, TridentCollector collector) {
        Number value1 = (Number) tuple.get(0);
        Number value2 = (Number) tuple.get(1);

        switch(operatorType) {
            case Addition:
                collector.emit(new Values(Numbers.add(value1, value2)));
                break;
            case Division:
                Number retVal = Numbers.divide(value1, value2);

                if(retVal instanceof Ratio) collector.emit(new Values(retVal.doubleValue()));
                else collector.emit(new Values(retVal));
                break;
            case Modulus:
                collector.emit(new Values(Numbers.remainder(value1, value2)));
                break;
            case Multiplication:
                collector.emit(new Values(Numbers.multiply(value1, value2)));
                break;
            case Subtraction:
                collector.emit(new Values(Numbers.minus(value1, value2)));
                break;
            default:
                throw new UnsupportedOperationException(operatorType.toString() + " is not supported");
        }
    }
}
