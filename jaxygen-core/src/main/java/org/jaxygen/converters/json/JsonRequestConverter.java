/*
 * Copyright 2012 jaxzgen.org.
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
package org.jaxygen.converters.json;

import com.google.gson.Gson;
import java.io.IOException;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.http.HttpRequestParams;

/**
 *
 * @author xnet
 */
public class JsonRequestConverter implements RequestConverter {

  public final static String NAME = "JSON";
  private static Gson gson = JSONBuilderRegistry.getBuilder().build();
  public String getName() {
    return NAME;
  }

  @Override
  public Object deserialise(HttpRequestParams params, Class<?> beanClass) throws DeserialisationError {
    Object rc = null;
    try {
      final String jqueryString = params.getAsString(beanClass.getName(), 1, Integer.MAX_VALUE, true);
      rc = gson.fromJson(jqueryString, beanClass);
    } catch (IOException ex) {
      throw new DeserialisationError("Could not obtain field data for class " + beanClass.getName(), ex);
    }
    return rc;
  }
}
