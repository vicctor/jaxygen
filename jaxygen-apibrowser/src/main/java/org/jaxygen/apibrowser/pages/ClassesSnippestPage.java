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
package org.jaxygen.apibrowser.pages;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.invoker.ClassRegistry;
import org.jaxygen.netservice.html.HTMAnchor;
import org.jaxygen.netservice.html.HTMLDiv;
import org.jaxygen.netservice.html.HTMLElement;
import org.jaxygen.netservice.html.HTMLLabel;
import org.jaxygen.netservice.html.HTMLParagraph;
import org.jaxygen.netservice.html.HTMLTable;
import org.jaxygen.url.UrlQuery;

/**
 *
 * @author artur
 */
public class ClassesSnippestPage extends Page {

 public static final String NAME = "ClassesSnippestPage";
 private final String browserPath;

 public ClassesSnippestPage(ServletContext context, HttpServletRequest request) throws ServletException {
  super(context);
  browserPath = request.getContextPath() + "/APIBrowser";
  append(renderClassesList());
 }

 /**
  * Render full list of classes
  *
  * @param securityProvider
  * @param classFilter
  * @param methodFilter
  * @param output
  * @return
  */
 private HTMLElement renderClassesList() {
  HTMLElement rc;
  int i;
  if (registry != null) {
   HTMLTable table = new HTMLTable();
   table.setCSSClassName("jaxygen-classes-snipest");

   table.getHeader().createColumns("className", "Description", "Methods");
   boolean even = false;
   for (Class c : registry.getRegisteredClasses()) {
    HTMLTable.Row row = new HTMLTable.Row();
    row.setCSSClassName("jaxygen-row-" + (even ? "even" : "odd"));
    even = !even;
    UrlQuery showClassMethodsQuery = new UrlQuery();
    showClassMethodsQuery.add("page", ClassMethodsPage.NAME);
    showClassMethodsQuery.add("className", c.getCanonicalName());

    row.addColumn(new HTMAnchor("?" + showClassMethodsQuery, new HTMLLabel(c.getSimpleName())));
    if (c.isAnnotationPresent(NetAPI.class)) {
     NetAPI netApi = (NetAPI) c.getAnnotation(NetAPI.class);
     if (netApi != null && netApi.description() != null) {
      row.addColumn(new HTMLLabel(netApi.description()));
     }
    } else {
     row.addColumn(new HTMLLabel("-------"));
    }

   
    row.addColumn(renderMethodReferences(c));

    table.addRow(row);
   }
   rc = table;
  } else {
   rc = new HTMLLabel("Please configure servicePath context-param in yout web.xml file. It must point to " + ClassRegistry.class.getCanonicalName() + " interface implementation");
  }

  return rc;
 }

 private HTMLElement renderMethodReferences(Class clazz) {
  List<HTMAnchor> anchors = new ArrayList<HTMAnchor>();
  for (Method method : clazz.getMethods()) {
   NetAPI netApi = method.getAnnotation(NetAPI.class);
   if (netApi != null) {
    final String className = clazz.getCanonicalName();
    final String methodName = method.getName();
    boolean show = true;

    UrlQuery showMethodQuery = new UrlQuery();
    showMethodQuery.add("page", MethodInvokerPage.NAME);
    showMethodQuery.add("className", className);
    showMethodQuery.add("methodName", methodName);
    showMethodQuery.add("getForm", className + "." + methodName);
    if (show) {
     anchors.add(new HTMAnchor(browserPath + "?" + showMethodQuery, new HTMLLabel(methodName)));
    }
   }
  }
  
  HTMLDiv methodsTable = new HTMLDiv();
  HTMLLabel space = new HTMLLabel(" ");
  for (HTMLElement e : anchors) {
   methodsTable.append(e,space);
  }

  return methodsTable;
 }
}
