/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.invoker;

import java.util.List;

/**Implemet this interface int order to inform NetService about classes providing
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
