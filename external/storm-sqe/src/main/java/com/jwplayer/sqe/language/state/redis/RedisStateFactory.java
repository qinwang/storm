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

import java.util.List;

import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.trident.state.RedisState;

/**
 * Similar to RedisState.Factory but also allows storing an objectName,
 * used to name-space hash data in Redis
 */
public class RedisStateFactory extends RedisState.Factory{
    private static final long serialVersionUID = 1L;
    public List<String> streamFields;
    public String objectName;


    /**
     * Store objectName for persisting state to Hash (additional key)
     * @param config
     * @param objectName
     */
    public RedisStateFactory(JedisPoolConfig config, String objectName, List<String> streamFields) {
        super(config);
        this.objectName = objectName;
        this.streamFields = streamFields;
    }

}
