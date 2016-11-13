/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.sql.runtime.serde.avro;

import org.apache.avro.Schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// TODO this class is reserved for supporting messages with different schemas.
// current only one schema in the cache
public class CachedSchemas implements Serializable{

    private final Map<String, Schema> cache = new HashMap<>();

    public Schema getSchema(String schemaString) {
        Schema schema = cache.get(schemaString);
        if (schema == null) {
            schema = new Schema.Parser().parse(schemaString);
            cache.put(schemaString, schema);
        }
        return schema;
    }

}