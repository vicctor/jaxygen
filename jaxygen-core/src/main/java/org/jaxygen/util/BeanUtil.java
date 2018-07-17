package org.jaxygen.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.PropertyUtils;
import org.jaxygen.annotations.BeanTransient;
import org.jaxygen.annotations.NumberPropertyValidator;
import org.jaxygen.annotations.StringPropertyValidator;
import org.jaxygen.annotations.Validable;
import org.jaxygen.exceptions.InvalidPropertyFormat;

public class BeanUtil {

    /**
     * Copy bean content from one bean to another. The bean is copied using the
     * set and get methods. Method invokes all getXX methods on the from object,
     * and result pass to the corresponding setXX methods in the to object. Note
     * that it is not obvious that both object are there same type, only set and
     * get methods must much.
     *
     * @param from data provider object.
     * @param to data acceptor object.
     */
    public static void translateBean(Object from, Object to) {
        {
            PropertyDescriptor[] fromGetters = null;
            fromGetters = PropertyUtils.getPropertyDescriptors(from.getClass());
            for (PropertyDescriptor pd : fromGetters) {
                if (pd.getReadMethod().isAnnotationPresent(BeanTransient.class) == false) {
                    if (PropertyUtils.isWriteable(to, pd.getName())) {
                        try {
                            PropertyDescriptor wd = PropertyUtils.getPropertyDescriptor(to, pd.getName());
                            if (wd.getPropertyType().isAssignableFrom(pd.getPropertyType())) {
                                Object copyVal = PropertyUtils.getProperty(from, pd.getName());
                                PropertyUtils.setProperty(to, wd.getName(), copyVal);
                            } else {
                                Logger.getLogger(BeanUtil.class.getCanonicalName()).log(Level.WARNING, "Method {0}.{1} of type {2} is not compatible to {3}.{4} of type{5}", new Object[]{from.getClass().getName(), pd.getName(), pd.getPropertyType(), to.getClass().getName(), wd.getName(), wd.getPropertyType()});
                            }
                        } catch (IllegalAccessException ex) {
                            throw new java.lang.IllegalArgumentException("Could not translate bean", ex);
                        } catch (InvocationTargetException ex) {
                            throw new java.lang.IllegalArgumentException("Could not translate bean", ex);
                        } catch (NoSuchMethodException ex) {
                            throw new java.lang.IllegalArgumentException("Could not translate bean", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method goes through bean getters, and check if the returned value matches
     * the validator passed in {\link StringPropertyValidator} or
     * {\link NumberPropertyValidator} Method check all methods which returns primitive
     * type, Long or Integer or a bean class annotated but the
     * {link Validable} annotation type and does not takes any argument.
     *
     * @param bean Bean under validation.
     * @throws InvocationTargetException .
     * @throws IllegalAccessException .
     * @throws IllegalArgumentException .
     * @throws InvalidPropertyFormat .
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
}
