/*
 * Copyright 2012 Artur Keska.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.apibrowser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.ServerException;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.jaxygen.mime.MimeTypeAnalyser;
import org.jaxygen.apibrowser.pages.ClassMethodsPage;
import org.jaxygen.apibrowser.pages.ClassesListPage;
import org.jaxygen.apibrowser.pages.ClassesSnippestPage;
import org.jaxygen.apibrowser.pages.MethodInvokerPage;
import org.jaxygen.apibrowser.pages.Page;

/**
 *
 * @author Artur Keska
 */
public class APIBrowser extends HttpServlet {
  private String classRegistryName = null;
  private String beansPathName = null;
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    classRegistryName = config.getInitParameter("classRegistry");
    beansPathName = config.getInitParameter("servicePath");
  }
  
  

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

  try {

   final String resource = request.getParameter("resource");

   if (resource != null) {
    postResource(resource, response);
   } else {
    renderApiPage(response, request);
   }
  } catch (Exception ex) {
   throw new ServerException("Service error", ex);
  } finally {
   //out.close();
  }
 }

 private InputStream openResourceStream(String resource) {
  InputStream is;
  is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
  return is;
 }

 private void renderApiPage(HttpServletResponse response, HttpServletRequest request) throws InvocationTargetException, IllegalArgumentException, SecurityException, ServletException, NamingException, InstantiationException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchFieldException {
  response.setContentType("text/html");
  Page page;
  final String pageName = request.getParameter("page");
  if (ClassMethodsPage.NAME.equals(pageName)) {
   page = new ClassMethodsPage(getServletContext(), request, classRegistryName, beansPathName);
  } else if (MethodInvokerPage.NAME.equals(pageName)) {
   page = new MethodInvokerPage(getServletContext(), request, classRegistryName, beansPathName);
  } else if (ClassesListPage.NAME.equals(pageName)) {
   page = new ClassesListPage(getServletContext(), request, classRegistryName, beansPathName);
  } else {
   page = new ClassesSnippestPage(getServletContext(), request, classRegistryName, beansPathName);
  }
  response.getWriter().append(page.render());
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

 private void postResource(String resource, HttpServletResponse response) throws IOException {
  InputStream is = null;
  try {
   is = openResourceStream(resource);
   if (is != null) {
    log("Sending resource as stream");
    response.setContentType(MimeTypeAnalyser.getMimeForExtension(resource));
    IOUtils.copy(is, response.getOutputStream());
   }
  } finally {
   if (is != null) {
    is.close();
   }
  }
 }
}
