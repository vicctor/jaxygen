package org.jaxygen.netservice.html;


public class HTMLSelect extends BasicHTMLElement {
  
  public HTMLSelect()
  {
    super("SELECT");
  }
  
  public HTMLSelect( final String id )
  {
    super("SELECT",id);
    setAttribute("name", id);
  }

  public void addOption( HTMLOption option ) {
    getContent().add(option);
  }
  
  
  
}
