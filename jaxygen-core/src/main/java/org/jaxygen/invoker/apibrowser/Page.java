/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.invoker.apibrowser;


import org.jaxygen.netservice.html.HTML;
import org.jaxygen.netservice.html.HTMLHeading;
import org.jaxygen.netservice.html.HTMLBody;
import org.jaxygen.netservice.html.HTMLElement;
import org.jaxygen.netservice.html.HTMLLabel;

/**
 *
 * @author artur
 */
public class Page implements HTMLElement {

 private HTML html = new HTML();
 private HTMLBody body = new HTMLBody();
 
 public Page() {
  html.append(body);  
  
  body.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Application interface")));
 }

 public void append(HTMLElement... elements) {
  body.append(elements);
 }
 
 public String render() {
  return html.render();
 }
 
}
