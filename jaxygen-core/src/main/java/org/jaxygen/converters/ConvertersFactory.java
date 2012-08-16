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
package org.jaxygen.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jaxygen.converters.properties.PropertiesToBeanConverter;

/**
 *
 * @author Artur Keska
 */
public class ConvertersFactory {
  private static final Map<String, RequestConverter> requestConverters = Collections.synchronizedMap(new HashMap<String, RequestConverter>());
  private static final Map<String, ResponseConverter> responseConverters = Collections.synchronizedMap(new HashMap<String, ResponseConverter>());
  

  public static void registerRequestConverter(RequestConverter coverter) {
   requestConverters.put(coverter.getName(),coverter);
  }
  
  public static void registerResponseConverter(ResponseConverter converter) {
   responseConverters.put(converter.getName(), converter);
  }
  
  /** Obtain request converted for given name
   * 
   * @param name converter name (build in converters: PROPERTIES)
   * @return 
   */
  public static synchronized RequestConverter getRequestConverter(final String name) {
    return requestConverters.get(name);
  }
  
    /** Obtain response converted for given name
   * 
   * @param name converter name
   * @return 
   */
  public static synchronized ResponseConverter getResponseConverter(final String name) {
    return responseConverters.get(name);
  }
}
