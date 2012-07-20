/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample;

import org.jaxygen.netserviceapisample.business.HelloWorld;
import org.jaxygen.netserviceapisample.business.ValidatorsSample;
import org.jaxygen.netserviceapisample.business.SecuritySample;
import org.jaxygen.netserviceapisample.business.DTOSample;
import org.jaxygen.netserviceapisample.business.SessionHandeSample;
import java.util.ArrayList;
import java.util.List;
import org.jaxygen.invoker.ClassRegistry;

/**
 *
 * @author artur
 */
public class SampleClassRegistry implements ClassRegistry
{

    @Override
    public List<Class> getRegisteredClasses() {
        List<Class> clases = new ArrayList<Class>();
        clases.add(HelloWorld.class);
        clases.add(SessionHandeSample.class);
        clases.add(DTOSample.class);
        clases.add(ValidatorsSample.class);
        clases.add(SecuritySample.class);
        return clases;
    }
}
