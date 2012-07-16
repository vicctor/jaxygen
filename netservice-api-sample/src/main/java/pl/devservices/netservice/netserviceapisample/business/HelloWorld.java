package pl.devservices.netservice.netserviceapisample.business;

import pl.devservices.netservice.annotations.NetAPI;

/**
 *
 * @author artur
 */
public class HelloWorld {

 @NetAPI(description = "This is Hello World function. Revice output to learn about the output message structure")
 public String sayHello() {
  return "Hello World";
 }

 @NetAPI(description = "This method jsut say bye bye")
 public String sayBye() {
  return "Bye bye World";
 }
}
