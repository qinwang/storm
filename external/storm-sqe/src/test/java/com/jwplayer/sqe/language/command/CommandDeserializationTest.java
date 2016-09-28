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

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CommandDeserializationTest {
    List<BaseCommand> commands;

    @Before
    public void setup() throws IOException {
        String json = IOUtils.toString(this.getClass().getResourceAsStream("/queries/command-deserialization.json"));
        commands = BaseCommand.load(json);
    }

    @Test
    public void testLoadCommand() {
        assertEquals(commands.size(), 4);
    }

    @Test
    public void testSetCommand() {
        assertEquals(commands.get(0).getCommandType(), CommandType.Set);

        SetCommand setCommand = (SetCommand) commands.get(0);

        assertEquals(setCommand.key, "jw.sqe.state.redis.host");
        assertEquals(setCommand.value, "localhost");
    }

    @Test
    public void testQueryCommand() {
        assertEquals(commands.get(2).getCommandType(), CommandType.Query);

        Query query = (Query) commands.get(2);

        assertNotNull(query.insertInto);
        assertEquals(query.insertInto.objectName, "TestComplexQuery");
        assertNotNull(query.select);
        assertEquals(query.select.expressions.size(), 4);
        assertNotNull(query.from.objectName, "big.query.data");
        assertNotNull(query.where);
    }
}
