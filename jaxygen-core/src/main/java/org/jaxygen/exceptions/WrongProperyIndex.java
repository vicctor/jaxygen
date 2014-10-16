/*
 * Copyright 2014 Artur.
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
package org.jaxygen.exceptions;

import org.jaxygen.annotations.NetAPI;

@NetAPI(description="The property provided in the properties array has invalid index")
public class WrongProperyIndex extends Exception {
  private static final long serialVersionUID = 472806478442825141L;
  
  public WrongProperyIndex()
  {
  }
  
  public WrongProperyIndex(final String name) {
    super(name);
  }
}
