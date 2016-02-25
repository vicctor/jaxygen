/*
 * Copyright 2012 Artur Keska.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.netserviceapisample.business;

import javax.servlet.http.HttpSession;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.NetworkInfoDTO;
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
 
 
 @NetAPI(description = "Get the client connection information",
        status = Status.GenerallyAvailable,
        version = "1.0")
 public NetworkInfoDTO echoNetworkInfo(NetworkInfoDTO info) {
   return info;
 }
}
