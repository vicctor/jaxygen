package org.jaxygen.netservice.html;

public class HTMLLink extends BasicHTMLElement {

  public HTMLLink( final String url ) {
    super("A");
    setAttribute("href", url);
  }

  public HTMLLink( final String id, final String url ) {
    super("A", id);
    setAttribute("href", url);
  }

  public HTMLLink( final String url, HTMLElement content ) {
    super("A");
    setAttribute("href", url);
    super.getContent().add(content);
  }
}
