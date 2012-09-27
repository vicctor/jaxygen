/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.apibrowser.pages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.converters.properties.PropertiesToBeanConverter;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.netservice.html.*;
import org.jaxygen.url.UrlQuery;

/**
 *
 * @author artur
 */
public class MethodInvokerPage extends Page {

    public static final String NAME = "MethodInvokerPage";
    private final String beensPath;
    private final String browserPath;
    private final String invokerPath;

    public MethodInvokerPage(ServletContext context,
            HttpServletRequest request) throws NamingException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException, ServletException {
        super(context);
        final String className = request.getParameter("className");
        final String method = request.getParameter("methodName");
        this.beensPath = context.getInitParameter("servicePath");
        this.browserPath = request.getContextPath() + "/APIBrowser";
        this.invokerPath = request.getContextPath() + "/invoker";

        renderClassForm(request, className, method);
    }

    private void debug(final String message) {
        System.out.println(message);
    }

    private boolean isSimpleResultType(final Class<?> returnType) {
        return returnType.isPrimitive() || returnType.equals(Integer.class) || returnType.equals(String.class);
    }

    private void renderClassForm(HttpServletRequest request, final String classFilter, final String methodFilter)
            throws NamingException, IllegalArgumentException, SecurityException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException {

        String simpleClassname = classFilter.substring(beensPath.length() + 1);
        HTMLTable exceptionsTable = new HTMLTable();
        exceptionsTable.getHeader().addColumn(new HTMLTable.HeadColumn(new HTMLLabel("Exception name")));
        exceptionsTable.getHeader().addColumn(new HTMLTable.HeadColumn(new HTMLLabel("Description")));
        exceptionsTable.setAttribute("border", "1");

        HTMLDiv pointer = new HTMLDiv();
        pointer.append(new HTMLLabel("className=" + classFilter),
                new HTMLLabel("&"),
                new HTMLLabel("methodName=" + methodFilter));

        HTMLForm propertiesInputForm = new HTMLForm();
        propertiesInputForm.setMethod(HTMLForm.Action.post);
        propertiesInputForm.setAction(invokerPath + "/" + simpleClassname + "/" + methodFilter);
        propertiesInputForm.setEnctype("multipart/form-data");

        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "className", classFilter);
        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "methodName", methodFilter);
        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "inputType", "PROPERTIES");


        Class handerClass = Thread.currentThread().getContextClassLoader().loadClass(classFilter);
        Class resultType = null;

        for (Method method : handerClass.getMethods()) {
            if (method.getName().equals(methodFilter)) {
                resultType = method.getReturnType();
                Type[] parameters = method.getParameterTypes();
                HTMLTable table = new HTMLTable();
                for (Type type : parameters) {
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
        propertiesInputForm.appendInput(HTMLInput.Type.submit, "submit", null);
        Page page = this;
        page.append(pointer);
        page.append(propertiesInputForm);
        page.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Return type")));
        page.append(renderOutputObject(resultType));
        page.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Exceptions thrown by the method")));
        page.append(exceptionsTable);
        String[] codes = getJSCode(methodFilter, handerClass, methodFilter);
        this.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("JS API code")));
        this.append(new HTMLPre("js1", codes[0]));
        this.append(new HTMLPre("js2", codes[1]));
        this.append(new HTMLPre("js3", codes[2]));
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
                            if (setter.getName().startsWith("set")) {
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
                    + methodName + "\",{} , onSuccess, onException)}";
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

    private static boolean isEnumType(Class clazz) {
        return clazz.isEnum();
    }

    private static boolean isArrayType(Class clazz) {
        return clazz.isArray();
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
            InvocationTargetException, NoSuchMethodException {
        Object inputObject = paramClass.getConstructor().newInstance();
        for (Method setter : paramClass.getMethods()) {
            if (setter.getName().startsWith("set")) {
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
                if (paramType.isArray()) {
                    final String counterName = parentFieldName + propertyName + "Size";
                    int multiplicity = 1;
                    if (request.getParameter(counterName) != null) {
                        multiplicity = Integer.parseInt(request.getParameter(counterName));
                    }
                    Class<?> componentType = paramType.getComponentType();
                    if (paramType.equals(List.class)) {
                        Type t = paramType.getTypeParameters()[0];
                        componentType = (Class<?>) t;
                    }
                    for (int i = 0; i < multiplicity; i++) {
                        renderFieldInputRow(request, table, parentFieldName + propertyName
                                + "[" + i + "]", counterName, defaultValue, componentType, multiplicity);
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
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        HTMLTable.Row row = new HTMLTable.Row();
        String propertyName = fieldName;
        row.addColumn(new HTMLLabel(paramType.getCanonicalName()));
        row.addColumn(new HTMLLabel(propertyName));

        /*
         * Build the query which adds more inputs to the rendered array
         */
        UrlQuery queryMultiplicityUp = new UrlQuery();
        for (Object key : request.getParameterMap().keySet()) {
            if (key.toString().equals(counterName)) {
                queryMultiplicityUp.add(counterName, "" + (multiplicity + 1));
            } else {
                queryMultiplicityUp.add(key.toString(), request.getParameter(key.toString()));
            }
        }
        /*
         * Build the query which removes one input from the rendered array
         */
        UrlQuery queryMultiplicityDown = new UrlQuery();
        for (Object key : request.getParameterMap().keySet()) {
            if (key.toString().equals(counterName)) {
                queryMultiplicityDown.add(counterName, "" + (multiplicity - 1));
            } else {
                queryMultiplicityDown.add(key.toString(), request.getParameter(key.toString()));
            }
        }
        if (queryMultiplicityUp.getParameters().containsKey(counterName) == false) {
            queryMultiplicityUp.add(counterName, "" + (multiplicity + 1));
        }

        if (multiplicity > 0) {
            row.addColumn(new HTMAnchor("" + browserPath + "?" + queryMultiplicityUp.toString(),
                    new HTMLLabel("+")));

        } else {
            row.addColumn(new HTMLLabel(""));
        }
        if (multiplicity > 1) {
            row.addColumn(new HTMAnchor("" + browserPath + "?" + queryMultiplicityDown.toString(),
                    new HTMLLabel("-")));
        } else {
            row.addColumn(new HTMLLabel(""));
        }
        table.addRow(row);
        if (paramType.isEnum()) {
            HTMLSelect select = new HTMLSelect(propertyName);
            for (Object name : paramType.getEnumConstants()) {
                select.addOption(new HTMLOption(name.toString(), new HTMLLabel(name.toString())));
            }
            row.addColumn(select);
        } else if (PropertiesToBeanConverter.isCovertable(paramType)) {
            row.addColumn(new HTMLInput(propertyName, defaultValue));
        } else if (paramType.isAssignableFrom(Uploadable.class)) {
            row.addColumn(new HTMLInput(HTMLInput.Type.file, propertyName));
        } else {
            HTMLTable beanTable = new HTMLTable();
            row.addColumn(beanTable);
            addInputClassParameters(request, beanTable, paramType, fieldName + ".");
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
