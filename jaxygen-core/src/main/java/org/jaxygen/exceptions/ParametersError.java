/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.exceptions;

/**
 *
 * @author artur
 */
public class ParametersError extends Exception {

    public ParametersError(String message, Exception ex) {
        super(message,ex);
    }
    
}
