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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileHelper {
    public static InputStream getInputStream(URI uri) throws IOException, URISyntaxException {
        URI fsURI;
        // This is a bit hacky because of weird behavior getting the file system.
        // We also want relative URIs to resolve to the default/local file system.
        if (uri.isAbsolute()) {
            fsURI = new URI(uri.getScheme(), uri.getAuthority() == null ? "" : uri.getAuthority(), "/", null, null);
        } else {
            fsURI = new URI("file", uri.getAuthority() == null ? "" : uri.getAuthority(), "/", null, null);
        }
        FileSystem fs = FileSystems.getFileSystem(fsURI);
        Path path;

        // Windows paths can have a preceding slash that breaks this, even if Java gave it to us! :D
        if (
                System.getProperty("os.name").contains("indow")
                        && uri.getPath().substring(0, 1).equals("/")
                        && fsURI.getScheme().equals("file")) {
            path = fs.getPath(uri.getPath().substring(1));
        } else {
            path = fs.getPath(uri.getPath());
        }

        return Files.newInputStream(path);
    }

    public static String loadFileAsString(URI uri) throws IOException, URISyntaxException {
        return IOUtils.toString(getInputStream(uri));
    }
}
