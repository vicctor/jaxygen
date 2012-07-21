package org.jaxygen.converters.properties;

import org.apache.commons.beanutils.Converter;

public class EnumConverter implements Converter {

  @SuppressWarnings("unchecked")
  @Override
  public Object convert(Class type, Object value) {
    Object rc = null;
    Object enums[] = type.getEnumConstants();
    for (Object o:enums) {
      if (o.toString().equals(value))
        rc = o;
    }
    return rc;
  }

}
