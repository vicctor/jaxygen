/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample.business.dto;

import pl.devservices.netservice.annotations.StringPropertyValidator;
import pl.devservices.netservice.annotations.Validable;

/**
 *
 * @author artur
 */
@Validable
public class LoginRequestDTO {
 private String userName;

 @StringPropertyValidator(minimalLength=1, maximalLength=255)
 public String getUserName() {
  return userName;
 }
 
 public void setUserName(String userName) {
  this.userName = userName;
 }
 
}
