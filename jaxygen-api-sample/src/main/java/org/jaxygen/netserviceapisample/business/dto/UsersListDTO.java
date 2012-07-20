/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business.dto;

import java.util.List;

/**
 *
 * @author artur
 */
public class UsersListDTO {
 public List<UserResponseDTO> users;

 public List<UserResponseDTO> getUsers() {
  return users;
 }

 public void setUsers(List<UserResponseDTO> users) {
  this.users = users;
 }
 
}
