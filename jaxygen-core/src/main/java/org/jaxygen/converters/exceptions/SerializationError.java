/*
 * Copyright 2012 artur.
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
package org.jaxygen.converters.exceptions;

/**This exceptio is thrown if the ResponseConverter was unable to 
 * translate object inot it's serialized form.
 * 
 * * It needs to be guaranteed by the implementation of this class that the serialize oprtation
 * is thread save.
 *
 * @author artur
 */
public class SerializationError extends Exception {

 public SerializationError() {
 }

 public SerializationError(String message, Throwable cause) {
  super(message, cause);
 }

 public SerializationError(Throwable cause) {
  super(cause);
 }
 
}
