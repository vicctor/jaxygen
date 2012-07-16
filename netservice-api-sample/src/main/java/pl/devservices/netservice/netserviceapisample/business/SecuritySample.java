/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import pl.devservices.netservice.annotations.NetAPI;
import pl.devservices.netservice.netserviceapisample.SampleClassRegistry;
import pl.devservices.netservice.netserviceapisample.business.dto.LoginRequestDTO;
import pl.devservices.netservice.security.basic.BasicSecurityProviderFactory;
import pl.devservices.netservice.security.SecurityProfile;
import pl.devservices.netservice.security.annotations.LoginMethod;
import pl.devservices.netservice.security.annotations.LogoutMethod;
import pl.devservices.netservice.security.annotations.Secured;
import pl.devservices.netservice.security.annotations.SecurityContext;
import pl.devservices.netservice.annotations.SessionContext;
import pl.devservices.netservice.security.basic.annotations.UserProfile;

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
 @NetAPI(description="Login to the session. Login as admin, user to attend to admin or user security group. Use any other name in order to attend to guests group")
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
 @NetAPI
 public boolean logout() {
  session.setAttribute("loggedInUser", null);
  return true;
 }
 
 @NetAPI
 @Secured()
 @UserProfile(name="admin")
 public List<LoginRequestDTO> whoWasLoggedIn() {
  return list;
 }
 
 @NetAPI
 @Secured()
 @UserProfile(name="user")
 public String whoAmI() {
  return (String) session.getAttribute("loggedInUser");
 }
 
 @NetAPI
 @Secured()
 public String[] getMyProfile() {
  return sp.getUserGroups();
 }
}
