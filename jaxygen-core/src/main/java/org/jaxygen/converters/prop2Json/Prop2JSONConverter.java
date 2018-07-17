/*
 * Copyright 2012 Artur Keska.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.converters.prop2Json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.converters.json.JSONBuilderRegistry;
import org.jaxygen.converters.properties.PropertiesToBeanConverter;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.http.HttpRequestParams;

/**
 *
 * @author jknast
 */
public class Prop2JSONConverter implements RequestConverter {

    private static Gson gson = JSONBuilderRegistry.getBuilder().build();
    public static final String NAME = "PROP2JSON";

    @Override
    public Object deserialise(HttpRequestParams params, Class<?> beanClass) throws DeserialisationError {
        try {
            String json = convertPropertiesToJSON(params.getParameters());
            Object pojo = convertJSONToPojo(json, beanClass);
            
            for (final String key : params.getFiles().keySet()) {
                final Uploadable value = params.getFiles().get(key);
                pojo = PropertiesToBeanConverter.fillBeanValueByName(key, value, beanClass, pojo);
            }
            return pojo;
        } catch (Exception ex) {
            throw new DeserialisationError("Could not parse input parameters for class " + beanClass, ex);
        }
    }

    public static Object convertJSONToPojo(String json, Class<?> beanClass) {
        Object ret = gson.fromJson(json, beanClass);
        return ret;
    }

    public static String convertPropertiesToJSON(Map<String, String> properties) {
        JsonObject root = new JsonObject();
        return addPropertiesToObject(root, properties);
    }

    private static String addPropertiesToObject(JsonObject root, Map<String, String> properties) {

        Map<String, String> primitives = new HashMap();
        Map<String, String> complex = new HashMap();
        for (Map.Entry<String, String> e : properties.entrySet()) {
            if (e.getKey().contains(".")) {
                complex.put(e.getKey(), e.getValue());
            } else {
                primitives.put(e.getKey(), e.getValue());
            }
        }
        //add primitives if not empty
        for (Map.Entry<String, String> e : primitives.entrySet()) {
            if (!e.getValue().isEmpty()) {
                root.addProperty(e.getKey(), e.getValue());
            }
        }

        //sort complex to buckets
        Map<String, Map<String, String>> bucket = sortToBucket(complex);

        // add all complex fields family
        for (Map.Entry<String, Map<String, String>> e : bucket.entrySet()) {
            addComplex(root, e.getKey(), e.getValue());
        }

        return root.toString();
    }

    private static Map<String, Map<String, String>> sortToBucket(Map<String, String> complex) {
        Map<String, Map<String, String>> bucket = new HashMap();

        for (Map.Entry<String, String> e : complex.entrySet()) {
            String nextKey = e.getKey();
            String splitter;
            if (nextKey.contains("<impl>")) {
                int dotIndex = nextKey.indexOf(".");
                int implIndex = nextKey.indexOf("<impl>");
                if (dotIndex > 0 && dotIndex < implIndex) {
                    splitter = "\\.";
                } else {
                    splitter = "#";
                }
            } else {
                splitter = "\\.";
            }
            String[] split = nextKey.split(splitter, 2);
            String fieldName = split[0];
            String innerName = split[1];
            Map<String, String> fieldMap = bucket.get(fieldName);
            if (fieldMap == null) {
                fieldMap = new HashMap();
                bucket.put(fieldName, fieldMap);
            }

            fieldMap.put(innerName, e.getValue());
        }
        return bucket;
    }

    private static void addComplex(JsonObject root, String fieldName, Map<String, String> properties) {
        if (fieldName.contains("[")) {
            addArr(root, fieldName, properties);
        } else {
            addObj(root, fieldName, properties);
        }
    }

    private static void addArr(JsonObject root, String fieldNameBracket, Map<String, String> properties) {

        String[] splited = fieldNameBracket.split("\\[");
        String arrayName = splited[0];
        if (!root.has(arrayName)) {
            root.add(arrayName, new JsonArray());
        }
        JsonArray jsonArr = root.getAsJsonArray(arrayName);
        if (fieldNameBracket.contains("<impl>")) {
            String implClassName = fieldNameBracket.substring(fieldNameBracket.indexOf("<impl>") + 6);
            JsonObject wrapper = new JsonObject();
            JsonObject dtoObj = new JsonObject();
            wrapper.addProperty("implementationClass", implClassName);
            wrapper.add("dto", dtoObj);
            jsonArr.add(wrapper);
            addPropertiesToObject(dtoObj, properties);
        } else {
            JsonObject obj = new JsonObject();
            jsonArr.add(obj);
            addPropertiesToObject(obj, properties);
        }
    }

    private static void addObj(JsonObject root, String fieldName, Map<String, String> properties) {

        if (fieldName.contains("<impl>")) {
            String[] splited = fieldName.split("<impl>");
            String fName = splited[0];
            String implClassName = fieldName.substring(fieldName.indexOf("<impl>") + 6);
            JsonObject wrapper = new JsonObject();
            JsonObject dtoObj = new JsonObject();
            wrapper.addProperty("implementationClass", implClassName);
            wrapper.add("dto", dtoObj);
            root.add(fName, wrapper);
            addPropertiesToObject(dtoObj, properties);
        } else {
            JsonObject obj = new JsonObject();
            root.add(fieldName, obj);
            addPropertiesToObject(obj, properties);
        }
    }

    public Prop2JSONConverter() {
    }

    public String getName() {
        return NAME;
    }
}
