package org.jaxygen.apibrowser.pages;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.converters.json.JsonHRResponseConverter;
import org.jaxygen.converters.properties.PropertiesToBeanConverter;
import org.jaxygen.dto.Downloadable;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.netservice.html.*;
import org.jaxygen.url.UrlQuery;

/**
 *
 * @author artur
 */
public class MethodInvokerPage extends Page {

  public static final String NAME = "MethodInvokerPage";

  public MethodInvokerPage(ServletContext context,
          HttpServletRequest request, String classRegistry, String beansPath) throws NamingException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException, ServletException, NoSuchFieldException {
    super(context, request, classRegistry, beansPath);
    final String className = request.getParameter("className");
    final String method = request.getParameter("methodName");

    renderClassForm(request, className, method);
  }

  private void debug(final String message) {
    System.out.println(message);
  }

  private boolean isSimpleResultType(final Class<?> returnType) {
    return returnType.isPrimitive() || returnType.equals(Integer.class) || returnType.equals(Double.class) || returnType.equals(Float.class) || returnType.equals(String.class) || returnType.equals(double.class) || returnType.equals(float.class);
  }
  
  private static String removePathContextFromClassName(final String className, final String beansPath) {
      String simpleClassname = className.substring(beansPath.length());
      if (className.startsWith(".")) {
          simpleClassname = simpleClassname.substring(1);
      }
      return simpleClassname;
  }

  private void renderClassForm(HttpServletRequest request, final String classFilter, final String methodFilter)
          throws NamingException, IllegalArgumentException, SecurityException,
          InstantiationException, IllegalAccessException,
          InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException, NoSuchFieldException {

    final String simpleClassname = removePathContextFromClassName(classFilter, beansPath);
    HTMLTable exceptionsTable = new HTMLTable();
    exceptionsTable.getHeader().addColumn(new HTMLTable.HeadColumn(new HTMLLabel("Exception name")));
    exceptionsTable.getHeader().addColumn(new HTMLTable.HeadColumn(new HTMLLabel("Description")));
    exceptionsTable.setAttribute("border", "1");

    HTMLDiv pointer = new HTMLDiv();
    pointer.append(new HTMLLabel("className=" + classFilter),
            new HTMLLabel("  "),
            new HTMLLabel("methodName=" + methodFilter));

    HTMLForm propertiesInputForm = new HTMLForm("submitForm");
    //propertiesInputForm.setMethod(HTMLForm.Action.post);
    //propertiesInputForm.setAction(invokerPath + "/" + simpleClassname + "/" + methodFilter);
    propertiesInputForm.setEnctype("multipart/form-data");

    propertiesInputForm.appendInput(HTMLInput.Type.hidden, "className", classFilter);
    propertiesInputForm.appendInput(HTMLInput.Type.hidden, "methodName", methodFilter);
    propertiesInputForm.appendInput(HTMLInput.Type.hidden, "inputType", "PROPERTIES");
    propertiesInputForm.appendInput(HTMLInput.Type.hidden, "outputType", JsonHRResponseConverter.NAME);

    Class handerClass = Thread.currentThread().getContextClassLoader().loadClass(classFilter);
    Class resultType = null;

    for (Method method : handerClass.getMethods()) {
      if (method.getName().equals(methodFilter)) {
        resultType = method.getReturnType();
        Type[] parameters = method.getParameterTypes();

        HTMLTable table = new HTMLTable();
        for (Type type : parameters) {
          HTMLParagraph p = new HTMLParagraph();
          p.append(new HTMLLabel("inputClass=" + ((Class) type).getCanonicalName()));
          pointer.append(p);
          if (type instanceof Class<?>) {
            Class<?> paramClass = (Class<?>) type;
            addInputClassParameters(request, table, paramClass, "");
          }
        }
        propertiesInputForm.append(table);
        Type[] exceptions = method.getExceptionTypes();

        for (Type exceptionType : exceptions) {
          addExceptionHelp(exceptionType, exceptionsTable);
        }
      }
    }
    final Class resultClass = resultType;

    propertiesInputForm.appendInput(HTMLInput.Type.submit, "submit", null);
    HTMLDiv mainDiv = new HTMLDiv("mainDiv");

    mainDiv.append(pointer);
    mainDiv.append(propertiesInputForm);
    mainDiv.append((HTMLElement) () -> {
      StringBuilder sb = new StringBuilder("<script type=\"text/javascript\">");
      sb.append("window.addEventListener(\"load\", function () {\n")
              .append("  var form = document.getElementById(\"submitForm\");\n")
              .append("\n")
              .append("  form.addEventListener(\"submit\", function (event) {\n")
              .append("    event.preventDefault();\n")
              .append("    sendData();\n")
              .append("  });\n")
              .append("\n")
              .append("  handleAnchors(form);\n")
              .append("\n")
              .append("});\n");
      sb.append("</script>");

      return sb.toString();
    });
    mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Return type")));
    mainDiv.append(renderOutputObject(resultType));
    mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Exceptions thrown by the method")));
    mainDiv.append(exceptionsTable);

    Page page = this;
    // append script responsible for saving files
    page.append((HTMLElement) () -> "<script type=\"application/ecmascript\" async src=\"js/FileSaver.js\"></script>");
    page.append((HTMLElement) () -> "<script type=\"application/ecmascript\" async src=\"js/AnchorUpdater.js\"></script>");
    // append script responsible for sending data to service
    page.append((HTMLElement) () -> {
      StringBuilder sb = new StringBuilder("<script type=\"text/javascript\">");
      sb.append("  function sendData() {\n")
              .append("  document.getElementById(\"queryResult\").innerHTML='Wait...';\n")
              .append("  document.getElementById(\"responseDiv\").style.display='block'\n")
              .append("  document.getElementById(\"mainDiv\").style.display='none';\n")
              .append("  var form = document.getElementById(\"submitForm\");\n")
              .append("    var XHR = new XMLHttpRequest();\n")
              .append("    var FD  = new FormData(form);\n")
              .append("\n")
              .append("    XHR.addEventListener(\"load\", function(event) {\n")
              .append("      if (event.target.getResponseHeader('tabid')) {\n")
              .append("       sessionStorage.setItem('tabid', event.target.getResponseHeader('tabid'));\n")
              .append("      }\n");
      if (resultClass.isAssignableFrom(Downloadable.class)) {
        sb.append("      var blob=new Blob([event.target.response], {type:event.target.getResponseHeader('Content-Type')});\n")
                .append("      var regex = /.*filename=\"(.*)\".*/g;\n")
                .append("      saveAs(blob, regex.exec(event.target.getResponseHeader('Content-Disposition'))[1]);\n");
      } else {
        sb.append("      document.getElementById(\"queryResult\").innerHTML=JSON.stringify(event.target.response, null, 2);\n");
      }
      sb.append("    });\n");
      if (resultClass.isAssignableFrom(Downloadable.class)) {
        sb.append("    XHR.addEventListener(\"progress\", updateProgress, false);\n");
      }
      sb.append("\n")
              .append("    XHR.open(\"POST\", \"").append(invokerPath).append("/").append(simpleClassname).append("/").append(methodFilter).append("\");\n");
      if (resultClass.isAssignableFrom(Downloadable.class)) {
        sb.append("    XHR.responseType='arraybuffer';\n");
      } else {
        sb.append("    XHR.responseType='json';\n");
      }
      sb.append("\n")
              .append("    if (sessionStorage.getItem('tabid')) {\n")
              .append("       XHR.setRequestHeader('tabid', sessionStorage.getItem('tabid'));\n")
              .append("    }\n")
              .append("    XHR.send(FD);\n")
              .append("  }\n");
      sb.append("  function updateProgress(event) {\n")
              .append("   if (event.lengthComputable) {\n")
              .append("     var percentageComplete = event.loaded*100 / event.total;\n")
              .append("     document.getElementById(\"queryResult\").innerHTML='Downloading: ' + percentageComplete + '%';\n")
              .append("   } else {\n")
              .append("     document.getElementById(\"queryResult\").innerHTML='Downloading file...';\n")
              .append("   }\n")
              .append(" }");
      sb.append("</script>");
      return sb.toString();
    });

    String[] codes = getJSCode(methodFilter, handerClass, methodFilter);
    mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("JS API code")));
    mainDiv.append(new HTMLPre("js1", codes[0]));
    mainDiv.append(new HTMLPre("js2", codes[1]));
    mainDiv.append(new HTMLPre("js3", codes[2]));

    page.append(mainDiv);
    HTMLDiv responseDiv = new HTMLDiv("responseDiv");
    responseDiv.setAttribute("style", "display:none");
    responseDiv.append((HTMLElement) () -> {
      StringBuilder sb = new StringBuilder("<script type=\"text/javascript\">");
      sb.append("function goBack() {\n")
              .append("   document.getElementById(\"mainDiv\").style.display='block';\n")
              .append("   document.getElementById(\"responseDiv\").style.display='none'\n")
              .append("}");
      sb.append("</script>");
      return sb.toString();
    });
    HTMLInput backButton = new HTMLInput();
    backButton.setType(HTMLInput.Type.button);
    backButton.setValue("Back");
    backButton.setAttribute("onClick", "goBack()");
    responseDiv.append(backButton);
    HTMLInput retryButton = new HTMLInput();
    retryButton.setType(HTMLInput.Type.button);
    retryButton.setValue("Resend request");
    retryButton.setAttribute("onClick", "sendData()");
    responseDiv.append(retryButton);
    HTMLParagraph responseQueryResult = new HTMLParagraph("queryResult");
    responseQueryResult.setAttribute("style", "white-space:pre-wrap;font-family:monospace");
    responseDiv.append(responseQueryResult);

    page.append(responseDiv);
  }

  private String[] getJSCode(String methodName, Class handerClass, String methodFilter) {
    String[] result = new String[4];
    result[3] = "";
    String fields = "";
    String fieldsInput = "";
    for (Method method : handerClass.getMethods()) {
      if (method.getName().equals(methodFilter)) {
        Type[] parameters = method.getParameterTypes();

        for (Type type : parameters) {
          if (type instanceof Class<?>) {
            Class<?> paramClass = (Class<?>) type;
            for (Method setter : paramClass.getMethods()) {
              if (setter.getName().startsWith("set") && !"set".equals(setter.getName())) {
                final String fieldName = setter.getName().substring(3);
                if (fieldName != null) {
                  result[3] = "ok";
                }
                String propertyName = fieldName.substring(0, 1).toLowerCase()
                        + fieldName.substring(1);
                fields += propertyName + ", ";
                fieldsInput += propertyName + ": " + propertyName + ", ";

              }
            }
          }
        }

      }
    }

    if (result[3].equals("ok")) {
      fieldsInput = fieldsInput.substring(0, fieldsInput.length() - 2);
      String function = "this." + methodName + " = function(" + fields + "onSuccess, onException){";
      String call = " this.call(\"" + handerClass.getSimpleName() + "\", \""
              + methodName + "\",{";
      String input = "inputType: \"PROPERTIES\", " + fieldsInput + " } , onSuccess, onException)}";
      result[0] = function;
      result[1] = call;
      result[2] = input;
    } else {
      String function = "this." + methodName + " = function(onSuccess, onException){";
      String call = " this.call(\"" + handerClass.getSimpleName() + "\", \""
              + methodName + "\",{} , onSuccess, onException);};";
      result[0] = function;
      result[1] = call;
      result[2] = "";
    }
    return result;
  }

  private HTMLElement renderOutputObject(Class<?> paramClass) {
    HTMLElement rc;
    if (paramClass != null) {
      HTMLTable table = new HTMLTable();
      HTMLTable gettersTale = new HTMLTable();
      table.addRow().addColumn(new HTMLHeading(HTMLHeading.Level.H3, new HTMLLabel(paramClass.getCanonicalName())));
      table.addRow().addColumn(gettersTale);
      for (Method getter : paramClass.getMethods()) {
        if (getter.getName().startsWith("get") || getter.getName().startsWith("is")) {
          final Class<?> returnType = getter.getReturnType();
          if (isSimpleResultType(returnType)) {
            gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()));
          } else if (isBoolType(returnType)) {
            gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()), enumBoolValues());
          } else if (isEnumType(returnType)) {
            gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()), enumValues(returnType));
          } else if (isArrayType(paramClass)) {
            gettersTale.addRow().addColumns(new HTMLLabel(getter.getName() + "[]"), new HTMLLabel(returnType.getSimpleName()));
          } else if (returnType.equals(List.class)) {
            gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()));
          }
        }
      }
      rc = table;
    } else {
      rc = new HTMLLabel("void");
    }
    return rc;
  }

  private static HTMLElement enumValues(Class enumeration) {
    HTMLTable table = new HTMLTable();
    HTMLTable.Row row = table.addRow();
    row.addColumn(new HTMLLabel("ENUMS:"));
    for (Object o : enumeration.getEnumConstants()) {
      row.addColumn(new HTMLLabel(o.toString()));
    }
    return table;
  }

  private static HTMLElement enumBoolValues() {
    HTMLTable table = new HTMLTable();
    HTMLTable.Row row = table.addRow();
    row.addColumn(new HTMLLabel("Boolean:"));

    row.addColumn(new HTMLLabel("TRUE"));
    row.addColumn(new HTMLLabel("FALSE"));

    return table;
  }

  private static boolean isEnumType(Class clazz) {
    return clazz.isEnum();
  }

  private static boolean isBoolType(Class clazz) {
    return Boolean.class.equals(clazz) || (clazz != null && clazz.isPrimitive() && "boolean".equals(clazz.getName()));
  }

  private static boolean isArrayType(Class clazz) {
    return clazz.isArray();
  }

  private Class<?> retrieveListType(Class<?> paramClass, String propertyName) {
    Class c = paramClass;
    Field listField = null;
    String name = c.getName();
    while (listField == null || "java.lang.Object".equals(name)) {
      try {
        listField = c.getDeclaredField(propertyName);
      } catch (Exception e) {
        c = c.getSuperclass();
      }
    }
    Type genericPropertyType = listField.getGenericType();
    
    ParameterizedType propertyType = null;
    while (propertyType == null) {
      if ((genericPropertyType instanceof ParameterizedType)) {
        propertyType = (ParameterizedType) genericPropertyType;
      } else {
        genericPropertyType = ((Class<?>) genericPropertyType).getGenericSuperclass();
      }
    }
    return (Class<?>) propertyType.getActualTypeArguments()[0];
  }

  /**
   * Add list of parameters from bean class passed in the parameter paramClass
   * as a list of rows to the table.
   *
   * @param request
   * @param table
   * @param paramClass
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  private void addInputClassParameters(HttpServletRequest request,
          HTMLTable table, Class<?> paramClass, final String parentFieldName)
          throws InstantiationException, IllegalAccessException,
          InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
    Object inputObject = paramClass.getConstructor().newInstance();
    for (Method setter : paramClass.getMethods()) {
      String setterName = setter.getName();
      if (!"set".equals(setterName) && setter.getName().startsWith("set")) {
        final String fieldName = setter.getName().substring(3);
        Method getter = paramClass.getMethod("get" + fieldName);
        Object defaultValue = "";
        Class<?> paramTypes[] = setter.getParameterTypes();
        Class<?> paramType = null;
        String propertyName = fieldName.substring(0, 1).toLowerCase()
                + fieldName.substring(1);
        if (paramTypes.length > 0) {
          paramType = paramTypes[0];
        }
        if (getter != null) {
          defaultValue = getter.invoke(inputObject);
        }
        if (paramType.isAssignableFrom(HashMap.class)) {
          System.out.println("I am in hash map :)");
        } else if (paramType.isAssignableFrom(ArrayList.class) || paramType.isAssignableFrom(LinkedList.class) || (List.class).isAssignableFrom(paramType)) {
          final String counterName = parentFieldName + propertyName + "Size";
          int multiplicity = 0;
          if (request.getParameter(counterName) != null) {
            multiplicity = Integer.parseInt(request.getParameter(counterName));
          }
          Class<?> componentType = retrieveListType(paramClass, propertyName);
          if (multiplicity == 0) {
            renderFieldInputRow(request, table, parentFieldName + propertyName
                    + "[]", counterName, null, componentType, 0);
          } else {
            for (int i = 0; i < multiplicity; i++) {
              String newFieldName = parentFieldName + propertyName + "[" + i + "]";
              renderFieldInputRow(request, table, newFieldName, counterName, null, componentType, multiplicity); //TODO: add heredefault value object
            }
          }
        } else if (paramType.isArray()) {
          final String counterName = parentFieldName + propertyName + "Size";
          int multiplicity = 0;
          if (request.getParameter(counterName) != null) {
            multiplicity = Integer.parseInt(request.getParameter(counterName));
          }
          Class<?> componentType = paramType.getComponentType();
          if (paramType.equals(List.class)) {
            Type t = paramType.getTypeParameters()[0];
            componentType = (Class<?>) t;
          }
          if (multiplicity == 0) {
            renderFieldInputRow(request, table, parentFieldName + propertyName
                    + "[]", counterName, null, componentType, 0);
          } else {
            for (int i = 0; i < multiplicity; i++) {
              renderFieldInputRow(request, table, parentFieldName + propertyName
                      + "[" + i + "]", counterName, null, componentType, multiplicity);
            }
          }
        } else {
          renderFieldInputRow(request, table, parentFieldName + propertyName,
                  parentFieldName + propertyName, defaultValue, paramType, -1);
        }
      }
    }
  }

  /**
   * Generate row with the parameter input and fill it with the default value
   *
   * @param table
   * @param fieldName
   * @param defaultValue
   * @param paramType
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  private void renderFieldInputRow(HttpServletRequest request, HTMLTable table,
          final String fieldName, final String counterName, Object defaultValue,
          Class<?> paramType, int multiplicity) throws InstantiationException,
          IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
    HTMLTable.Row row = new HTMLTable.Row();
    String propertyName = fieldName;
    row.addColumn(new HTMLLabel(paramType.getCanonicalName()));
    row.addColumn(new HTMLLabel(propertyName));

    /*
     * Build the query which adds more inputs to the rendered array
     */
    UrlQuery queryMultiplicityUp = new UrlQuery();
    request.getParameterMap().keySet().stream().forEach((key) -> {
      if (key.toString().equals(counterName)) {
        queryMultiplicityUp.add(counterName, "" + (multiplicity + 1));
      } else {
        queryMultiplicityUp.add(key.toString(), request.getParameter(key.toString()));
      }
    });
    /*
     * Build the query which removes one input from the rendered array
     */
    UrlQuery queryMultiplicityDown = new UrlQuery();
    request.getParameterMap().keySet().stream().forEach((key) -> {
      if (key.toString().equals(counterName)) {
        queryMultiplicityDown.add(counterName, "" + (multiplicity - 1));
      } else {
        queryMultiplicityDown.add(key.toString(), request.getParameter(key.toString()));
      }
    });
    if (queryMultiplicityUp.getParameters().containsKey(counterName) == false) {
      queryMultiplicityUp.add(counterName, "" + (multiplicity + 1));
    }
    if (multiplicity >= 0) {
      row.addColumn(new HTMAnchor("P" + propertyName, "anchor", "" + browserPath + "?" + queryMultiplicityUp.toString(),
              new HTMLLabel("+")));
    } else {
      row.addColumn(new HTMLLabel(""));
    }
    if (multiplicity >= 1) {
      row.addColumn(new HTMAnchor("M" + propertyName, "anchor", "" + browserPath + "?" + queryMultiplicityDown.toString(),
              new HTMLLabel("-")));
    } else {
      row.addColumn(new HTMLLabel(""));
    }
    table.addRow(row);
    if (multiplicity > 0 || multiplicity == -1) {
      if (isBoolType(paramType)) {
        HTMLSelect select = new HTMLSelect(propertyName);

        select.addOption(new HTMLOption("TRUE", new HTMLLabel("TRUE")));
        select.addOption(new HTMLOption("FALSE", new HTMLLabel("FALSE")));

        row.addColumn(select);
      } else if (paramType.isEnum()) {
        HTMLSelect select = new HTMLSelect(propertyName);
        String parameterName = propertyName + "_Value";
        Object value = request.getParameter(parameterName);
        for (Object obj : paramType.getEnumConstants()) {
          String name = obj.toString();
          HTMLOption htmlOptions = new HTMLOption(name, new HTMLLabel(name));
          boolean isSelected = value != null && name.equals(value.toString());
          htmlOptions.setSelected(isSelected);
          select.addOption(htmlOptions);
        }
        row.addColumn(select);
      } else if (PropertiesToBeanConverter.isCovertable(paramType)) {
        String parameterName = propertyName + "_Value";
        Object value = request.getParameter(parameterName);
        Object v = value != null ? value : defaultValue;
        row.addColumn(new HTMLInput(propertyName, propertyName, "INPUT_FIELD", v));
      } else if (paramType.isAssignableFrom(Uploadable.class)) {
        row.addColumn(new HTMLInput(HTMLInput.Type.file, propertyName));
      } else {
        HTMLTable beanTable = new HTMLTable();
        row.addColumn(beanTable);
        addInputClassParameters(request, beanTable, paramType, fieldName + ".");
      }
    }

  }

  private void addExceptionHelp(Type exceptionType, HTMLTable table) {
    HTMLTable.Row row = table.addRow();
    table.addRow(row);
    row.addColumn(new HTMLLabel(exceptionType.toString()));
    NetAPI annotation = (NetAPI) ((Class) exceptionType).getAnnotation(NetAPI.class);
    if (annotation != null) {
      row.addColumn(new HTMLLabel(annotation.description()));
    }
  }
}
