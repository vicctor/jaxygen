/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.invoker.apibrowser;

import org.jaxygen.netservice.html.*;

/**
 *
 * @author artur
 */
public class Page implements HTMLElement {

  private HTML html = new HTML();
  private HTMLBody body = new HTMLBody();

  public Page() {
    html.append(new HTMLLink(HTMLLink.Type.CSS, "?resource=/org/jaxygen/invoker/css/page.css"));
    
    body.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Application interface")));
    
    html.append(body);
  }

  public void append(HTMLElement... elements) {
    body.append(elements);
  }

  public String render() {
    return html.render();
  }
}
