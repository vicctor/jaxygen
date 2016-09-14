/*
 * Copyright 2012 imfact02.
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.converters.properties.PropertiesToBeanConverter;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.http.HttpRequestParams;
import org.jaxygen.network.UploadedFile;

/**
 *
 * @author imfact02
 */
public class JsonMultipartRequestConverter implements RequestConverter {

  public final static String NAME = "JSON/MULTIPART";
  private static Gson gson = JSONBuilderRegistry.getBuilder().build();
  private final static Map<String, String> nullPropertes = new HashMap<String, String>();

  public String getName() {
    return NAME;
  }

  public Object deserialise(HttpRequestParams params, Class<?> beanClass) throws DeserialisationError {
    Map<String, Uploadable> files = params.getFiles();
    Object rc = null;
    if (files != null) {
      Uploadable f = files.get(beanClass.getName());
      if (f != null) {
        Reader reader = null;
        try {
          reader = new InputStreamReader(f.getInputStream(), "UTF-8");
          rc = gson.fromJson(reader, beanClass);
          try {
            PropertiesToBeanConverter.convertPropertiesToBean(nullPropertes, files, rc);
          } catch (Exception ex) {
            throw new DeserialisationError("Could not feel been files", ex);
          }
        } catch (IOException ex) {
          throw new DeserialisationError("Could not obtain field data for class " + beanClass.getName(), ex);
        } finally {
          if (reader != null) {
            try {
              reader.close();
            } catch (IOException ex) {
              throw new DeserialisationError("Cluld not close input stream while deserializing class " + beanClass.getName(), ex);

            }
          }
        }
      }
    }
    return rc;
  }
}
