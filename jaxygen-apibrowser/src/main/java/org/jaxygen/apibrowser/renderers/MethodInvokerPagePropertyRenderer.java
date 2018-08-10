/*
 * Copyright 2018 fbrzuzka.
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
package org.jaxygen.apibrowser.renderers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.annotations.HasImplementation;
import org.jaxygen.annotations.HiddenField;
import org.jaxygen.annotations.LeaveEmptyField;
import org.jaxygen.annotations.MandatoryField;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.apibrowser.APIBrowserException;
import org.jaxygen.converters.properties.PropertiesToBeanConverter;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.netservice.html.HTMAnchor;
import org.jaxygen.netservice.html.HTMLDiv;
import org.jaxygen.netservice.html.HTMLElement;
import org.jaxygen.netservice.html.HTMLForm;
import org.jaxygen.netservice.html.HTMLHeading;
import org.jaxygen.netservice.html.HTMLInput;
import org.jaxygen.netservice.html.HTMLLabel;
import org.jaxygen.netservice.html.HTMLOption;
import org.jaxygen.netservice.html.HTMLParagraph;
import org.jaxygen.netservice.html.HTMLSelect;
import org.jaxygen.netservice.html.HTMLTable;
import org.jaxygen.url.UrlQuery;
import org.jaxygen.util.ClassInstanceCreator;
import org.jaxygen.util.ClassTypeUtil;
import org.jaxygen.util.FieldNameComparator;

/**
 *
 * @author fbrzuzka
 */
public class MethodInvokerPagePropertyRenderer {

    private String browserPath;

    public MethodInvokerPagePropertyRenderer(String browserPath) {
        this.browserPath = browserPath;
    }

    private String capitalise(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Method getMethodByName(Class clazz, String methodName) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new APIBrowserException("There is no method '" + methodName + "' in class '" + clazz.getCanonicalName() + "'");
    }

    public String[] renderJSCode(String methodName, Class handerClass, String methodFilter) {
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
            String call = " this.call('" + handerClass.getSimpleName() + "', '"
                    + methodName + "',{";
            String input = "inputType: 'PROPERTIES', " + fieldsInput + " } , onSuccess, onException)}";
            result[0] = function;
            result[1] = call;
            result[2] = input;
        } else {
            String function = "this." + methodName + " = function(onSuccess, onException){";
            String call = " this.call('" + handerClass.getSimpleName() + "', '"
                    + methodName + "',{} , onSuccess, onException);};";
            result[0] = function;
            result[1] = call;
            result[2] = "";
        }
        return result;
    }

    public Method renderMethod(Class handlerClass, final String methodFilter, HTMLDiv pointer, HTMLForm propertiesInputForm, HttpServletRequest request) {
        Method method = getMethodByName(handlerClass, methodFilter);
        Type[] parameters = method.getParameterTypes();

        HTMLTable table = new HTMLTable();
        for (Type type : parameters) {
            HTMLParagraph p = new HTMLParagraph();
            p.append(new HTMLLabel("inputClass=" + ((Class) type).getCanonicalName()));
            pointer.append(p);
            if (type instanceof Class<?>) {
                Class<?> paramClass = (Class<?>) type;
                try {
                    addInputClassParameters(request, table, paramClass, "");
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException ex) {
                    throw new APIBrowserException("Cannot render input properties", ex);
                }
            }
        }
        propertiesInputForm.append(table);

        return method;
    }

    public HTMLElement renderOutputObject(Class<?> paramClass) {
        HTMLElement rc;
        if (paramClass != null) {
            HTMLTable table = new HTMLTable();
            HTMLTable gettersTale = new HTMLTable();
            table.addRow().addColumn(new HTMLHeading(HTMLHeading.Level.H3, new HTMLLabel(paramClass.getCanonicalName())));
            table.addRow().addColumn(gettersTale);
            for (Method getter : paramClass.getMethods()) {
                if (getter.getName().startsWith("get") || getter.getName().startsWith("is")) {
                    final Class<?> returnType = getter.getReturnType();
                    if (ClassTypeUtil.isSimpleResultType(returnType)) {
                        gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()));
                    } else if (ClassTypeUtil.isBoolType(returnType)) {
                        gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()), enumBoolValues());
                    } else if (ClassTypeUtil.isEnumType(returnType)) {
                        gettersTale.addRow().addColumns(new HTMLLabel(getter.getName()), new HTMLLabel(returnType.getSimpleName()), enumValues(returnType));
                    } else if (ClassTypeUtil.isArrayType(paramClass)) {
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

    private HTMLTable.Row addLabeledRow(Field field, HTMLTable table) {
        HTMLTable.Row row = new HTMLTable.Row();
        table.addRow(row);
        List<HTMLLabel> labels = new ArrayList();
        if (field.isAnnotationPresent(MandatoryField.class)) {
            labels.add((HTMLLabel) new HTMLLabel("M").setStyleInfo("color:red").setLabelTooltip("This field is mandatory"));
        }
        if (field.isAnnotationPresent(LeaveEmptyField.class)) {
            labels.add((HTMLLabel) new HTMLLabel("E").setStyleInfo("color:blue").setLabelTooltip("Leave this field empty"));
        }
        HTMLDiv div = new HTMLDiv();
        for (HTMLLabel label : labels) {
            div.append(label);
        }
        row.addColumn(div);
        return row;
    }

    private Method provideGetter(Class<?> clazz, String fieldName) throws NoSuchMethodException {
        try {
            return clazz.getMethod("get" + capitalise(fieldName));
        } catch (NoSuchMethodException ex) {

        }
        try {
            return clazz.getMethod("is" + capitalise(fieldName));
        } catch (NoSuchMethodException ex) {

        }
        String get = "get" + capitalise(fieldName);
        String is = "is" + capitalise(fieldName);
        throw new APIBrowserException("Cannot render page, there is no getter for field: \"" + fieldName + "\". We looked for \"" + get + "\" and for \"" + is + "\"");

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
        Object inputObject = ClassInstanceCreator.createObject(paramClass).getObject();

        List<Field> filteredFields = ClassTypeUtil.getFields(paramClass).stream()
                .filter(m -> !m.isAnnotationPresent(HiddenField.class))
                .collect(Collectors.toList());
        Collections.sort(filteredFields, new FieldNameComparator());

        for (Field field : filteredFields) {
            final String propertyName = field.getName();
            Class<?> paramType = field.getType();
            Method getter = provideGetter(paramClass, propertyName);
            Object defaultValue = "";
            if (getter != null) {
                defaultValue = getter.invoke(inputObject);
            }
            final String counterName = parentFieldName + propertyName + "Size";
            int multiplicity = 0;
            if (request.getParameter(counterName) != null) {
                multiplicity = Integer.parseInt(request.getParameter(counterName));
            }
            if ((HashMap.class.isAssignableFrom(paramType)) || paramType.isAssignableFrom(HashMap.class)) {
                Class<?>[] componentsTypes = ClassTypeUtil.retrieveMapTypes(paramClass, propertyName);
                if (multiplicity == 0) {
                    HTMLTable.Row row = addLabeledRow(field, table);
                    String newKeyFieldName = parentFieldName + propertyName + "[]";
                    renderMapFieldInputRow(request, row, newKeyFieldName, counterName, null, componentsTypes, 0);
                } else {
                    for (int i = 0; i < multiplicity; i++) {
                        HTMLTable.Row row = addLabeledRow(field, table);
                        String newKeyFieldName = parentFieldName + propertyName + "[" + i + "]";
                        renderMapFieldInputRow(request, row, newKeyFieldName, counterName, null, componentsTypes, multiplicity); //TODO: add heredefault value object
                    }
                }

            } else if (paramType.isAssignableFrom(ArrayList.class) || paramType.isAssignableFrom(LinkedList.class) || (List.class).isAssignableFrom(paramType)) {
                Class<?> componentType = ClassTypeUtil.retrieveListType(paramClass, propertyName);
                if (multiplicity == 0) {
                    HTMLTable.Row row = addLabeledRow(field, table);
                    String newFieldName = parentFieldName + propertyName + "[]";
                    renderFieldInputRow(request, row, newFieldName, counterName, null, componentType, 0);
                } else {
                    for (int i = 0; i < multiplicity; i++) {
                        HTMLTable.Row row = addLabeledRow(field, table);
                        String newFieldName = parentFieldName + propertyName + "[" + i + "]";
                        renderFieldInputRow(request, row, newFieldName, counterName, null, componentType, multiplicity); //TODO: add heredefault value object
                    }
                }
            } else if (paramType.isArray()) {
                Class<?> componentType = paramType.getComponentType();
                if (paramType.equals(List.class)) {
                    Type t = paramType.getTypeParameters()[0];
                    componentType = (Class<?>) t;
                }
                if (multiplicity == 0) {
                    HTMLTable.Row row = addLabeledRow(field, table);
                    String newFieldName = parentFieldName + propertyName + "[]";
                    renderFieldInputRow(request, row, newFieldName, counterName, null, componentType, 0);
                } else {
                    for (int i = 0; i < multiplicity; i++) {
                        HTMLTable.Row row = addLabeledRow(field, table);
                        String newFieldName = parentFieldName + propertyName + "[" + i + "]";
                        renderFieldInputRow(request, row, newFieldName, counterName, null, componentType, multiplicity);
                    }
                }
            } else {
                HTMLTable.Row row = addLabeledRow(field, table);
                String newFieldName = parentFieldName + propertyName;
                renderFieldInputRow(request, row, newFieldName, parentFieldName + propertyName, defaultValue, paramType, -1);
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
    private void renderFieldInputRow(HttpServletRequest request, HTMLTable.Row row,
            final String fieldName, final String counterName, Object defaultValue,
            Class<?> paramType, int multiplicity) throws InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

        String propertyName = fieldName;
        row.addColumn(new HTMLLabel(paramType.getSimpleName(), paramType.getCanonicalName()));
        if (propertyName.contains("<impl>")) {
            propertyName = propertyName.substring(propertyName.indexOf("#") + 1);
        }

        String[] splited = propertyName.split("\\.");
        row.addColumn(new HTMLLabel(splited[splited.length - 1], propertyName));

        addPlusAndMinusAnchors(request, row, counterName, multiplicity, fieldName);

        if (multiplicity > 0 || multiplicity == -1) {
            addSpecificInput(request, row, defaultValue, paramType, fieldName);
        }
    }

    private void renderMapFieldInputRow(HttpServletRequest request, HTMLTable.Row row,
            final String fieldName, final String counterName, Object defaultValue,
            Class<?>[] keyValueTypes, int multiplicity) throws InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        Class<?> keyType = keyValueTypes[0];
        Class<?> valueType = keyValueTypes[1];

        String keyInputName = fieldName + "<key>";
        String valueInputName = fieldName + "<value>";
        String propertyName = fieldName;
        row.addColumn(new HTMLLabel("<" + keyType.getSimpleName() + ", " + keyType.getSimpleName() + ">", "<" + keyType.getCanonicalName() + ", " + keyType.getCanonicalName() + ">"));
        row.addColumn(new HTMLLabel(propertyName));
        addPlusAndMinusAnchors(request, row, counterName, multiplicity, propertyName);

        if (multiplicity > 0 || multiplicity == -1) {
            addSpecificInput(request, row, defaultValue, keyType, keyInputName);
            addSpecificInput(request, row, defaultValue, valueType, valueInputName);
        }
    }

    private void addSpecificInput(HttpServletRequest request, HTMLTable.Row row,
            Object defaultValue, final Class<?> paramType, final String propertyName)
            throws InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        if (ClassTypeUtil.isBoolType(paramType)) {
            HTMLSelect select = new HTMLSelect(propertyName);
            select.addOption(new HTMLOption("TRUE", new HTMLLabel("TRUE")));
            select.addOption(new HTMLOption("FALSE", new HTMLLabel("FALSE")));
            row.addColumn(select);
        } else if (paramType.isEnum()) {
            Object[] values = paramType.getEnumConstants();
            renderSelectHTML(request, propertyName, row, values);
        } else if (PropertiesToBeanConverter.isCovertable(paramType)) {
            String parameterName = propertyName + "_Value";
            Object value = request.getParameter(parameterName);
            Object v = value != null ? value : defaultValue;
            row.addColumn(new HTMLInput(propertyName, propertyName, "INPUT_FIELD", v));
        } else if (paramType.isAssignableFrom(Uploadable.class)) {
            row.addColumn(new HTMLInput(HTMLInput.Type.file, propertyName));
        } else if (paramType.isAnnotationPresent(HasImplementation.class)) {
            HasImplementation annot = paramType.getAnnotation(HasImplementation.class);
            Class[] implementations = annot.implementations();

            HTMLTable implTable = new HTMLTable();
            row.addColumn(implTable);

            for (Class im : implementations) {
                String implName = im.getSimpleName();
                String anchorId = propertyName + "<impl>" + im.getSimpleName();
                String canonicalAnchorId = propertyName + "<impl>" + im.getCanonicalName();
                UrlQuery okQuery = new UrlQuery();
                request.getParameterMap().keySet().stream().forEach((key) -> {
                    // we filter properties from other implementations of this field
                    // so we can switch between implementations
                    if (!key.startsWith(propertyName + "<impl>")) {
                        okQuery.add(key, request.getParameter(key));
                    }
                });
                okQuery.add(anchorId, implName);
                HTMLTable.Row r = new HTMLTable.Row();
                r.addColumn(new HTMAnchor(anchorId, "anchor", "" + browserPath + "?" + okQuery.toString(),
                        new HTMLLabel(implName)));
                implTable.addRow(r);

                // if in parameters is implementation key, we add input for that
                if (request.getParameterMap().keySet().contains(anchorId)) {
                    Class<?> selectedClass = im;
                    HTMLTable beanTable = new HTMLTable();
                    r.addColumn(beanTable);
                    try {
                        addInputClassParameters(request, beanTable, selectedClass, canonicalAnchorId + "#");
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException ex) {
                        throw new APIBrowserException(ex);
                    }
                }
            }
        } else {
            HTMLTable beanTable = new HTMLTable();
            row.addColumn(beanTable);
            addInputClassParameters(request, beanTable, paramType, propertyName + ".");
        }
    }

    private void renderSelectHTML(HttpServletRequest request, final String propertyName, HTMLTable.Row row, Object[] values) {
        HTMLSelect select = new HTMLSelect(propertyName);
        String parameterName = propertyName + "_Value";
        Object value = request.getParameter(parameterName);
        for (Object obj : values) {
            String name = obj.toString();
            HTMLOption htmlOptions = new HTMLOption(name, new HTMLLabel(name));
            boolean isSelected = value != null && name.equals(value.toString());
            htmlOptions.setSelected(isSelected);
            select.addOption(htmlOptions);
        }
        row.addColumn(select);
    }

    public void addExceptionHelp(Type exceptionType, HTMLTable table) {
        HTMLTable.Row row = table.addRow();
        table.addRow(row);
        row.addColumn(new HTMLLabel(exceptionType.toString()));
        NetAPI annotation = (NetAPI) ((Class) exceptionType).getAnnotation(NetAPI.class);
        if (annotation != null) {
            row.addColumn(new HTMLLabel(annotation.description()));
        }
    }

    private void addPlusAndMinusAnchors(HttpServletRequest request, HTMLTable.Row row, final String counterName, int multiplicity, final String propertyName) {

        if (multiplicity >= 0) {
            /*
             * Build the query which adds more inputs to the rendered array
             */
            UrlQuery queryMultiplicityUp = new UrlQuery();
            request.getParameterMap().keySet().stream().forEach((key) -> {
                if (key.equals(counterName)) {
                    queryMultiplicityUp.add(counterName, "" + (multiplicity + 1));
                } else {
                    queryMultiplicityUp.add(key, request.getParameter(key));
                }
            });
            if (queryMultiplicityUp.getParameters().containsKey(counterName) == false) {
                queryMultiplicityUp.add(counterName, "" + (multiplicity + 1));
            }
            row.addColumn(new HTMAnchor("P" + propertyName, "anchor", "" + browserPath + "?" + queryMultiplicityUp.toString(),
                    new HTMLLabel("+")));
        } else {
            row.addColumn(new HTMLLabel(""));
        }
        if (multiplicity >= 1) {
            /*
             * Build the query which removes one input from the rendered array
             */
            UrlQuery queryMultiplicityDown = new UrlQuery();
            request.getParameterMap().keySet().stream().forEach((key) -> {
                if (key.equals(counterName)) {
                    queryMultiplicityDown.add(counterName, "" + (multiplicity - 1));
                } else {
                    queryMultiplicityDown.add(key, request.getParameter(key));
                }
            });

            row.addColumn(new HTMAnchor("M" + propertyName, "anchor", "" + browserPath + "?" + queryMultiplicityDown.toString(),
                    new HTMLLabel("-")));
        } else {
            row.addColumn(new HTMLLabel(""));
        }
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

}
