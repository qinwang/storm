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
package com.jwplayer.sqe.language.state.mongodb;

import com.jwplayer.sqe.language.state.StateAdapter;
import com.jwplayer.sqe.language.state.StateOperationType;
import com.jwplayer.sqe.trident.state.mongodb.MongoDBState;
import com.jwplayer.sqe.trident.state.mongodb.MongoDBStateOptions;

import org.apache.storm.trident.state.StateFactory;
import org.apache.storm.trident.state.StateType;

import java.util.List;

public class MongoDBStateAdapter extends StateAdapter {
    private MongoDBStateOptions options;

    public MongoDBStateAdapter(MongoDBStateOptions options) { this.options = options; }

    @Override
    public StateFactory makeFactory(String objectName, List<String> keyFields, String valueField, StateType stateType, StateOperationType stateOperationType) {
        switch(stateType) {
            case NON_TRANSACTIONAL:
                return MongoDBState.nonTransactional(objectName, keyFields, valueField, options);
            case OPAQUE:
                return MongoDBState.opaque(objectName, keyFields, valueField, options);
            case TRANSACTIONAL:
                return MongoDBState.transactional(objectName, keyFields, valueField, options);
            default:
                throw new RuntimeException("Unknown state type: " + stateType);
        }
    }
}
