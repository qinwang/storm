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

import com.google.gson.GsonBuilder;
import com.jwplayer.sqe.language.clause.FromClause;
import com.jwplayer.sqe.language.clause.InsertIntoClause;
import com.jwplayer.sqe.language.clause.SelectClause;
import com.jwplayer.sqe.language.expression.BaseExpression;

public class Query extends BaseCommand {
    public InsertIntoClause insertInto;
    public SelectClause select;
    public FromClause from;
    public BaseExpression where;

    public Query(InsertIntoClause insertInto, SelectClause select, FromClause from, BaseExpression where) {
        this.insertInto = insertInto;
        this.select = select;
        this.from = from;
        this.where = where;
    }

    @Deprecated
    public static Query[] loadQueriesFromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(BaseExpression.class, new BaseExpression.BaseExpressionTypeAdapter());
        return gson.create().fromJson(json, Query[].class);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.Query;
    }
}
