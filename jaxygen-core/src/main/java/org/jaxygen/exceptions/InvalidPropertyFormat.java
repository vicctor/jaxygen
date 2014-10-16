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

@NetAPI(description="This exception is thrown in case if provided parameter format is not valid.")
public class InvalidPropertyFormat extends BasicException {
  private static final long serialVersionUID = 1757274619471348761L;

  public InvalidPropertyFormat() {
   
  }
  
  public InvalidPropertyFormat( final String message ) {
    super(message);
  }
}
