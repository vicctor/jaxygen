/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample;

import java.util.ArrayList;
import java.util.List;
import pl.devservices.netservice.invoker.ClassRegistry;
import pl.devservices.netservice.netserviceapisample.business.*;

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
