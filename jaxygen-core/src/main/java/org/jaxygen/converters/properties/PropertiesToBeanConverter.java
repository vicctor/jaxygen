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
package org.jaxygen.converters.properties;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.exceptions.WrongProperyIndex;
import org.jaxygen.http.HttpRequestParams;
import org.jaxygen.util.ClassTypeUtil;

/**
 *
 * @author Artur Keska
 */
public class PropertiesToBeanConverter implements RequestConverter {

    static final Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>();

    static {
        converters.put(Boolean.class, new BooleanConverter());
        converters.put(Boolean.TYPE, new BooleanConverter());
        converters.put(Byte.class, new ByteConverter());
        converters.put(Byte.TYPE, new ByteConverter());
        converters.put(Character.class, new CharacterConverter());
        converters.put(Character.TYPE, new CharacterConverter());
        converters.put(Float.class, new FloatConverter());
        converters.put(Float.TYPE, new FloatConverter());
        converters.put(Double.class, new DoubleConverter());
        converters.put(Double.TYPE, new DoubleConverter());
        converters.put(double.class, new DoubleConverter());
        converters.put(Integer.class, new IntegerConverter());
        converters.put(Integer.TYPE, new IntegerConverter());
        converters.put(Long.class, new LongConverter());
        converters.put(Long.TYPE, new LongConverter());
        converters.put(Short.class, new ShortConverter());
        converters.put(Short.TYPE, new ShortConverter());
        converters.put(Enum.class, new EnumConverter());
        converters.put(String.class, new StringConverter());
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        converters.put(Date.class, dateConverter);
        converters.put(BigInteger.class, new BigIntegerConverter());
        converters.put(BigDecimal.class, new BigDecimalConverter());
        for (Class<?> c : converters.keySet()) {
            ConvertUtils.register(converters.get(c), c);
        }
    }

    static public boolean isCovertable(Class<?> c) {
        return converters.containsKey(c);
    }
    public static final String NAME = "PROPERTIES";

    @Override
    public Object deserialise(HttpRequestParams params, Class<?> beanClass) throws DeserialisationError {
        try {
            return convertPropertiesToBean(params.getParameters(), params.getFiles(), beanClass);
        } catch (Exception ex) {
            throw new DeserialisationError("Could not parse input parameters for beed class " + beanClass, ex);
        }
    }

    /**
     * Applies a collection of properties to a JavaBean. Converts String and
     * String[] values to correct property types
     *
     * @param properties A map of the properties to set on the JavaBean
     * @param files List of files.
     * @param beanClass Bean class to be converted.
     * @return A new object of beanClass.
     * @throws InstantiationException .
     * @throws InvocationTargetException .
     * @throws IllegalAccessException .
     * @throws IntrospectionException .
     * @throws IllegalArgumentException .
     * @throws WrongProperyIndex Exception thrown on property validation errors.
     * @throws NoSuchFieldException .
     */
    public static Object convertPropertiesToBean(Map<String, String> properties,
            Map<String, Uploadable> files,
            Class<?> beanClass) throws IllegalArgumentException,
            IntrospectionException, IllegalAccessException,
            InvocationTargetException, InstantiationException, WrongProperyIndex, NoSuchFieldException {
        Object bean = beanClass.newInstance();
        final SortedSet<String> sortedKeys = new TreeSet(properties.keySet());
        final List<String> innerMapKeysToSkip = new ArrayList<>();
        for (final String key : sortedKeys) {
            if (!innerMapKeysToSkip.contains(key)) {
                if (key.contains("<key>")) {
                    bean = deserializeMapField(key, properties, innerMapKeysToSkip, bean, beanClass);
                } else if (key.contains("<value>")) {
                    //this property was handled in '<key>' condition
                } else {
                    final String value = properties.get(key);
                    bean = fillBeanValueByName(key, value, beanClass, bean);
                }
            }
        }

        for (final String key : files.keySet()) {
            final Uploadable value = files.get(key);
            bean = fillBeanValueByName(key, value, beanClass, bean);
        }

        return bean;
    }

    private static Object deserializeMapField(final String key, Map<String, String> properties, final List<String> innerMapKeysToSkip, Object bean, Class<?> beanClass) throws NoSuchFieldException, IntrospectionException, WrongProperyIndex, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException {
        final String keyBase = key.replace("<key>", "");
        final String keykey = key;
        final String keyval = properties.get(keykey);
        final String valkey = key.replace("<key>", "<value>");
        Object valval = properties.get(valkey);
        if (valval == null) { // seems that value is more complex than just string
            int bracketIndex = keyBase.indexOf("[");
            final String fieldName = keyBase.substring(0, bracketIndex);

            Map<String, String> innerProperties = new HashMap<>();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (entry.getKey().startsWith(valkey)) {
                    innerMapKeysToSkip.add(entry.getKey());
                    final String k = entry.getKey().replace(valkey + ".", "");
                    final String v = entry.getValue().replace(valkey + ".", "");
                    innerProperties.put(k, v);
                }
            }
            Class<?>[] keyValueTypes = ClassTypeUtil.retrieveMapTypes(bean.getClass(), fieldName);
            Class<?> valueType = keyValueTypes[1];
            valval = convertPropertiesToBean(innerProperties, new HashMap(), valueType);
        }
        bean = fillBeanValueForMapField(keyBase, keyval, valval, beanClass, bean);
        return bean;
    }

    public static Object convertPropertiesToBean(Map<String, String> properties,
            Map<String, Uploadable> files,
            Object bean) throws IllegalArgumentException,
            IntrospectionException, IllegalAccessException,
            InvocationTargetException, InstantiationException, WrongProperyIndex, NoSuchFieldException {
        Object pojo = bean;
        for (final String key : properties.keySet()) {
            final String value = properties.get(key);
            pojo = fillBeanValueByName(key, value, bean.getClass(), pojo);
        }
        for (final String key : files.keySet()) {
            final Uploadable value = files.get(key);
            pojo = fillBeanValueByName(key, value, bean.getClass(), pojo);
        }
        return bean;
    }

    /**
     * Fill the field in bean by the value pointed by the name. Name format
     * name=(KEY([N])?)+ where KEY bean property name, N index in table (if
     * bean field is List of java array).
     *
     * @param name
     * @param value
     * @param beanClass
     * @param baseBean
     * @return
     * @throws IntrospectionException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws WrongProperyIndex
     * @throws NoSuchFieldException
     */
    public static Object fillBeanValueByName(final String name, Object value,
            Class<?> beanClass, Object baseBean)
            throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, WrongProperyIndex, NoSuchFieldException {
        // parse name x.y[i].z[n].v
        Object bean = baseBean;
        if (bean == null) {
            bean = beanClass.newInstance();
        }
        Class<?> c = beanClass;
        BeanInfo beanInfo = Introspector.getBeanInfo(c, Object.class);
        final String childName = name.substring(name.indexOf(".") + 1);
        String path[] = name.split("\\.");

        final String fieldName = path[0];
        // parse arrays [n]
        if (fieldName.endsWith("]")) {
            int bracketStart = fieldName.indexOf("[");
            int len = fieldName.length();
            if (bracketStart > 0) {
                fillBeanArrayField(name, value, bean, beanInfo, path, fieldName,
                        bracketStart, len);
            } else {
                throw new WrongProperyIndex(name);
            }
        } else {
            // parse non arrays
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                if (pd.getName().equals(fieldName)) {
                    Method writter = pd.getWriteMethod();
                    Method reader = pd.getReadMethod();
                    if (writter != null && reader != null) {
                        Class<?> valueType = reader.getReturnType();
                        if (path.length == 1) {
                            Object valueObject = parsePropertyToValue(value, valueType);
                            writter.invoke(bean, valueObject);
                        } else {
                            Object childBean = reader.invoke(bean);
                            Object valueObject = fillBeanValueByName(childName, value,
                                    valueType, childBean);
                            writter.invoke(bean, valueObject);
                        }
                    }
                }
            }
        }

        // Object bean = c.newInstance();
        return bean;
    }

    private static Object fillBeanValueForMapField(final String keybase,
            Object keyval, Object valval,
            Class<?> beanClass, Object baseBean)
            throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, WrongProperyIndex, NoSuchFieldException {
        // parse name x.y[i].z[n].v
        Object bean = baseBean;
        if (bean == null) {
            bean = beanClass.newInstance();
        }

        final String fieldName = keybase;
        // parse arrays [n]
        if (fieldName.endsWith("]")) {
            int bracketStart = fieldName.indexOf("[");
            int len = fieldName.length();
            if (bracketStart > 0) {
                fillBeanMapField(keyval, valval, bean, beanClass, fieldName,
                        bracketStart, len);
            } else {
                throw new WrongProperyIndex(keybase);
            }
        }
        return bean;
    }

    private static Class<?> retrieveListType(Class<?> paramClass, String propertyName) {
        Class c = paramClass;
        Field listField = null;
        String name = c.getName();
        while (listField == null || "java.lang.Object".equals(name)) {
            try {
                listField = c.getDeclaredField(propertyName);
            } catch (Exception e) {
                c = c.getSuperclass();
            }
        }
        Type genericPropertyType = listField.getGenericType();

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

    private static void fillBeanMapField(Object keyval, Object valval,
            Object bean, Class<?> beanClass, final String fieldName,
            int bracketStart, int len)
            throws IllegalAccessException, InvocationTargetException,
            IntrospectionException, InstantiationException, IllegalArgumentException,
            WrongProperyIndex, NoSuchFieldException {
        final String indexStr = fieldName.substring(bracketStart + 1, len - 1);
        final String propertyName = fieldName.substring(0, bracketStart);
        int index = Integer.parseInt(indexStr);
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (pd.getName().equals(propertyName)) {
                Method writter = pd.getWriteMethod();
                Method reader = pd.getReadMethod();
                if (writter != null && reader != null) {
                    Object object = reader.invoke(bean);
                    if (pd.getPropertyType().isAssignableFrom(HashMap.class)) {
                        if (object == null) {
                            Class childType = pd.getPropertyType().getComponentType();
                            object = childType.newInstance();
                            writter.invoke(bean, object);
                        }
                        Class<?>[] keyValueTypes = ClassTypeUtil.retrieveMapTypes(bean.getClass(), propertyName);
                        Class<?> keyType = keyValueTypes[0];
                        Class<?> valueType = keyValueTypes[1];
                        Map map = (Map) object;
                        while (map.size() < (index + 1)) {
                            Object keyObject = parsePropertyToValue(keyval, keyType);
                            Object valueObject = parsePropertyToValue(valval, valueType);
                            map.put(keyObject, valueObject);
                        }
                    }
                }
            }
        }
    }

    private static void fillBeanArrayField(final String name, Object value,
            Object bean, BeanInfo beanInfo, String[] path, final String fieldName,
            int bracketStart, int len)
            throws IllegalAccessException, InvocationTargetException,
            IntrospectionException, InstantiationException, IllegalArgumentException,
            WrongProperyIndex, NoSuchFieldException {
        final String indexStr = fieldName.substring(bracketStart + 1, len - 1);
        final String propertyName = fieldName.substring(0, bracketStart);
        int index = Integer.parseInt(indexStr);
        String childName = "";
        int firstDot = name.indexOf(".");
        if (firstDot > 0) {
            childName = name.substring(firstDot + 1);
        }

        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if (pd.getName().equals(propertyName)) {
                Method writter = pd.getWriteMethod();
                Method reader = pd.getReadMethod();
                if (writter != null && reader != null) {
                    Object array = reader.invoke(bean);
                    if (pd.getPropertyType().isAssignableFrom(ArrayList.class) || pd.getPropertyType().isAssignableFrom(LinkedList.class) || (List.class).isAssignableFrom(pd.getPropertyType())) { // List
                        if (array == null) {
                            Class childType = pd.getPropertyType().getComponentType();
                            array = childType.newInstance();
                            writter.invoke(bean, array);
                        }
                        Class<?> componentType = retrieveListType(bean.getClass(), propertyName);
                        List list = (List) array;                        
                        while (list.size() < (index + 1)) {
                            try {
                                Object o;
                                if(componentType.isEnum()){
                                    o = parsePropertyToValue(value, componentType);
                                }else{
                                    o = componentType.getConstructor().newInstance();
                                }
                                list.add(o);
                            } catch (NoSuchMethodException | SecurityException ex) {
                                throw new ConversionException(ex);
                            }
                        }
                        if (path.length == 1) {
                            Object valueObject = parsePropertyToValue(value, componentType);
                            list.set(index, valueObject);
                        } else {
                            Object valueObject = fillBeanValueByName(childName, value, componentType, list.get(index));
                            list.set(index, valueObject);
                        }
                    } else if (pd.getPropertyType().isArray()) {
                        if (array == null) {
                            array = Array.newInstance(
                                    pd.getPropertyType().getComponentType(), index + 1);
                            writter.invoke(bean, array);
                        }
                        if (Array.getLength(array) < (index + 1)) {
                            array = resizeArray(array, index + 1);
                            writter.invoke(bean, array);
                        }
                        if (path.length == 1) {
                            Object valueObject = parsePropertyToValue(value, array.getClass().getComponentType());
                            Array.set(array, index, valueObject);
                        } else {
                            Object valueObject = fillBeanValueByName(childName, value, array.getClass().getComponentType(), Array.get(array, index));
                            Array.set(array, index, valueObject);
                        }
                    } else if (pd.getPropertyType().equals(List.class)) {
                        if (array == null) {
                            array = pd.getPropertyType().newInstance();
                            writter.invoke(bean, array);
                        }
                        Class<?> genericClass = array.getClass().getTypeParameters()[0].getClass();
                        if (path.length == 1) {
                            Object valueObject = parsePropertyToValue(value, genericClass);
                            Array.set(array, index, valueObject);
                        } else {
                            Object valueObject = fillBeanValueByName(childName, value,
                                    genericClass, null);
                            Array.set(array, index, valueObject);
                        }
                    }
                }
            }
        }
    }

    private static Object parsePropertyToValue(Object valueObject,
            Class<?> propertyType) {
        Object value = null;

        //TODO: add cache of enum converters
        boolean isEnum = propertyType.isEnum();
        if (isEnum) {
            ConvertUtils.register(new EnumConverter(), propertyType);
        }

        if (valueObject != null && valueObject.getClass().equals(String.class)) {
            value = ConvertUtils.convert((String) valueObject, propertyType);
        } else {
            value = valueObject;
        }

        return value;
    }

    private static Object resizeArray(Object array, int size) {
        Object newArray = Array.newInstance(array.getClass().getComponentType(),
                size);
        for (int i = 0; i < Array.getLength(array); i++) {
            Object value = Array.get(array, i);
            Array.set(newArray, i, value);
        }
        return newArray;
    }

    public String getName() {
        return NAME;
    }
}
