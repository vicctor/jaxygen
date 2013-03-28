/*
 * Copyright 2013 xnet.
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
package org.jaxygen.dto.security;

import java.io.Serializable;

/**
 *
 * @author xnet
 */
public class SecurityProfileDTO implements Serializable{
  private static final long serialVersionUID = 13654L;
  
  private String[] groups;
  private String[] allowedMethods;

  public String[] getGroups() {
    return groups;
  }

  public void setGroups(String[] groups) {
    this.groups = groups;
  }

  public String[] getAllowedMethods() {
    return allowedMethods;
    
  }

  public void setAllowedMethods(String[] allowedMethods) {
    this.allowedMethods = allowedMethods;
  }
}
