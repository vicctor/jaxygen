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

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;

/**
 *
 * @author artur
 */
public class HelloWorld {

 @NetAPI(description = "This is Hello World function. Revice output to learn about the output message structure",
         status= Status.GenerallyAvailable,
         version="1.0"
       )
 public String sayHello() {
  return "Hello World";
 }

 @NetAPI(description = "This method jsut say bye bye",
         status= Status.GenerallyAvailable,
         version="1.0")
 public String sayBye() {
  return "Bye bye World";
 }
 
 
}
