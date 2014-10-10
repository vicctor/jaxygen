/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business.dto;

import org.jaxygen.annotations.ClientIp;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.StringPropertyValidator;
import org.jaxygen.annotations.Validable;

/**
 * This is a sample validable request
 *
 * @author artur
 */
@Validable // Class must be marked as Validable. Otherwise validators are not used.
public class EmailRequestDTO {
    private String email;

    @ClientIp
    public String ip;

    public void setEmail(String email) {
        this.email = email;
    }

    @NetAPI(description = "Put the valid e-mail address here. NetService will validate addres "
            + "and return exception if not correct")
    @StringPropertyValidator(regex = "^[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,})$")
    public String getEmail() {
        return email;
    }

}
