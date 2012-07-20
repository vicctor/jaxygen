/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.security.exceptions;

import org.jaxygen.annotations.NetAPI;

/**
 *
 * @author artur
 */
@NetAPI(description="Currenlty logged in user is not allowed to access requested method")
public class NotAlowed extends Exception {

    public NotAlowed(String clazz, String name) {
        super("Method "+clazz+"."+name+": access denied");
    }
    
}
