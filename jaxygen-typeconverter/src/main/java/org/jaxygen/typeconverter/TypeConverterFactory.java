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
package org.jaxygen.typeconverter;

import java.util.HashMap;
import java.util.Map;
import org.jaxygen.typeconverter.exceptions.ConversionError;

/**
 *
 * @author Artur
 */
public class TypeConverterFactory {
  private Map<Class, Map<Class, TypeConverter>> converters = new HashMap<Class, Map<Class, TypeConverter>>();
  
  /**Add a new converter to this converters factory.
   * 
   * @param converter 
   */
  public void registerConverter(TypeConverter converter) {
        final Class from = converter.from();
        Map<Class, TypeConverter> toMap;
      if (converters.containsKey(from) == false) {
          toMap = new HashMap<Class, TypeConverter>();
          converters.put(from, toMap);
      } else {
          toMap = converters.get(from);
      }
      toMap.put(converter.to(), converter);
  }
  
  /**Get the converter which could translate an object from class from to class to.
   * 
   * @param <FROM>
   * @param <TO>
   * @param from
   * @param to
   * @return 
   */
  public <FROM, TO> TypeConverter<FROM, TO> get(Class<FROM> from, Class<TO> to) {
      TypeConverter<FROM, TO> converter = null;
      if (converters.containsKey(from) && converters.get(from).containsKey(to)) {
          converter = converters.get(from).get(to);
      }
    return converter;
  };
  
  public <FROM, TO> TO convert(FROM from, Class<TO> toClass) throws ConversionError {
      @SuppressWarnings("unchecked")
      TypeConverter<FROM, TO> converter = get((Class<FROM>)from.getClass(), toClass);
      if (converter == null) {
          throw new ConversionError("Could not find converter from class " + from.getClass() + " to class " + toClass);
      }
      return (TO) converter.convert(from);
  }
  
  /** Convenient method used in case if the from object could be null
   * 
   * @param <FROM>
   * @param <TO>
   * @param from
   * @param fromClass
   * @param toClass
   * @return
   * @throws ConversionError 
   */
  public <FROM, TO> TO convert(FROM from, Class<FROM> fromClass, Class<TO> toClass) throws ConversionError {
      @SuppressWarnings("unchecked")
      TypeConverter<FROM, TO> converter = get(fromClass, toClass);
      if (converter == null) {
          throw new ConversionError("Could not find converter from class " + fromClass + " to class " + toClass);
      }
      return (TO) converter.convert(from);
  }
}
