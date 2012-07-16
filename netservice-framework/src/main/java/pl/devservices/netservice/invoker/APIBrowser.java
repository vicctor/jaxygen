/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.invoker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.devservices.netservice.annotations.NetAPI;
import pl.devservices.netservice.converters.BeanUtil;
import pl.devservices.netservice.html.*;
import pl.devservices.netservice.invoker.apibrowser.ClassesListPage;
import pl.devservices.netservice.invoker.apibrowser.MethodInvokerPage;
import pl.devservices.netservice.invoker.apibrowser.Page;
import pl.devservices.netservice.security.basic.annotations.UserProfile;
import pl.devservices.netservice.url.UrlQuery;

/**
 *
 * @author artur
 */
public class APIBrowser extends HttpServlet {

 private String browserPath;
 private String invokerPath;
 private String beensPath;
 private ClassRegistry registry;

 /**
  * Processes requests for both HTTP
  * <code>GET</code> and
  * <code>POST</code> methods.
  *
  * @param request servlet request
  * @param response servlet response
  * @throws ServletException if a servlet-specific error occurs
  * @throws IOException if an I/O error occurs
  */
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
  response.setContentType("text/html;charset=UTF-8");
  //PrintWriter out = response.getWriter();
  beensPath = getServletContext().getInitParameter("servicePath");
  browserPath = request.getContextPath() + "/APIBrowser";
  invokerPath = request.getContextPath() + "/invoker";
  openClassRegistry();
  try {
   final String className = request.getParameter("className");
   final String method = request.getParameter("methodName");

   response.setContentType("text/html");
       
   Page page;
   if (className == null || method == null) {
    page = new ClassesListPage(getServletContext(), request);
   } else {    
    page = new MethodInvokerPage(registry, getServletContext(), request, className, method);
   }
   response.getWriter().append(page.render());
  } catch (Exception ex) {
   throw new ServerException("Service error", ex);
  } finally {
   //out.close();
  }
 }

 // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
 /**
  * Handles the HTTP
  * <code>GET</code> method.
  *
  * @param request servlet request
  * @param response servlet response
  * @throws ServletException if a servlet-specific error occurs
  * @throws IOException if an I/O error occurs
  */
 @Override
 protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
  processRequest(request, response);
 }

 /**
  * Handles the HTTP
  * <code>POST</code> method.
  *
  * @param request servlet request
  * @param response servlet response
  * @throws ServletException if a servlet-specific error occurs
  * @throws IOException if an I/O error occurs
  */
 @Override
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
  processRequest(request, response);
 }

 /**
  * Returns a short description of the servlet.
  *
  * @return a String containing servlet description
  */
 @Override
 public String getServletInfo() {
  return "Short description";
 }// </editor-fold>

 
private void openClassRegistry() throws ServletException {
  ServletContext context = this.getServletContext();
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
