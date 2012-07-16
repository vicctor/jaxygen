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
@NetAPI(description="Invalid user name or password")
public class LoginFailed extends Exception {

    public LoginFailed() {
    }
    
}
