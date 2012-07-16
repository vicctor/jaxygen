package pl.devservices.netservice.converters;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringConverter;

import pl.devservices.netservice.annotations.BeanTransient;
import pl.devservices.netservice.annotations.NumberPropertyValidator;
import pl.devservices.netservice.annotations.StringPropertyValidator;
import pl.devservices.netservice.annotations.Validable;
import pl.devservices.netservice.exceptions.InvalidPropertyFormat;
import pl.devservices.netservice.exceptions.MissingArgumentException;
import pl.devservices.netservice.exceptions.WrongProperyIndex;

public class BeanUtil {

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
        converters.put(Integer.class, new IntegerConverter());
        converters.put(Integer.TYPE, new IntegerConverter());
        converters.put(Long.class, new LongConverter());
        converters.put(Long.TYPE, new LongConverter());
        converters.put(Short.class, new ShortConverter());
        converters.put(Short.TYPE, new ShortConverter());
        converters.put(Enum.class, new EnumConverter());
        converters.put(String.class, new StringConverter());
        for (Class<?> c : converters.keySet()) {
            ConvertUtils.register(converters.get(c), c);
        }

    }

    static public boolean isCovertable(Class<?> c) {
        return converters.containsKey(c);
    }

    /**
     * Copy bean content from one bean to another. The bean is copied using the
     * set and get methods. Method invokes all getXX methods on the from object,
     * and result pass to the corresponding setXX methods in the to object. Note
     * that it is not obvious that both object are there same type, only set and
     * get methods must much.
     *
     * @param from
     *          data provider object
     * @param to
     *          data acceptor object.
     */
    public static void translateBean(Object from, Object to) {
        {
            Class<?> cClass = to.getClass();
            Method[] cMethods = cClass.getMethods();
            ArrayList<String> cFields = new ArrayList<String>();
            ArrayList<Class<?>[]> cTypes = new ArrayList<Class<?>[]>();
            for (int i = 0; i < cMethods.length; i++) {
                if (!cMethods[i].isAnnotationPresent(BeanTransient.class)) {
                    String mName = cMethods[i].getName();
                    if (mName.substring(0, 3).equals("set")) {
                        cFields.add(mName.substring(3));
                        cTypes.add(cMethods[i].getParameterTypes());
                    }
                }
            }
            for (int i = 0; i < cFields.size(); i++) {
                try {
                    String name = cFields.get(i);
                    String getterName = "get" + name; // firstChar+name.substring(1);
                    String setterName = "set" + name; // firstChar+name.substring(1);
                    Class<?>[] params = cTypes.get(i);
                    Class<?>[] getterParams = null;
                    Object[] getterValues = null;
                    Method getter = from.getClass().getMethod(getterName, getterParams);
                    if (!getter.isAnnotationPresent(BeanTransient.class)) {
                        Method setter = cClass.getMethod(setterName, params);
                        if (!setter.isAnnotationPresent(BeanTransient.class)) {
                            Object args[] = {getter.invoke(from, getterValues)};
                            setter.invoke(to, args);
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
    }


    /**
     * Applies a collection of properties to a JavaBean. Converts String and
     * String[] values to correct property types
     *
     * @param properties
     *          A map of the properties to set on the JavaBean
     * @param bean
     *          The JavaBean to set the properties on
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     *           could not initialize instance of bean object.
     * @throws WrongProperyIndex
     * @throws MissingArgumentException
     *           One or more bean properties (annotated with the MandatoryProperty
     *           annotation has not been found in the properties map.
     */
    public static Object convertPropertiesToBean(Map<String, Object> properties,
            Class<?> beanClass) throws IllegalArgumentException,
            IntrospectionException, IllegalAccessException,
            InvocationTargetException, InstantiationException, WrongProperyIndex {

        Map<String, PropertyDescriptor> conversionReport = new HashMap<String, PropertyDescriptor>();

        Object bean = beanClass.newInstance();
        for (final String key : properties.keySet()) {
            final Object value = properties.get(key);
            bean = fillBeanValueByName(key, value, beanClass, bean, conversionReport);
        }

        return bean;
    }

    /**
     * Method goes through bean getters, and check if the returned value matches
     * the validator passed in @StringPropertyValidator or @NumberPropertyValidator
     * Method check all methods which returns primitive type, Long or Integer or a
     * bean class annotated but the @Validable annotation type and does not takes
     * any argument.
     *
     * @param bean
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvalidPropertyFormat
     */
    public static void validateBean(Object bean) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, InvalidPropertyFormat {

        Method[] getters = bean.getClass().getMethods();
        for (Method m : getters) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
                    && m.getReturnType() != null) {
                if ((m.getReturnType().isPrimitive() || m.getReturnType().equals(Long.class) || m.getReturnType().equals(Integer.class) || m.getReturnType().equals(String.class))) {
                    StringPropertyValidator sp = m.getAnnotation(StringPropertyValidator.class);
                    NumberPropertyValidator np = m.getAnnotation(NumberPropertyValidator.class);
                    Object rc = m.invoke(bean);
                    String propertyName = m.getName().substring(3);
                    if (sp != null) {
                        if (rc == null) {
                            throw new InvalidPropertyFormat("Property " + propertyName + " must be set");
                        }
                        String v = rc.toString();
                        if (sp.maximalLength() < v.length()) {
                            throw new InvalidPropertyFormat("Property " + propertyName
                                    + " is to long. Maximal length is " + sp.maximalLength());
                        }
                        if (sp.minimalLength() > v.length()) {
                            throw new InvalidPropertyFormat("Property " + propertyName
                                    + " is to short. Minimal length is " + sp.minimalLength());
                        }
                        if (sp.regex().length() > 0) {
                            if (Pattern.matches(sp.regex(), v) == false) {
                                throw new InvalidPropertyFormat("Property " + propertyName
                                        + " not match regular expression:" + sp.regex());
                            }
                        }
                    }
                    if (np != null) {
                        Long v = Long.decode(rc.toString());
                        if (v > np.maxValue()) {
                            throw new InvalidPropertyFormat("Property " + propertyName
                                    + " value is to big. Maximal value is " + np.maxValue());
                        }
                        if (v < np.minValue()) {
                            throw new InvalidPropertyFormat("Property " + propertyName
                                    + " value is to low. Minimal value is " + np.minValue());
                        }
                    }
                }
            } else {
                if (m.getReturnType().isAnnotationPresent(Validable.class)) {
                    Object rc = m.invoke(bean);
                    if (rc != null) {
                        validateBean(rc);
                    }
                }
            }
        }

    }

    /**
     * Fill the field in bean by the value pointed by the name. Name format
     * name=<(KEY([N])?)+> where KEY bean property name, N index in table (if bean
     * field is List of java array).
     *
     * @param name
     * @param value
     * @param beanClass
     * @param baseBean
     * @param conversionReport
     * @return
     * @throws IntrospectionException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws WrongProperyIndex
     */
    private static Object fillBeanValueByName(final String name, Object value,
            Class<?> beanClass, Object baseBean,
            Map<String, PropertyDescriptor> conversionReport)
            throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, WrongProperyIndex {
        // parse name x.y[i].z[n].v
        Object bean = baseBean;
        if (bean == null) {
            bean = beanClass.newInstance();
        }
        Class<?> c = beanClass;
        BeanInfo beanInfo = Introspector.getBeanInfo(c, Object.class);
        final String childName = name.substring(name.indexOf(".") + 1);
        String path[] = name.split("\\.");

        // for (i=0; i<path.length; i++) {
        final String fieldName = path[0];
        if (fieldName.endsWith("]")) { // parse arrays [n]
            int bracketStart = fieldName.indexOf("[");
            int len = fieldName.length();
            if (bracketStart > 0) {
                fillBeanArrayField(name, value, bean, beanInfo, path, fieldName,
                        bracketStart, len, conversionReport);
            } else {
                throw new WrongProperyIndex(name);
            }
        } else { // parse non arrays
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
                                    valueType, childBean, conversionReport);
                            writter.invoke(bean, valueObject);
                        }
                    }
                }
            }
        }

        // Object bean = c.newInstance();

        return bean;
    }

    private static void fillBeanArrayField(final String name, Object value,
            Object bean, BeanInfo beanInfo, String[] path, final String fieldName,
            int bracketStart, int len,
            Map<String, PropertyDescriptor> conversionReport)
            throws IllegalAccessException, InvocationTargetException,
            IntrospectionException, InstantiationException, IllegalArgumentException,
            WrongProperyIndex {
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
                    if (pd.getPropertyType().isArray()) {
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
                            Object valueObject = fillBeanValueByName(childName, value, array.getClass().getComponentType(), Array.get(array, index), conversionReport);
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
                                    genericClass, null, conversionReport);
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

        boolean isEnum = propertyType.isEnum();
        if (isEnum) {
            ConvertUtils.register(new EnumConverter(), propertyType);
        }

        if (valueObject.getClass().equals(String.class)) {
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


}
