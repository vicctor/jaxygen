package org.jaxygen.netservice.html;

public class HTMAnchor extends BasicHTMLElement {

  public HTMAnchor( final String url ) {
    super("A");
    setAttribute("href", url);
  }

  public HTMAnchor( final String id, final String url ) {
    super("A", id);
    setAttribute("href", url);
  }

  public HTMAnchor( final String url, HTMLElement content ) {
    super("A");
    setAttribute("href", url);
    super.getContent().add(content);
  }
  
  public HTMAnchor( final String id, final String cssClass, final String url, HTMLElement content ) {
    super("A", id);
    setCSSClassName(cssClass);
    setAttribute("href", url);
    super.getContent().add(content);
  }
}
