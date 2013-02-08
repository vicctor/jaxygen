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
import org.jaxygen.netserviceapisample.business.dto.*;
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

 @NetAPI(description="Creates an user entity in the current session",
         status= Status.ReleaseCandidate,
         version="1.0")
 public UserDTO createUser(UserRegistrationRequestDTO request) {
  getUsersFromSession().add(request);
  return request;
 }

 @NetAPI(description="Get the list of users from current session. The fiter parameter could selsct users by name substring.",
         status= Status.ReleaseCandidate,
         version="1.0")
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
 
  @NetAPI(description="Get the list of users from current session. The fiter parameter could selsct users by name substring.",
         status= Status.ReleaseCandidate,
         version="1.0")
  public String arraysSample(ItemsListRequestDTO request) {
   return request.toString();
  }
}
