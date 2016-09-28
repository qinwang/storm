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

import com.google.gson.GsonBuilder;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpressionDeserializationTest {
    GsonBuilder gsonBuilder = null;

    @Before
    public void setup() {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BaseExpression.class, new BaseExpression.BaseExpressionTypeAdapter());
    }

    @After
    public void shutdown() {

    }

    @Test
    public void testBoolean() {
        BaseExpression boolConstant1 = gsonBuilder.create().fromJson("{\"C\":true}", BaseExpression.class);
        BaseExpression boolConstant2 = gsonBuilder.create().fromJson("false", BaseExpression.class);

        assertTrue(boolConstant1 instanceof ConstantExpression);
        assertTrue(((ConstantExpression) boolConstant1).constant instanceof Boolean);
        assertTrue((Boolean) ((ConstantExpression) boolConstant1).constant);
        assertTrue(boolConstant2 instanceof ConstantExpression);
        assertTrue(((ConstantExpression) boolConstant2).constant instanceof Boolean);
        assertTrue(!(Boolean) ((ConstantExpression) boolConstant2).constant);

    }

    @Test
    public void testField() {
        BaseExpression field = gsonBuilder.create().fromJson("test", BaseExpression.class);

        assertTrue(field instanceof FieldExpression);
        assertTrue(((FieldExpression) field).fieldName.equals("test"));
    }

    @Test
    public void testFunction() {
        BaseExpression ifExpression = gsonBuilder.create().fromJson("{\"If\":[{\">\":[\"Plays\",0]},1,0]}", BaseExpression.class);
        BaseExpression sumExpression = gsonBuilder.create().fromJson("{\"Sum\":[\"Embeds\"]}", BaseExpression.class);

        assertTrue(ifExpression instanceof FunctionExpression);
        assertTrue(((FunctionExpression) ifExpression).getFunctionName().equals("if"));
        assertTrue(sumExpression instanceof FunctionExpression);
        assertTrue(((FunctionExpression) sumExpression).getFunctionName().equals("sum"));
    }

    @Test
    public void testNull() {
        BaseExpression nullConstant1 = gsonBuilder.create().fromJson("{\"C\":null}", BaseExpression.class);
        // This works as part of the arguments to a function, but doesn't work here for some reason
        //BaseExpression nullConstant2 = gsonBuilder.create().fromJson("null", BaseExpression.class);

        assertTrue(nullConstant1 instanceof ConstantExpression);
        assertNull(((ConstantExpression) nullConstant1).constant);
        //assertTrue(nullConstant2 instanceof ConstantExpression);
        //assertNull(((ConstantExpression) nullConstant2).constant);
    }

    @Test
    public void testDouble() {
        BaseExpression doubleConstant = gsonBuilder.create().fromJson("1.1", BaseExpression.class);

        assertTrue(doubleConstant instanceof ConstantExpression);
        assertTrue(((ConstantExpression) doubleConstant).constant instanceof Double);
        assertTrue((Double) ((ConstantExpression) doubleConstant).constant == 1.1);
    }

    @Test
    public void testInteger() {
        BaseExpression integerConstant = gsonBuilder.create().fromJson("{\"C\":0}", BaseExpression.class);

        assertTrue(integerConstant instanceof ConstantExpression);
        assertTrue(((ConstantExpression) integerConstant).constant instanceof Integer);
        assertTrue((Integer) ((ConstantExpression) integerConstant).constant == 0);
    }

    @Test
    public void testLong() {
        BaseExpression longConstant = gsonBuilder.create().fromJson("{\"C\":999999999999}", BaseExpression.class);

        assertTrue(longConstant instanceof ConstantExpression);
        assertTrue(((ConstantExpression) longConstant).constant instanceof Long);
        assertTrue((Long) ((ConstantExpression) longConstant).constant == 999999999999l);
    }

    @Test
    public void testString() {
        BaseExpression stringConstant = gsonBuilder.create().fromJson("{\"C\":\"test\"}", BaseExpression.class);

        assertTrue(stringConstant instanceof ConstantExpression);
        assertTrue(((ConstantExpression) stringConstant).constant instanceof String);
        assertTrue(((ConstantExpression) stringConstant).constant.equals("test"));
    }
}
