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
package com.jwplayer.sqe.language.expression.transform;

import org.apache.storm.trident.Stream;


public class ExpandKeys extends TransformExpression {
    public ExpandKeys() {
    }

    @Override
    public Stream transform(Stream stream) {
        return stream.each(getInputFields(), new com.jwplayer.sqe.trident.function.ExpandKeys(), getOutputFields());
    }

    @Override
    public String getFunctionName() {
        return "expandkeys";
    }
}
