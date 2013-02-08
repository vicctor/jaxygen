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
package org.jaxygen.converters.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.jaxygen.converters.ResponseConverter;
import org.jaxygen.converters.exceptions.SerializationError;

/**
 *
 * @author artur
 */
public class XMLResponseConverter implements ResponseConverter {

 public final static String NAME = "XML";

 public void serialize(Object msg, OutputStream writter) throws SerializationError {
  if (msg != null) {
      try {
        // get the list of already registered classes
        List<Class<?>> registeredClasses = new ArrayList<Class<?>>();
        registeredClasses.add(msg.getClass()); // add
        

        Class<?> classes[] = new Class<?>[registeredClasses.size()];
        JAXBContext jc = JAXBContext.newInstance((Class<?>[]) registeredClasses.toArray(classes));

        Marshaller marshaler = jc.createMarshaller();
        marshaler.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        

        marshaler.marshal(msg, writter);
        
      } catch (Exception e) {
        Logger.getLogger(XMLResponseConverter.class.getName()).log(Level.SEVERE, "Serialize error : ", e);
        try {
          writter.write(e.toString().getBytes());
        } catch (IOException ex) {
          Logger.getLogger(XMLResponseConverter.class.getName()).log(Level.SEVERE, "Could not respond with an error : ", ex);
        }
      }
    }
 }

 public String getName() {
  return NAME;
 }
}
