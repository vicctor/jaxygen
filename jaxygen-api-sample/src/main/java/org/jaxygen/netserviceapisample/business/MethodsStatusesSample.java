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

import java.io.File;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.dto.Uploadable;



/**This class is used to test and demonstrate the jaxygen methods statuses annotations
 *
 * @author Artur Keska
 */
public class MethodsStatusesSample {
  @NetAPI(description="Status not provided")
  void methodWithoutStatus() {}
  
  @NetAPI(description="Method which returns something but does not perform a real job. "
          + "This kind of methods is usefull for verry first stagging implementsion and design verification",
          status= Status.Mockup,
          version="1.0")
  public String mockupMethod() {return "Hello, it just looks i'm there, but I'm stil not real";}
  
  @NetAPI(description="The method is not functional. Method has been either destroyed by the developer"
          + " or the implementation han not been started so far (even for mocking)",
          status= Status.Nonfunctional,
          version="1.0")
  public String nonFunctional() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not implemented so far");
  }
  
  @NetAPI(description="Method has been implemented, and developer did not found important errors",
          status= Status.ReleaseCandidate,
          version="1.0")
  public String releaseCandidate() {
    return "Hi, I'm working. And I think I'm good!";
  }
  
  @NetAPI(description="Method has been implemented. Developer has fixed all bugs reported by comunity "
          + "and decided that the method is good enough for public usage.",
          status= Status.GenerallyAvailable,
          version="1.0.1")
  public String generalyyAvailable() {
    return "Hi, I'm working. And I think I'm good! What's better all around me think I'm good!";
  }
}
