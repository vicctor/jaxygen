package org.jaxygen.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;
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
   * @param from data provider object
   * @param to data acceptor object.
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
   * Method goes through bean getters, and check if the returned value matches
   * the validator passed in @StringPropertyValidator or
   * @NumberPropertyValidator Method check all methods which returns primitive
   * type, Long or Integer or a bean class annotated but the @Validable
   * annotation type and does not takes any argument.
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
}
