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

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YamlConfig {
    public static final String CONFIG_PATH = "conf/sample-conf.yaml";

    public static Yaml getJWConfig(String configPath) throws IOException, URISyntaxException {
        if (configPath == null) {
            configPath = CONFIG_PATH;
        }

        Yaml config = new Yaml();
        config.load(FileHelper.getInputStream(new URI(configPath)));
        return config;
    }

    public static Yaml getJWConfig() throws IOException, URISyntaxException {
        return getJWConfig(null);
    }

    public static Map<String, Object> getJWConfigAsMap(String configPath) throws IOException, URISyntaxException {
        if (configPath == null) {
            configPath = CONFIG_PATH;
        }

        Yaml config = new Yaml();
        return (HashMap<String, Object>) config.load(FileHelper.getInputStream(new URI(configPath)));
    }

    public static Map<String, Object> getJWConfigAsMap() throws IOException, URISyntaxException {
        return getJWConfigAsMap(null);
    }

    public static InnerConfig getConfig(String configPath) {
        return new InnerConfig(configPath);
    }

    public static InnerConfig getConfig() {
        return new InnerConfig(CONFIG_PATH);
    }


    public static class InnerConfig {

        Map<String, Object> config;

        public InnerConfig(String path) {
            try {
                config = YamlConfig.getJWConfigAsMap(path);
            } catch (IOException|URISyntaxException e) {
                throw new RuntimeException("No dice!", e);
            }
        }

        public InnerConfig(Map<String, Object> config) {
            this.config = config;
        }

        public Object get(String key, Object defaultValue) {
            try {
                if (config.get(key) == null)
                    return defaultValue;
                return config.get(key);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        public String getString(String key, String defaultValue){
            try {
                if (config.get(key) == null)
                    return defaultValue;
                return (String) config.get(key);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        public Map<String, Object> getMap(String key, Map<String, Object> defaultValue){
            try {
                if (config.get(key) == null) {
                    return defaultValue;
                }
                return (Map<String, Object>) config.get(key);
            } catch (Exception ex) {
                return defaultValue;
            }
        }

        public boolean getBoolean(String key, boolean defaultValue) {
            try {
                if (config.get(key) == null) {
                    return defaultValue;
                }
                return (Boolean) config.get(key);
            } catch (Exception ex) {
                return defaultValue;
            }
        }

        public InnerConfig getInnerConfig(String key, Map<String, Object> defaultValue) {
            return new InnerConfig(getMap(key, defaultValue));
        }

        public int getInteger(String key, int defaultValue) {
            try {
                if (config.get(key) == null) {
                    return defaultValue;
                }
                return (Integer)config.get(key);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        public double getDouble(String key, double defaultValue) {
            try {
                if (config.get(key) == null) {
                    return defaultValue;
                }
                return (Double) config.get(key);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        public List getList(String key, List defaultValue){
            try {
                if (config.get(key) == null) {
                    return defaultValue;
                }
                return (List) config.get(key);
            } catch (Exception e) {
                return defaultValue;
            }
        }

    }
}
