/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business;

import javax.servlet.http.HttpSession;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.exceptions.CouldNotLeanWithoutEntering;

/**Class demonstrates the session handing using @SessionContext annotation
 *
 * @author artur
 */
public class SessionHandeSample {
 @SessionContext
 private HttpSession session;
 
 @NetAPI(description="Increment counter. Method retuns nothing, just always successes",
         status= Status.ReleaseCandidate,
         version="1.0")
 public void enter() {
  Integer counter = (Integer) session.getAttribute("counter");
  if (counter == null) {
   counter = 0;
  } else {
   counter ++;
  }
  session.setAttribute("counter", counter);
 }
 
 /** This method demonstrates exception handling.
  * If enter has not been yet called, 
  * @throws CouldNotLeanWithoutEntering 
  */
 @NetAPI(description="Decrement counter. Counter must be at least once <br/>incremented before calling this method",
         status= Status.ReleaseCandidate,
         version="1.0")
 public void leave() throws CouldNotLeanWithoutEntering {
  Integer counter = (Integer) session.getAttribute("counter");
  if (counter == null) {
   throw new CouldNotLeanWithoutEntering();
  } else {
   counter --;
  }
  session.setAttribute("counter", counter);
 }
 
 @NetAPI(description="Get current counter value",
         status= Status.ReleaseCandidate,
         version="1.0")
 public int count() {
  Integer counter = (Integer) session.getAttribute("counter");
  if (counter == null) {
   counter = 0;
  }
  return counter;
 }
}
