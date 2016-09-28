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
package com.jwplayer.sqe.language.state.redis;

import org.apache.storm.redis.trident.state.RedisDataTypeDescription.RedisDataType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisStateOptions implements Serializable {
    public int database = 0;
    public RedisDataType dataType = RedisDataType.STRING;
    public String delimiter = ":";
    public List<String> fieldNameFields = new ArrayList<>();
    public String host = "";
    public List<String> keyNameFields = new ArrayList<>();
    public int port = 6379;
    public int expireintervalsec = 0;

    @SuppressWarnings("unchecked")
    public static RedisStateOptions parse(Map map) {
        RedisStateOptions options = new RedisStateOptions();

        if(map.containsKey("jw.sqe.state.redis.database"))
            options.database = ((Number) map.get("jw.sqe.state.redis.database")).intValue();
        if(map.containsKey("jw.sqe.state.redis.datatype"))
            options.dataType = RedisDataType.valueOf((String) map.get("jw.sqe.state.redis.datatype"));
        if(map.containsKey("jw.sqe.state.redis.delimiter"))
            options.delimiter = (String) map.get("jw.sqe.state.redis.delimiter");
        if(map.containsKey("jw.sqe.state.redis.expireintervalsec"))
            options.expireintervalsec = ((Number)map.get("jw.sqe.state.redis.expireintervalsec")).intValue();
        if(map.containsKey("jw.sqe.state.redis.fieldname.fields"))
            options.fieldNameFields = (List<String>) map.get("jw.sqe.state.redis.fieldname.fields");
        if(map.containsKey("jw.sqe.state.redis.host"))
            options.host = (String) map.get("jw.sqe.state.redis.host");
        if(map.containsKey("jw.sqe.state.redis.keyname.fields"))
            options.keyNameFields = (List<String>) map.get("jw.sqe.state.redis.keyname.fields");
        if(map.containsKey("jw.sqe.state.redis.port"))
            options.port = Integer.parseInt((String) map.get("jw.sqe.state.redis.port"));

        return options;
    }
}
