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
import org.jaxygen.netservice.html.*;
import org.jaxygen.security.basic.annotations.UserProfile;
import org.jaxygen.url.UrlQuery;

/**
 * Class shows methods of the class including description of each method
 *
 * @author Artur Keska
 */
public class ClassMethodsPage extends Page {

  public static final String NAME = "ClassMethodsPage";

  public ClassMethodsPage(ServletContext context, HttpServletRequest request, String classRegistry, String beansPath) throws ServletException {
    super(context, request, classRegistry, beansPath);
    final String className = request.getParameter("className");
    super.append(renderClassMethods(className));
  }

  /**
   * Create table containing links to given class methods
   *
   * @param className
   * @return
   */
  private HTMLElement renderClassMethods(final String className) {
    Class clazz = getRegisteredClassByName(className);
    HTMLDiv div = new HTMLDiv();
    HTMLTable methodsTable = new HTMLTable();
    methodsTable.createHeader().createColumns("methodName","Status","Since version","Description");
    methodsTable.addRows(renderMethodReferences(clazz));

    div.append(new HTMLLabel("Class: " + className));
    div.append(methodsTable);
    return div;
  }

  private HTMLTable.Row[] renderMethodReferences(Class clazz) {
    List<HTMLTable.Row> rows = new ArrayList<HTMLTable.Row>();
    for (Method method : clazz.getMethods()) {
      NetAPI netApi = method.getAnnotation(NetAPI.class);
      UserProfile userProfile = method.getAnnotation(UserProfile.class);
      if (netApi != null) {
        final String className = clazz.getCanonicalName();
        final String methodName = method.getName();
        boolean show = true;

        UrlQuery query = new UrlQuery();
        query.add("methodName", methodName);
        query.add("getForm", className + "." + methodName);
        if (show) {
          HTMLTable.Row row = new HTMLTable.Row();
          row.addColumn(new HTMAnchor("?page=" + MethodInvokerPage.NAME + 
                  "&className=" + className + "&methodName=" + methodName, 
                  new HTMLLabel(methodName)));          
          row.addColumn(new HTMLLabel(netApi.status().toString()));
          row.addColumn(new HTMLLabel(netApi.version()));
          row.addColumn(new HTMLLabel(netApi.description()));
          HTMLDiv allowed = new HTMLDiv();
          row.addColumn(allowed);
          if (userProfile != null) {
            for (String dm : userProfile.name()) {
              allowed.append(new HTMLLabel(dm));
            }
          }
          rows.add(row);
        }
      }
    }
    return rows.toArray(new HTMLTable.Row[rows.size()]);
  }

  private Class getRegisteredClassByName(String className) {
    Class rc = null;
    for (Class c : getRegistry().getRegisteredClasses()) {
      if (c.getName().equals(className)) {
        rc = c;
        break;
      }
    }
    return rc;
  }
}
