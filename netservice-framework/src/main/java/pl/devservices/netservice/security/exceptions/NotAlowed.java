/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.security.exceptions;

import pl.devservices.netservice.annotations.NetAPI;

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
