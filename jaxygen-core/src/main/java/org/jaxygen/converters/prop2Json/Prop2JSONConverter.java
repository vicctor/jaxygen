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
import java.util.Iterator;
import java.util.Map;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.converters.json.JSONBuilderRegistry;
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
            String json = convertPropertiesToJSON(params.getParameters(), params.getFiles());
            return convertJSONToPojo(json, beanClass);
        } catch (Exception ex) {
            throw new DeserialisationError("Could not parse input parameters for class " + beanClass, ex);
        }
    }

    public static Object convertJSONToPojo(String json, Class<?> beanClass) {

        Object ret = gson.fromJson(json, beanClass);
        return ret;
    }

    public static String convertPropertiesToJSON(Map<String, String> properties, Map<String, Uploadable> files) {
        return convertPropertiesToJSON(properties);
    }

    private static String convertPropertiesToJSON(Map<String, String> properties) {
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
        //add primitives
        for (Map.Entry<String, String> e : primitives.entrySet()) {
            root.addProperty(e.getKey(), e.getValue());
        }

        //sort complex to buckets
        Iterator<String> iterator = complex.keySet().iterator();
        String nextKey = null;
        if (iterator.hasNext()) {
            nextKey = iterator.next();
        }
        if (nextKey != null) {
            String[] split = nextKey.split("\\.", 2);
            String fieldName = split[0];
            Map<String, String> map = new HashMap();
            Iterator<Map.Entry<String, String>> iter = complex.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (entry.getKey().startsWith(fieldName)) {
                    String innerName = entry.getKey().substring(fieldName.length() + 1);
                    map.put(innerName, entry.getValue());
                    iter.remove();
                }
            }
            addComplex(root, fieldName, map);
        }


        return root.toString();
    }
    
    private static void addComplex(JsonObject root, String fieldName, Map<String, String> properties) {
         if (fieldName.contains("[")) {
             addArr(root, fieldName, properties);
            }
    }


    private static void addArr(JsonObject root, String fieldNameBracket, Map<String, String> properties) {

        String[] splited = fieldNameBracket.split("\\[");
        String arrayName = splited[0];
        if (!root.has(arrayName)) {
            root.add(arrayName, new JsonArray());
        }
        JsonArray jsonArr = root.getAsJsonArray(arrayName);        
        JsonObject obj = new JsonObject();
        jsonArr.add(obj);
        addPropertiesToObject(obj, properties);
    }

    public Prop2JSONConverter() {
    }

    public String getName() {
        return NAME;
    }
}
