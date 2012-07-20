/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business.dto;

import org.jaxygen.annotations.QueryMessage;

/**
 *
 * @author artur
 */
@QueryMessage //* Request DTO must be annotated with @QueryMessage. Otherwise 
              //* engine will not parse it.
public class UserRegistrationRequestDTO extends UserDTO {
 
}
