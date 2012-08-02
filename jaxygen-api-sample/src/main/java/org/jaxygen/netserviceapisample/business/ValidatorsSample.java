/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.EmailRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.RangeRequestDTO;

/**This class demonstrates validators usage
 *
 * @author artur
 */
@NetAPI(description="Class demonstrates NetService validators",
         status= Status.ReleaseCandidate,
         version="1.0")
public class ValidatorsSample {
 @NetAPI(description="String validation sample")
 public String enterEmail(EmailRequestDTO request) {
  return "Passed e-mail : " + request.getEmail();
 }
 
 @NetAPI(description="String validation sample",
         status= Status.ReleaseCandidate,
         version="1.0")
 public String range10to100(RangeRequestDTO request) {
  return "Passed value is : " + request.getValue();
 }
}
