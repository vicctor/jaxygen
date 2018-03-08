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
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Modifier;
import org.jaxygen.annotations.HasImplementation;

/**
 *
 * @author xnet
 */
public class CustomTypeAdapterFactory implements TypeAdapterFactory {

    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAnnotationPresent(HasImplementation.class)) {
            return (TypeAdapter<T>) chooseTypeAdapter(gson, type);
        } else {
            return null;
        }
    }

    private TypeAdapter<?> chooseTypeAdapter(Gson gson, TypeToken type) {
        if (type.getRawType().isInterface() || Modifier.isAbstract(type.getRawType().getModifiers())) {
            return new CustomImplementationTypeAdapter(gson);
        } else {
            return gson.getDelegateAdapter(this, type);
        }
    }
}
