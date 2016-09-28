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
package com.jwplayer.sqe.trident.state.mongodb;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MongoDBStateOptions implements Serializable {
    public int cacheSize = 5000;
    public String db = "";
    public String globalKey = "$MONGO-DB-STATE-GLOBAL";
    public List<String> hosts = null;
    public String password = "";
    public int port = 27017;
    public String prevValueName = "Prev";
    public String replicaSet = "";
    public String txIdName = "TxId";
    public String userName = "";
    public String valueName = "Value";

    @SuppressWarnings("unchecked")
    public static MongoDBStateOptions parse(Map map) {
        MongoDBStateOptions options = new MongoDBStateOptions();

        if(map.containsKey("jw.sqe.state.mongodb.cachesize")) options.cacheSize = (int) map.get("jw.sqe.state.mongodb.cachesize");
        if(map.containsKey("jw.sqe.state.mongodb.db")) options.db = (String) map.get("jw.sqe.state.mongodb.db");
        if(map.containsKey("jw.sqe.state.mongodb.hosts")) options.hosts = (List<String>) map.get("jw.sqe.state.mongodb.hosts");
        if(map.containsKey("jw.sqe.state.mongodb.password")) options.password = (String) map.get("jw.sqe.state.mongodb.password");
        if(map.containsKey("jw.sqe.state.mongodb.port")) options.port = Integer.parseInt((String) map.get("jw.sqe.state.mongodb.port"));
        if(map.containsKey("jw.sqe.state.mongodb.replicaSet")) options.replicaSet = (String) map.get("jw.sqe.state.mongodb.replicaSet");
        if(map.containsKey("jw.sqe.state.mongodb.userName")) options.userName = (String) map.get("jw.sqe.state.mongodb.userName");

        return options;
    }
}
