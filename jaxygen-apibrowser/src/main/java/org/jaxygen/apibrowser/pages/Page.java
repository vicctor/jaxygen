/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.apibrowser.pages;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.invoker.ClassRegistry;
import org.jaxygen.netservice.html.*;

/**
 *
 * @author artur
 */
public class Page implements HTMLElement {
  private ClassRegistry registry;
    
  private final HTML html = new HTML();
  private final HTMLBody body = new HTMLBody();
  private final HTMLHead head = new HTMLHead();
  
  
  protected String home;
  protected String servletContext;
  protected String browserPath;
  protected String invokerPath;
  protected String beansPath;

  public Page(ServletContext context, HttpServletRequest request, String classRegistry, String beansPath) throws ServletException {
    final String serletPath = request.getServletPath();
    this.browserPath = "APIBrowser";
    this.home = ".";
    this.invokerPath = home + "/invoker";
    this.servletContext = "..";
    
    String classRegistryName = classRegistry;
    if (classRegistryName == null) {
       classRegistryName  = context.getInitParameter("classRegistry");
    }
    openClassRegistry(classRegistryName);
    
    this.beansPath = beansPath;
    if (this.beansPath == null) {
      this.beansPath = context.getInitParameter("servicePath");
    }
    
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

  private void openClassRegistry(String registryClassName) throws ServletException {

   
    if (registryClassName != null) {
      try {
        Class<ClassRegistry> registryClass = (Class<ClassRegistry>) Thread.currentThread().getContextClassLoader().loadClass(registryClassName);
        setRegistry(registryClass.newInstance());
      } catch (InstantiationException ex) {
        throw new ServletException("Cann not instantiate class registy " + registryClassName + ". Please check classRegistry property in your web.xml <context-param> section", ex);
      } catch (IllegalAccessException ex) {
        throw new ServletException("Class registry provider not found. Please check classRegistry property in your web.xml <context-param> section", ex);
      } catch (ClassNotFoundException ex) {
        throw new ServletException("Class registry provider not found. Please check classRegistry property in your web.xml <context-param> section", ex);
      }
    }

  }

  /**
   * @return the registry
   */
  public ClassRegistry getRegistry() {
    return registry;
  }

  /**
   * @param registry the registry to set
   */
  public void setRegistry(ClassRegistry registry) {
    this.registry = registry;
  }
}
