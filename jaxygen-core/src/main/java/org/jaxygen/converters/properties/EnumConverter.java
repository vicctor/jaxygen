/*
 * Copyright 2014 Artur.
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
