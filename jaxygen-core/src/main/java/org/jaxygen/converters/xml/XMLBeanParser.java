/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
