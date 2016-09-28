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
package com.jwplayer.sqe.trident.state;


import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.apache.commons.codec.binary.Base64;
import org.apache.storm.trident.state.Serializer;

public abstract class BaseGsonSerializer<T> implements Serializer<T> {
    public static JsonArray serializeType(Object value) {
        JsonArray jsonArray = new JsonArray();

        if(value == null) {
            jsonArray.add(new JsonPrimitive("N"));
            jsonArray.add(JsonNull.INSTANCE);
        } else if(value instanceof Integer) {
            jsonArray.add(new JsonPrimitive("I"));
            jsonArray.add(new JsonPrimitive((Integer) value));
        } else if(value instanceof Long) {
            jsonArray.add(new JsonPrimitive("L"));
            jsonArray.add(new JsonPrimitive((Long) value));
        } else if(value instanceof Float) {
            jsonArray.add(new JsonPrimitive("F"));
            jsonArray.add(new JsonPrimitive((Float) value));
        } else if(value instanceof Double) {
            jsonArray.add(new JsonPrimitive("D"));
            jsonArray.add(new JsonPrimitive((Double) value));
        } else if(value instanceof String) {
            jsonArray.add(new JsonPrimitive("S"));
            jsonArray.add(new JsonPrimitive((String) value));
        } else if(value instanceof byte[]) {
            jsonArray.add(new JsonPrimitive("B"));
            jsonArray.add(new JsonPrimitive(Base64.encodeBase64String((byte[]) value)));
        } else {
            throw new RuntimeException(value.getClass().getName() + "is not a supported type");
        }

        return jsonArray;
    }

    public static Object deserializeType(JsonArray jsonArray, int typeIndex, int valueIndex) {
        String type = jsonArray.get(typeIndex).getAsString();
        Object value;

        switch(type) {
            case "N":
                value = null;
                break;
            case "I":
                value = jsonArray.get(valueIndex).getAsInt();
                break;
            case "L":
                value = jsonArray.get(valueIndex).getAsLong();
                break;
            case "F":
                value = jsonArray.get(valueIndex).getAsFloat();
                break;
            case "D":
                value = jsonArray.get(valueIndex).getAsDouble();
                break;
            case "S":
                value = jsonArray.get(valueIndex).getAsString();
                break;
            case "B":
                value = Base64.decodeBase64(jsonArray.get(valueIndex).getAsString());
                break;
            default:
                throw new RuntimeException(type + " is not a supported type");
        }

        return value;
    }
}
