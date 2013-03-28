package org.jaxygen.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Artur Keska
 */
@XmlRootElement
public class Response implements Serializable {

  public final static long serialVersionUID = 53489753810234L;

  public static class ResponseDTO implements Serializable {

    public final static long serialVersionUID = 53489753810231L;
    private String responseClass;
    private Object responseObject;

    public ResponseDTO() {
    }

    private ResponseDTO(Object o) {
      if (o != null) {
        responseClass = o.getClass().getCanonicalName();
        responseObject = o;
      } else {
        responseClass = "null";
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
