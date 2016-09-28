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
package com.jwplayer.sqe.language.expression;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FunctionExpressionTest {
    GsonBuilder gsonBuilder = null;

    @Before
    public void setup() {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BaseExpression.class,
                new BaseExpression.BaseExpressionTypeAdapter());
    }

    @After
    public void shutdown() {

    }

    @Test
    public void testMakeFunction() {

        List<BaseExpression> arguments = new ArrayList<>();

        BaseExpression integerConstant1 = gsonBuilder.create().fromJson("{\"C\":0}", BaseExpression.class);
        arguments.add(integerConstant1);
        BaseExpression integerConstant2 = gsonBuilder.create().fromJson("{\"C\":1}", BaseExpression.class);
        arguments.add(integerConstant2);
        FunctionExpression test = FunctionExpression.makeFunction("+", arguments);
        assertEquals(test.getClass().toString(),
                "class com.jwplayer.sqe.language.expression.transform.PlusArithmeticOperator");

        assertEquals(test.getFunctionName(), "+");

    }

}