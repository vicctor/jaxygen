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
@NetAPI(description="Invalid user name or password")
public class LoginFailed extends Exception {

    public LoginFailed() {
    }
    
}
