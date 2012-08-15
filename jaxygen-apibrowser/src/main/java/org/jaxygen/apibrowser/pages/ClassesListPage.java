/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.apibrowser.pages;

import org.jaxygen.netservice.html.HTMAnchor;
import org.jaxygen.netservice.html.HTMLElement;
import org.jaxygen.netservice.html.HTMLLabel;
import org.jaxygen.netservice.html.HTMLTable;
import org.jaxygen.netservice.html.HTMLDiv;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.invoker.ClassRegistry;
import org.jaxygen.security.basic.annotations.UserProfile;
import org.jaxygen.url.UrlQuery;

/**
 *
 * @author artur
 */
public class ClassesListPage extends Page {  
  public static final String NAME = "ClassesListPage";
  
  private final String browserPath;

  public ClassesListPage(ServletContext context, HttpServletRequest request) throws ServletException {
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
    if (registry != null) {
      HTMLTable table = new HTMLTable();
      table.getHeader().createColumns("className", "methodName", "Description", "Allowed to");
      for (Class c : registry.getRegisteredClasses()) {
        table.addRows(renderMethodReferences(c));
      }
      rc = table;
    } else {
      rc = new HTMLLabel("Please configure servicePath context-param in yout web.xml file. It must point to " + ClassRegistry.class.getCanonicalName() + " interface implementation");
    }

    return rc;
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
        query.add("className", className);
        query.add("methodName", methodName);
        query.add("getForm", className + "." + methodName);
        if (show) {
          HTMLTable.Row row = new HTMLTable.Row();
          row.addColumn(new HTMAnchor("?page=" + ClassMethodsPage.NAME + "&className=" + className, new HTMLLabel(className)));
          row.addColumn(new HTMAnchor(browserPath + "?" + query, new HTMLLabel(methodName)));
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


}
