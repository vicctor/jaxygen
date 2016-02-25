/*
 * Copyright 2016 Your Name <your.name at your.org>.
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
package org.jaxygen.propertyinjector;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import org.jaxygen.propertyinjector.exceptions.PropertyEnhancementException;

/**
 * Inject declared parameters into the
 *
 */
public class PropertyInjector {

  public static void bind(Object dtos[], ValueProvider.ValueBuilder<?> ... providers) throws PropertyEnhancementException {

    for (Object dto : dtos) {
      for (Field f : dto.getClass().getDeclaredFields()) {
        for (ValueProvider.ValueBuilder<?> p : providers) {
          @SuppressWarnings("unchecked")
          Object annotation = f.getAnnotation(p.getAnnotation());
          if (annotation != null) {
            try {
              boolean origAccess = f.isAccessible();
              f.setAccessible(true);
              f.set(dto, p.getFactory().value());
              f.setAccessible(origAccess);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
              throw new PropertyEnhancementException(ex);
            }
          }
        }
      }
    }
  }
}
