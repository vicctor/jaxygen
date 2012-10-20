package org.jaxygen.client.it;

import org.jaxygen.annotations.NetAPI;

/**
 * Hello world!
 *
 */
public class TestBean implements TestBeanInterface 
{

    @NetAPI
    public String sayHello() {
       return "Hello World";
    }
   
}
