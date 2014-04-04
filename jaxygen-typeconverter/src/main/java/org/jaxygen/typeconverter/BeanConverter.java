package org.jaxygen.typeconverter;

import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.jaxygen.util.BeanUtil;


/**
 * The generic implementation of the class responsible to convert one POJO into anther. NOTE: class is marked abstract due to JVM generics implementation
 * converter must be instantiated to the concrete form always:
 *
 * Example usage: b = new BeanConverter<AClasss, BClass>{}.convert(a);
 *
 * @author Artur Keska <artur.keska@xdsnet.pl>
 */
public abstract class BeanConverter<FROM, TO> extends ClassToClassTypeConverter<FROM, TO> {

  @Override
  public TO convert(FROM from) throws ConversionError {
    TO to = null;
    try {
      if (from != null) {
        to = to().getConstructor().newInstance();
      }
    } catch (Exception ex) {
      throw new ConversionError("Could not convert parameter", ex);
    }
    if (to != null) {
      BeanUtil.translateBean(from, to);
    }
    return to;
  }
}
