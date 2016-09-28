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
package com.jwplayer.sqe.util;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;


public class FileHelperTest {
    @Test
    public void testRelativeURI() throws IOException, URISyntaxException {
        URI uri = new URI("conf/sample-conf.yaml");
        String text = FileHelper.loadFileAsString(uri);

        assertNotNull(text);
        assertNotSame(text, "");
    }

    @Test
    public void testFileURI() throws IOException, URISyntaxException {
        URI currentPath = Paths.get("").toAbsolutePath().toUri();
        URI uri = new URI(currentPath + "conf/sample-conf.yaml");
        String text = FileHelper.loadFileAsString(uri);

        assertNotNull(text);
        assertNotSame(text, "");
    }

    @Test
    public void testResourceURI() throws IOException, URISyntaxException {
        URI uri = new URI("resource:///sample-resource.txt");
        String text = FileHelper.loadFileAsString(uri);

        assertNotNull(text);
        assertNotSame(text, "");
    }
}
