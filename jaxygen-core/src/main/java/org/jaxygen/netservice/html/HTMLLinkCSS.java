/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netservice.html;

/**
 *
 * @author Artur Keska
 */
public class HTMLLinkCSS extends BasicHTMLElement {

  public static final String TAG = "link";
  
  public HTMLLinkCSS() {
    super(TAG);
    setAttribute("type", "text/css");    
    setAttribute("rel", "stylesheet");
  }
  
  public HTMLLinkCSS(final String id) {
    super(TAG, id);
  }

  @Override
  protected boolean isShortTagAlowed() {
    return false;
  }
  
  public final void setHref(final String href) {
    setAttribute("href", href);
  }
  
}
