package org.jaxygen.client.it;

//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
import org.jaxygen.client.it.TestBeanInterface;
import org.jaxygen.client.it.TestBeanInterface;
import org.jaxygen.client.it.TestBeanInterface;
import org.jaxygen.client.jaxygenclient.JaxygenClient;
//import org.jaxygen.client.jaxygenclient.JaxygenClient;

/**
 * Unit test for simple App.
 */
public class JaxygenClientIT //   extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JaxygenClientIT( String testName )
    {
        //super( testName );
    }

    /**
     * @return the suite of tests being tested
    
    public static Test suite()
    {
        return new TestSuite( JaxygenClientIT.class );
    }
    *  */

    /**
     */
    public void test_SimpleBeanClass()
    {
        System.out.println("Test is running");
        JaxygenClient factory = new JaxygenClient("http://localhost:8080/");
        TestBeanInterface bean = (TestBeanInterface)factory.lookup("invoker/TestBean", TestBeanInterface.class);
        //assertEquals("Hello World", bean.sayHello());
        
        System.out.println(bean.sayHello());
    }
    
    public static void main(String... args) {
        new JaxygenClientIT(null).test_SimpleBeanClass();
    }
            
}
