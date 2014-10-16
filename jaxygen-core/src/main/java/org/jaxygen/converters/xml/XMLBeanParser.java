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
package org.jaxygen.converters.xml;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author artur
 */
public class XMLBeanParser {
     /**
     * Parse input stream into a bean class specified by parameter c.
     *
     * @param is
     * @param c
     * @return
     * @throws JAXBException
     */
    static public Object parseXMLtoBean(InputStream is, Class<?> c)
            throws JAXBException {
        Class<?> classes[] = new Class<?>[]{c};
        JAXBContext jc = JAXBContext.newInstance(classes);
        Unmarshaller unmarshaler = jc.createUnmarshaller();
        return unmarshaler.unmarshal(is);
    }
}
