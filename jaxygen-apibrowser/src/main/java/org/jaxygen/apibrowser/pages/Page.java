/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.apibrowser.pages;

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
    css.setHref("css/org/jaxygen/apibrowser/page.css");
    
    head.append(css);
    
    css = new HTMLLinkCSS();
    css.setHref("css/org/jaxygen/apibrowser/classes-snippest.css");    
    
    head.append(css);
    
    final HTMLDiv pagetHeader = new HTMLDiv();
    final HTMLDiv pagetFooter = new HTMLDiv();
    final HTMLHeading pageTitle = new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Jaxygen API Browser"));
    
    pagetHeader.setCSSClassName("jaxygen-page-header");    
    pagetFooter.setCSSClassName("jaxygen-page-footer");
    
    pagetHeader.append(pageTitle);
    body.append(pagetHeader);

    html.append(head);
    html.append(body);
    html.append(pagetFooter);
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
