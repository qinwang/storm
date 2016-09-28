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
package com.jwplayer.sqe.trident.function;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.util.Utf8;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;


public class GetTupleFromAvro extends BaseFunction {
    private Fields fields;
    private Schema schema = null;
    private String schemaName;

    public GetTupleFromAvro(String schemaName, Fields fields) {
        this.fields = fields;
        this.schemaName = schemaName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        Values values = new Values();

        try {
            if(schema == null) schema = ReflectData.get().getSchema(Class.forName(schemaName));
            DatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
            ByteArrayInputStream in = new ByteArrayInputStream(tuple.getBinary(1));
            Decoder decoder = DecoderFactory.get().directBinaryDecoder(in, null);
            GenericRecord record = reader.read(null, decoder);

            for(String field : fields) {
                Object value = record.get(field);
                // Avro strings are stored using a special Avro type instead of using Java primitives
                if(value instanceof Utf8) {
                    values.add(value.toString());
                } else if(value instanceof Map<?, ?>) {
                    // Due to type erasure, generic type parameter can't be generalized for whole map
                    Map<Object, Object> map = new HashMap<>();
                    for (Map.Entry<Object, Object> entry : ((Map<Object, Object>)value).entrySet()) {
                        Object key = entry.getKey();
                        Object newKey = key instanceof Utf8 ? key.toString() : key;
                        Object val = entry.getValue();
                        Object newVal = val instanceof Utf8 ? val.toString() : val;
                        map.put(newKey, newVal);
                    }
                    values.add(map);
                } else {
                    values.add(value);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        collector.emit(values);
    }
}
