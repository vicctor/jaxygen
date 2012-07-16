/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample.business.dto;

import pl.devservices.netservice.annotations.QueryMessage;

/**
 *
 * @author artur
 */
@QueryMessage //* Request DTO must be annotated with @QueryMessage. Otherwise 
              //* engine will not parse it.
public class UserRegistrationRequestDTO extends UserDTO {
 
}
