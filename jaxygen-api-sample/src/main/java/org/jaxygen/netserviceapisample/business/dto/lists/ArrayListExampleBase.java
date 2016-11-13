/*
 * Copyright 2016 Jakub Knast.
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
package org.jaxygen.netserviceapisample.business.dto.lists;

import java.util.ArrayList;
import java.util.List;
import org.jaxygen.netserviceapisample.business.dto.UserDTO;

/**
 *
 * @author jknast
 */
public class ArrayListExampleBase {

  private List<String> strings = new ArrayList<>();
  private List<UserDTO> users = new ArrayList<>();
  private String str;

  public List<String> getStrings() {
    return strings;
  }

  public void setStrings(List<String> strings) {
    this.strings = strings;
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }

  public String getS() {
    return str;
  }

  public void setS(String s) {
    this.str = s;
  }

}
