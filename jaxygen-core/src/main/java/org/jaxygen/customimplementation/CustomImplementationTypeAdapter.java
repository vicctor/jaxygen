/*
 * Copyright 2017 xnet.
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
package org.jaxygen.customimplementation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.jaxygen.exceptions.NoDefinitionOfImplementationException;

/**
 *
 * @author xnet
 */
public class CustomImplementationTypeAdapter extends TypeAdapter<Object> {

    private static final String IMPLEMENTATION_CLASS = "implementationClass";
    private static final String DTO = "dto";

    final private TypeAdapter<JsonElement> elementAdapter;
    final private Gson gson;

    public CustomImplementationTypeAdapter(Gson gson) {
        this.gson = gson;
        this.elementAdapter = gson.getAdapter(JsonElement.class);
    }

    //serialize function
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        TypeAdapter adapter = gson.getAdapter(value.getClass());
        JsonElement ret = adapter.toJsonTree(value);
        elementAdapter.write(out, ret);
    }

    //deserialize function
    @Override
    public Object read(JsonReader in) throws IOException {
        JsonElement element = elementAdapter.read(in);
        final JsonObject convertable = element.getAsJsonObject();
        final JsonPrimitive jsonClassName = (JsonPrimitive) convertable.get(IMPLEMENTATION_CLASS);
        if (jsonClassName == null) {
            throw new NoDefinitionOfImplementationException("There is no definition of class that will implement interface with annotation 'HasImplementation'");
        }
        final JsonObject jsonDto = convertable.getAsJsonObject(DTO);

        final String implClassName = jsonClassName.getAsString();
        final Class implClass = getClassInstance(implClassName);
        TypeAdapter adapter = gson.getAdapter(implClass);
        Object ret = adapter.fromJsonTree(jsonDto);
        return ret;
    }

    public Class getClassInstance(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException(cnfe.getMessage());
        }
    }

}
