package org.jaxygen.invoker;

import java.util.Set;

/**Implement this interface in order to inform JAXygen about classes providing
 * the remote service.
 *
 * @author artur
 */
public interface ServiceRegistry {

    /**Return the list of classes providing service methods.
     * 
     * @return A list of classes to be registered in this app contxt.
     */
    public Set<Class<?>> getRegisteredClasses();
    
    /** Return the base name of the package, so all services will be exposed
     * without the package name as a prefix.
     * 
     * @return 
     */
    public String getPackageBase();
}
