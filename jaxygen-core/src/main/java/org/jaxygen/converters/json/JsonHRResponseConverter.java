/*
 * Copyright 2012 artur.
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
import com.google.gson.GsonBuilder;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.jaxygen.converters.ResponseConverter;
import org.jaxygen.converters.exceptions.SerializationError;

/**
 *
 * @author artur
 */
public class JsonHRResponseConverter implements ResponseConverter {

  public final static String NAME = "JSONHR";

  public void serialize(Object object, OutputStream writter) throws SerializationError {
    try {

      final Gson gson = JSONBuilderRegistry.getBuilder().build();
      final String json = gson.toJson(object);
      writter.write(json.getBytes(Charset.forName("UTF-8")));
    } catch (Exception ex) {
      throw new SerializationError("Could not serialize output data.", ex);
    }
  }

  public String getName() {
    return NAME;
  }
}
