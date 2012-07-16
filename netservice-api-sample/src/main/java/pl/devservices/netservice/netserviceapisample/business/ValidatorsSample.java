/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample.business;

import pl.devservices.netservice.annotations.NetAPI;
import pl.devservices.netservice.netserviceapisample.business.dto.EmailRequestDTO;
import pl.devservices.netservice.netserviceapisample.business.dto.RangeRequestDTO;

/**
 *
 * @author artur
 */
@NetAPI(description="Class demonstrates NetService validators")
public class ValidatorsSample {
 @NetAPI(description="String validation sample")
 public String enterEmail(EmailRequestDTO request) {
  return "Passed e-mail : " + request.getEmail();
 }
 
 @NetAPI(description="String validation sample")
 public String range10to100(RangeRequestDTO request) {
  return "Passed value is : " + request.getValue();
 }
}
