package org.jaxygen.security;

import java.io.Serializable;
import org.jaxygen.security.basic.SecuredMethodDescriptor;

/**Implementation of this class tells to the application container if the given method is secured.
 *
 * @author artur
 */
public interface SecurityProfile extends Serializable {
    /**Returns an array of user profiles bound to this provider.
     * 
     * @return List of user groups.
     */
    String[] getUserGroups();
    
    /**Check if the given method has assigned security descriptor.
     * 
     * @param className Name of the checked class.
     * @param methodName Name of the checked method.
     * @return Descriptor of the found method.
     */
    SecuredMethodDescriptor isAllowed(final String className, final String methodName);
    
    /** Get the list of allowed methods.
     * 
     * @return Each method is described by token className#methodName.
     */
    String[] getAllowedMethodDescriptors();
}
