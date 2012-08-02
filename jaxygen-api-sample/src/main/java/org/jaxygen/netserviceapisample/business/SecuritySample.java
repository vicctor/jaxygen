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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.SampleClassRegistry;
import org.jaxygen.netserviceapisample.business.dto.LoginRequestDTO;
import org.jaxygen.security.SecurityProfile;
import org.jaxygen.security.annotations.LoginMethod;
import org.jaxygen.security.annotations.LogoutMethod;
import org.jaxygen.security.annotations.Secured;
import org.jaxygen.security.annotations.SecurityContext;
import org.jaxygen.security.basic.BasicSecurityProviderFactory;
import org.jaxygen.security.basic.annotations.UserProfile;

/**Class demonstrates session and security handing 
 * in NetAPI framework.
 *
 * @author artur
 */

public class SecuritySample {
 @SessionContext
 private HttpSession session;
 
 @SecurityContext
 private SecurityProfile sp;
 
 static List<LoginRequestDTO> list = Collections.synchronizedList(new ArrayList<LoginRequestDTO>());
 
 List<LoginRequestDTO> getLoggedInUsersList() { 
  return list;
 }
 
 @LoginMethod
 @NetAPI(description="Login to the session. Login as admin, user to attend to admin or user security group. Use any other name in order to attend to guests group",
         status= Status.ReleaseCandidate,
         version="1.0")
 public SecurityProfile login(LoginRequestDTO request) {
  SecurityProfile profile;
  session.setAttribute("loggedInUser", request.getUserName());
  getLoggedInUsersList().add(request);
  
  // select security profiule depending on logged in user
  if ("admin".equals(request.getUserName())) {
   profile = new BasicSecurityProviderFactory(new SampleClassRegistry(), "admin", "user").getProvider();
  } else if ("user".equals(request.getUserName())) {
   profile = new BasicSecurityProviderFactory(new SampleClassRegistry(), "user").getProvider();
  } else {
   profile = new BasicSecurityProviderFactory(new SampleClassRegistry(), "guest").getProvider();
  }
  return profile;
 }
 
 @LogoutMethod
 @NetAPI(description="Release user context from session",
         status= Status.ReleaseCandidate,
         version="1.0")
 public boolean logout() {
  session.setAttribute("loggedInUser", null);
  return true;
 }
 
 @NetAPI(description="Shows who was logged in. Method is available for admin user only. Please login using admin as user before accessing this method",
         status= Status.ReleaseCandidate,
         version="1.0")
 @Secured()
 @UserProfile(name="admin")
 public List<LoginRequestDTO> whoWasLoggedIn() {
  return list;
 }
 
 @NetAPI(description="Methods return the currently logged in user name",
         status= Status.ReleaseCandidate,
         version="1.0")
 @Secured()
 @UserProfile(name="user")
 public String whoAmI() {
  return (String) session.getAttribute("loggedInUser");
 }
 
 @NetAPI(description="Method returns user security profiles",
         status= Status.ReleaseCandidate,
         version="1.0")
 @Secured()
 public String[] getMyProfile() {
  return sp.getUserGroups();
 }
}
