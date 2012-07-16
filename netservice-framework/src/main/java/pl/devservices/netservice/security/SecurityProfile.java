/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.security;

import pl.devservices.netservice.security.basic.SecuredMethodDescriptor;

/**Implementation of this class tells to the application container if the given method is secured.
 *
 * @author artur
 */
public interface SecurityProfile {
    /**Returns an array of user profiles boud to this provider.
     * 
     * @return 
     */
    String[] getUserGroups();
    
    /**Check if the given method has assigned security descriptor
     * 
     * @param className
     * @param methodName
     * @return 
     */
    SecuredMethodDescriptor isAllowed(final String className, final String methodName);
}
