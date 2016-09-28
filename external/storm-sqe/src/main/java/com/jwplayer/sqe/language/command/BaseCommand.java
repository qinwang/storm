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
package com.jwplayer.sqe.language.command;

import com.google.gson.*;
import com.jwplayer.sqe.language.expression.BaseExpression;

import java.lang.reflect.Type;
import java.util.*;

public abstract class BaseCommand {
    public abstract CommandType getCommandType();

    public static class BaseCommandTypeAdapter implements JsonDeserializer<BaseCommand> {
        protected Gson gson =
                new GsonBuilder()
                .registerTypeAdapter(BaseExpression.class, new BaseExpression.BaseExpressionTypeAdapter())
                .create();

        @Override
        public BaseCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if(jsonElement.isJsonObject()) {
                Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
                if (entrySet.size() != 1) {
                    throw new RuntimeException("The following JSON should contain one and only one element:\n" + jsonElement.getAsString());
                }
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    return parseCommand(entry.getKey(), entry.getValue());
                }
            }

            throw new JsonParseException("Could not parse json:\n" + jsonElement.getAsString());
        }

        // TODO: Hello factory method, replace?
        public BaseCommand parseCommand(String commandName, JsonElement command) {
            switch(commandName.toLowerCase()) {
                case "createstream":
                    return gson.fromJson(command, CreateStream.class);
                case "query":
                    return gson.fromJson(command, Query.class);
                case "set":
                    return gson.fromJson(command, SetCommand.class);
                default:
                    throw new RuntimeException(commandName + " is not a valid command");
            }
        }
    }

    public static List<BaseCommand> load(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(BaseExpression.class, new BaseExpression.BaseExpressionTypeAdapter());
        gson.registerTypeAdapter(BaseCommand.class, new BaseCommand.BaseCommandTypeAdapter());

        BaseCommand[] commands = gson.create().fromJson(json, BaseCommand[].class);

        return Arrays.asList(commands);
    }
}
