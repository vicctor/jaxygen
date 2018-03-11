/*
 * Copyright 2017 jaxygen.org.
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
package org.jaxygen.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jaxygen.exceptions.InstantiateClassException;

/**
 *
 * @author jknast
 */
public class ClassTypeUtil {

    public static  boolean isSimpleResultType(final Class<?> returnType) {
        return returnType.isPrimitive() || returnType.equals(Integer.class) || returnType.equals(Double.class) || returnType.equals(Float.class) || returnType.equals(String.class) || returnType.equals(double.class) || returnType.equals(float.class);
    }
    
    public static List<Field> getFields(final Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class c = clazz;
        while (c != Object.class) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return fields;
    }

    private static Type obtainGenericPropertyType(final Class<?> paramClass, String propertyName) {
        Class c = paramClass;
        Field listField = null;
        while (listField == null && !c.equals(Object.class)) {
            try {
                listField = c.getDeclaredField(propertyName);
            } catch (Exception e) {
                c = c.getSuperclass();
            }
        }
        if (listField == null) {
            throw new InstantiateClassException("Cannot obtain type for field: " + propertyName + ", from class " + paramClass.getCanonicalName());
        }
        final Type genericPropertyType = listField.getGenericType();
        return genericPropertyType;
    }

    public static Class<?> retrieveListType(Class<?> paramClass, String propertyName) {

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

    public static Class<?>[] retrieveMapTypes(Class<?> paramClass, String propertyName) {

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

    public static boolean isEnumType(Class clazz) {
        return clazz.isEnum();
    }

    public static boolean isBoolType(Class clazz) {
        return Boolean.class.equals(clazz) || (clazz != null && clazz.isPrimitive() && "boolean".equals(clazz.getName()));
    }

    public static boolean isArrayType(Class clazz) {
        return clazz.isArray();
    }
}
