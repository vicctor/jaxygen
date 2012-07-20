/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.security.basic;

/**Class describes single method that is alowed by the 
 * security system for execution in given security context
 *
 * @author artur
 */
public class SecuredMethodDescriptor {

    private String className;
    private String methodName;

    public SecuredMethodDescriptor(final String className, final String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public SecuredMethodDescriptor() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
