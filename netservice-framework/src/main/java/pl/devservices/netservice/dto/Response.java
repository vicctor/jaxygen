/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.dto;

/**
 *
 * @author Artur Keska
 */
public class Response {

 public static class ResponseDTO {

  private String responseClass;
  private Object responseObject;

  private ResponseDTO(Object o) {
   if (o != null) {
    responseClass = o.getClass().getCanonicalName();
    responseObject = o;
   } else {
    responseClass="null";
    responseObject = null;
   }
  }

  public String getResponseClass() {
   return responseClass;
  }

  public void setResponseClass(String responseClass) {
   this.responseClass = responseClass;
  }

  public Object getResponseObject() {
   return responseObject;
  }

  public void setResponseObject(Object responseObject) {
   this.responseObject = responseObject;
  }
 }
 private ResponseDTO dto;

 public Response() {
 }

 public Response(Object o) {
  dto = new ResponseDTO(o);
 }

 public ResponseDTO getDto() {
  return dto;
 }

 public void setDto(ResponseDTO dto) {
  this.dto = dto;
 }
}
