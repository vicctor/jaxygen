package org.jaxygen.netservice.html;

public class HTMLForm extends BasicHTMLElement {

 public enum Action {
  post, get, put
 }
 
  public HTMLForm(String id) {
    super("FORM",id);        
  }
  
  public HTMLForm() {
    super("FORM");   
  }
  
  public void setMethod( final Action method ) {
    setAttribute("method", method);
  }
  
  public void setAction( final String action ) {
    setAttribute("action", action.toString());
  }
  
  public void setEnctype( final String method ) {
    setAttribute("enctype", method);
  }

  /**Set the enctype="multipart/form-data" property
   * 
   */
  public void setMultipartDataEncoding() {
    setAttribute("enctype","multipart/form-data");
  }
 
  
  public HTMLInput appendInput(HTMLInput.Type type, final String name, final String value) {
   HTMLInput input = new HTMLInput();
   input.setType(type);
   input.setName(name);
   input.setValue(value);
   append(input);
   return input;
  }
  
}
