/*
 * Copyright 2017 Artur.
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
package org.jaxygen.jaxygen.apigin.beaninspector;

import com.google.common.collect.Lists;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.jodah.typetools.TypeResolver;
import org.jaxygen.jaxygen.apigin.beaninspector.annotations.GenericType;
import org.jaxygen.jaxygen.apigin.beaninspector.exceptions.InspectionError;
import org.jaxygen.jaxygen.apigin.beaninspector.model.ArrayField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.BooleanField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.EnumField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.FieldDescriptor;
import org.jaxygen.jaxygen.apigin.beaninspector.model.IntegerField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.InvalidFieldDescriptor;
import org.jaxygen.jaxygen.apigin.beaninspector.model.ObjectDescriptor;
import org.jaxygen.jaxygen.apigin.beaninspector.model.StringField;

/**
 *
 * @author Artur
 */
public class BeanInspector {

    private static final Map<Class, FieldDescriptorBuilder> BUILDERS = new HashMap<>();

   
    interface FieldDescriptorBuilder {

        FieldDescriptor build();
    };

    static {
        BUILDERS.put(String.class,  () -> new StringField());
        BUILDERS.put(Integer.TYPE,  () -> new IntegerField());
        BUILDERS.put(Integer.class, () -> new IntegerField());
        BUILDERS.put(Boolean.TYPE,  () -> new BooleanField());
        BUILDERS.put(Boolean.class, () -> new BooleanField());
    }

    /** Inspect bean class and create a composed model of the class fields.
     * 
     * @param clazz bean class for analysis
     * @return composed object model containing description of the clazz type.
     * @throws InspectionError 
     */
    public static FieldDescriptor inspect(final Class clazz) throws InspectionError {
        FieldDescriptor rc;
        // Decide where this class is a simple type or a complex object
        if (isPrimitive(clazz)) {
            FieldDescriptorBuilder builder = BUILDERS.get(clazz);
            rc = builder.build();
        } else {
            rc = inspectObject(clazz);
        }
        return rc;
    }

    /** Verify if the given class type is primitive
     * 
     * @param clazz
     * @return 
     */
    private static boolean isPrimitive(final Class clazz) {
        return BUILDERS.containsKey(clazz);
    }

    /** Inspect complex class type
     * 
     * @param clazz
     * @return
     * @throws InspectionError 
     */
    public static ObjectDescriptor inspectObject(final Class clazz) throws InspectionError {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

        try {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
                JXPropertyDescriptor pd = describeProperty(clazz, descriptor);
                FieldDescriptor fd = buildDescritptor(pd);
                if (fd != null) {
                    fieldDescriptors.add(fd);
                }
            }
        } catch (IntrospectionException ex) {
            throw new InspectionError(ex);
        }

        ObjectDescriptor rc = new ObjectDescriptor();
        rc.setClassName(clazz.getSimpleName());
        rc.setName(clazz.getSimpleName());
        rc.setClassFullName(clazz.getCanonicalName());
        rc.setFields(fieldDescriptors);

        return rc;
    }

    private static Field getClassFieldFromParentClasses(Class clazz, final String fieldName) {
        Class parent = clazz;
        Field listField = null;
        //String name = parent.getName();
        while (parent != null && parent != Object.class && listField == null) {
            try {
                listField = parent.getDeclaredField(fieldName);
            } catch (Exception e) {
                parent = parent.getSuperclass();
            }
        }
        return listField;
    }

    /**
     * Obtain composed information about the type of the property.
     *
     * @param clazz class of the property type
     * @param pd property descriptor
     * @return composed property information
     */
    private static JXPropertyDescriptor describeProperty(Class clazz, PropertyDescriptor pd) {
        JXPropertyDescriptor rc = new JXPropertyDescriptor();
        Field field = getClassFieldFromParentClasses(clazz, pd.getName());
        rc.setWrittable(pd.getWriteMethod() != null);
        rc.setReadable(rc.isReadable());
        rc.setPropertyName(pd.getName());
        rc.setType(pd.getPropertyType());
        rc.setField(field);
        List<Annotation> annotatins = new ArrayList<>();
        if (pd.getWriteMethod() != null) {
            annotatins.addAll(Lists.newArrayList(pd.getWriteMethod().getAnnotations()));
        }
        if (pd.getReadMethod() != null) {
            annotatins.addAll(Lists.newArrayList(pd.getReadMethod().getAnnotations()));
        }
        try {
            if (clazz.getDeclaredField(pd.getName()) != null) {
                annotatins.addAll(Lists.newArrayList(pd.getReadMethod().getAnnotations()));
            }
        } catch (NoSuchFieldException ex) {
            // ignore - has no meaning
        }
        rc.setAnnotations(annotatins);
        return rc;
    }

    /**
     * Verify if the given type should be ignored and not reported in the bean
     * model descriptor.
     */
    private static boolean isIgnorable(Class clazz) {
        return Class.class.equals(clazz);
    }

    private static FieldDescriptor createFieldDescrioptor(JXPropertyDescriptor descriptor) throws InspectionError {
        final Class clazz = descriptor.getType();
        FieldDescriptorBuilder builder = BUILDERS.get(clazz);
        FieldDescriptor rc = new InvalidFieldDescriptor();
        if (builder != null) {
            rc = builder.build();
        } else if (clazz.isArray()) {
            Class type = clazz.getComponentType();
            FieldDescriptor contentDescriptor = inspect(type);
            rc = new ArrayField(contentDescriptor);
        } else if (clazz.isAssignableFrom(List.class)) {
            rc = resolveListType(descriptor);
        } else if (clazz.isEnum()) {
            rc = buildEnumDescriptor(clazz);
        } else if (!isIgnorable(clazz)) {
            rc = inspect(clazz);
        }
        return rc;
    }

    private static Class<?> resolveListTypeFromField(Field listField) {
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

    private static FieldDescriptor resolveListType(JXPropertyDescriptor descriptor) throws InspectionError {
        Class clazz = descriptor.getType();
        FieldDescriptor rc;
        Class type = null;

        // lets try to find out the type follow the annotation
        GenericType annotation = descriptor.getAnnotation(GenericType.class);
        if (annotation != null) {
            type = annotation.type();
        }
        // Try to get type from type resolver. This requres that the class is instantiated
        if (type == null) {
            type = TypeResolver.resolveRawArgument(clazz, clazz);
        }
        // Try to resolve generic type from instance of the class
        if (type.equals(TypeResolver.Unknown.class)) {
            type = resolveListTypeFromField(descriptor.getField());
        }
        FieldDescriptor contentDescriptor = inspect(type);
        rc = new ArrayField(contentDescriptor);
        return rc;
    }
    
     private static FieldDescriptor buildEnumDescriptor(Class clazz) {
        EnumField rc = new EnumField();
        List<String> list = Lists.newArrayList(clazz.getEnumConstants()).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        rc.setValues(list);
        return rc;
    }


    private static FieldDescriptor buildDescritptor(JXPropertyDescriptor descriptor) throws InspectionError {
        FieldDescriptor rc = null;
        if (descriptor.isWrittable() || descriptor.isReadable()) {
            rc = createFieldDescrioptor(descriptor);
            rc.setName(descriptor.getPropertyName());
            rc.setReadable(descriptor.isReadable());
            rc.setWritable(descriptor.isWrittable());
        }
        return rc;
    }
}
