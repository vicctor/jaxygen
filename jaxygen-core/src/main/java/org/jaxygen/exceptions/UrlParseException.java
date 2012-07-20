/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.exceptions;

/**
 *
 * @author artur
 */
public class UrlParseException extends Exception {

    public UrlParseException() {
    }

    
    
    public UrlParseException(String string, Exception ex) {
        super(string,ex);
    }
    
}
