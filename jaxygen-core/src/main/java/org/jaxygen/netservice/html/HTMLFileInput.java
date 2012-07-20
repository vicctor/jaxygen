package org.jaxygen.netservice.html;

public class HTMLFileInput extends BasicHTMLElement {

  public HTMLFileInput( final String id ) {
    super("INPUT",id);
    setAttribute("type", "file");
    setAttribute("name", id);  
  }
  
}
