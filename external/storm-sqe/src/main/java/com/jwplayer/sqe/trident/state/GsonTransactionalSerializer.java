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

import com.google.gson.*;
import org.apache.storm.trident.state.TransactionalValue;

import java.io.UnsupportedEncodingException;


public class GsonTransactionalSerializer extends BaseGsonSerializer<TransactionalValue> {
    public byte[] serialize(TransactionalValue tValue) {
        try {
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();
            JsonPrimitive txID = new JsonPrimitive(tValue.getTxid());
            jsonArray.add(txID);
            jsonArray.addAll(serializeType(tValue.getVal()));

            return gson.toJson(jsonArray).getBytes("UTF-8");
        } catch(UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public TransactionalValue deserialize(byte[] bytes) {
        try {
            Gson gson = new Gson();
            String json = new String(bytes, "UTF-8");
            JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
            Long txID = jsonArray.get(0).getAsLong();

            return new TransactionalValue(txID, deserializeType(jsonArray, 1, 2));
        } catch(UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
