/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business;

import org.jaxygen.netserviceapisample.business.dto.UsersListDTO;
import org.jaxygen.netserviceapisample.business.dto.UsersFilterDTO;
import org.jaxygen.netserviceapisample.business.dto.UserDTO;
import org.jaxygen.netserviceapisample.business.dto.UserRegistrationRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.UserResponseDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.util.BeanUtil;

/**
 *
 * @author artur
 */
public class DTOSample {

 @SessionContext
 HttpSession session;

 List<UserDTO> getUsersFromSession() {
  List<UserDTO> list = (List<UserDTO>) session.getAttribute("dtoSample.users");
  if (list == null) {
   list = Collections.synchronizedList(new ArrayList<UserDTO>());
   session.setAttribute("dtoSample.users", list);
  }
  return list;
 }

 @NetAPI(description="Creates an user entity in the current session")
 public UserDTO createUser(UserRegistrationRequestDTO request) {
  getUsersFromSession().add(request);
  return request;
 }

 @NetAPI(description="Get the list of users from current session. The fiter parameter could selsct users by name substring.")
 public UsersListDTO getUsers(final UsersFilterDTO filter) {
  UsersListDTO response = new UsersListDTO();
  List<UserResponseDTO> users = new ArrayList<UserResponseDTO>();
  int current = 0;
  for (UserDTO u : getUsersFromSession()) {
   if (filter.getFilter() == null || filter.getFilter().length() == 0 || u.getName().contains(filter.getFilter())) {
    UserResponseDTO urd = new UserResponseDTO();
    BeanUtil.translateBean(u, urd);
    urd.setId(current);
    users.add(urd);
   }
   current++;
  }
  response.setUsers(users);  
  return response;
 }
}
