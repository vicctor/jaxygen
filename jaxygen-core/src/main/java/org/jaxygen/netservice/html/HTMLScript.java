/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netservice.html;

/**
 *
 * @author Artur Keska
 */
public class HTMLScript extends BasicHTMLElement {

  public static final String TAG = "SCRIPT";
  
  public HTMLScript() {
    super(TAG);
    setAttribute("type", "text/javascript");    
  }
  
  public HTMLScript(final String id) {
    super(TAG, id);
  }
  
  public HTMLScript(final String id, final String src) {
    super(TAG, id);
    setSource(src);
  }

  public final void setSource(final String href) {
    setAttribute("src", href);
  }
  
}
