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
package org.jaxygen.apibrowser.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author jknast
 */
public class ClassTypeUtil {

    private Type obtainGenericPropertyType(final Class<?> paramClass, String propertyName) {
        Class c = paramClass;
        Field listField = null;
        final String name = c.getName();
        while (listField == null || "java.lang.Object".equals(name)) {
            try {
                listField = c.getDeclaredField(propertyName);
            } catch (Exception e) {
                c = c.getSuperclass();
            }
        }
        final Type genericPropertyType = listField.getGenericType();
        return genericPropertyType;
    }

    public Class<?> retrieveListType(Class<?> paramClass, String propertyName) {

        Type genericPropertyType = obtainGenericPropertyType(paramClass, propertyName);
        ParameterizedType propertyType = null;
        while (propertyType == null) {
            if ((genericPropertyType instanceof ParameterizedType)) {
                propertyType = (ParameterizedType) genericPropertyType;
            } else {
                genericPropertyType = ((Class<?>) genericPropertyType).getGenericSuperclass();
            }
        }
        return (Class<?>) propertyType.getActualTypeArguments()[0];
    }

    public Class<?>[] retrieveMapTypes(Class<?> paramClass, String propertyName) {

        Type genericPropertyType = obtainGenericPropertyType(paramClass, propertyName);
        ParameterizedType propertyType = null;
        while (propertyType == null) {
            if ((genericPropertyType instanceof ParameterizedType)) {
                propertyType = (ParameterizedType) genericPropertyType;
            } else {
                genericPropertyType = ((Class<?>) genericPropertyType).getGenericSuperclass();
            }
        }
        Class<?>[] ret = new Class<?>[2];
        ret[0] = (Class<?>) propertyType.getActualTypeArguments()[0];
        ret[1] = (Class<?>) propertyType.getActualTypeArguments()[1];
        return ret;
    }

    public boolean isEnumType(Class clazz) {
        return clazz.isEnum();
    }

    public boolean isBoolType(Class clazz) {
        return Boolean.class.equals(clazz) || (clazz != null && clazz.isPrimitive() && "boolean".equals(clazz.getName()));
    }

    public boolean isArrayType(Class clazz) {
        return clazz.isArray();
    }
}
