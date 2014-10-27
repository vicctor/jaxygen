/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.invoker;

import java.util.List;

/**Implement this interface in order to inform JAXygen about classes providing
 * the remote service.
 *
 * @author artur
 */
public interface ClassRegistry {

    /**Return the list of classes providing service methods 
     * 
     * @return 
     */
    public List<Class> getRegisteredClasses();
    
}
