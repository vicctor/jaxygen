package org.jaxygen.netservice.html;

public class HTMLOption extends BasicHTMLElement {

  
  public HTMLOption( final HTMLElement content ) {
    super("OPTION");
    getContent().add( content ); 
  }

  public HTMLOption( final String id, final HTMLElement content ) {
    super("OPTION",id );
    getContent().add( content );
  }
  
  public void setTitle(final String title) {
    setAttribute("title", title);
  }
  
  public void setSelected(final boolean selected) {
    if (selected) { 
      setAttribute("selected", "selected"); 
    } 
  }

}
