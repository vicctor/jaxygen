/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.invoker.apibrowser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.jaxygen.invoker.ClassRegistry;
import org.jaxygen.netservice.html.*;

/**
 *
 * @author artur
 */
public class Page implements HTMLElement {
  protected ClassRegistry registry;
    
  private HTML html = new HTML();
  private HTMLBody body = new HTMLBody();
  private HTMLHead head = new HTMLHead();

  public Page(ServletContext context) throws ServletException {
    openClassRegistry(context);
    
    HTMLLinkCSS css = new HTMLLinkCSS();
    css.setHref("?resource=/org/jaxygen/invoker/css/page.css");

    head.append(css);
    body.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Application interface")));

    html.append(head);
    html.append(body);
  }

  public void append(HTMLElement... elements) {
    body.append(elements);
  }

  public String render() {
    return html.render();
  }

  private void openClassRegistry(ServletContext context) throws ServletException {

    final String registryClassName = context.getInitParameter("classRegistry");
    if (registryClassName != null) {
      try {
        Class<ClassRegistry> registryClass = (Class<ClassRegistry>) Thread.currentThread().getContextClassLoader().loadClass(registryClassName);
        registry = registryClass.newInstance();
      } catch (InstantiationException ex) {
        throw new ServletException("Cann not instantiate class registy " + registryClassName + ". Please check classRegistry property in your web.xml <context-param> section", ex);
      } catch (IllegalAccessException ex) {
        throw new ServletException("Class registry provider not found. Please check classRegistry property in your web.xml <context-param> section", ex);
      } catch (ClassNotFoundException ex) {
        throw new ServletException("Class registry provider not found. Please check classRegistry property in your web.xml <context-param> section", ex);
      }
    }

  }
}
