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
