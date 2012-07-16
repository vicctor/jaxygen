/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.invoker.apibrowser;


import pl.devservices.netservice.html.*;

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
