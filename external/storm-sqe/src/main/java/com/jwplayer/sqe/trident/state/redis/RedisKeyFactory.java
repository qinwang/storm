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
package com.jwplayer.sqe.trident.state.redis;

import org.apache.storm.redis.trident.state.KeyFactory;

import java.util.List;

public class RedisKeyFactory implements KeyFactory {
    public String delimiter;
    public List<Integer> keyIndexes;
    public String prefix;
    public String suffix;

    public RedisKeyFactory(String delimiter, List<Integer> keyIndexes, String prefix, String suffix) {
        this.delimiter = delimiter;
        this.keyIndexes = keyIndexes;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public String build(List<Object> keys) {
        StringBuilder sb = new StringBuilder();

        // Append prefix, if it exists
        if(prefix != null && !prefix.equals("")) {
            sb.append(prefix);
            if(keyIndexes.size() > 0 || (suffix != null && !suffix.equals(""))) sb.append(delimiter);
        }

        // Append the keys we want based on their indexes
        for(int i = 0; i < keyIndexes.size() - 1; ++i) {
            sb.append(keys.get(keyIndexes.get(i)));
            sb.append(delimiter);
        }
        if(keyIndexes.size() > 0){
            sb.append(keys.get(keyIndexes.get(keyIndexes.size() - 1)));
        }

        // Append the suffix, if it exists
        if(suffix != null && !suffix.equals("")) {
            if(keyIndexes.size() > 0 || (prefix != null && !prefix.equals(""))) sb.append(delimiter);
            sb.append(suffix);
        }

        return sb.toString();
    }
}
