/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netservice.html;

/**
 *
 * @author Artur Keska
 */
public class HTMLLink extends BasicHTMLElement {

  public static final String TAG = "link";

  public enum Type {
    CSS("text/css"),
    JAVASCRIPT("text/javascript");
    private final String typeName;
    Type(final String typeName) {
      this.typeName = typeName;
    }
    public final String getTypeName() {
      return typeName;
    }
  }
  
  public HTMLLink() {
    super(TAG);
  }
  
  public HTMLLink(Type type, final String href) {
    super(TAG);
    setType(type);
    setHref(href);   
  }

  public HTMLLink(final String id) {
    super(TAG, id);
  }
  
  public final void setType(Type type) {
    setAttribute("type", type.typeName);
  }
  
  public final void setHref(final String href) {
    setAttribute("href", href);
  }
  
}
