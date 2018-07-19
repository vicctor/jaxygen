package org.jaxygen.apibrowser.pages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jaxygen.apibrowser.renderers.MethodInvokerPagePropertyRenderer;
import org.jaxygen.converters.json.JsonHRResponseConverter;
import org.jaxygen.dto.Downloadable;
import org.jaxygen.netservice.html.*;

/**
 *
 * @author artur
 */
public class MethodInvokerPage extends Page {

    public static final String NAME = "MethodInvokerPage";

    private final MethodInvokerPagePropertyRenderer renderer = new MethodInvokerPagePropertyRenderer(this.browserPath);

    public MethodInvokerPage(ServletContext context,
            HttpServletRequest request, String classRegistry, String beansPath) throws NamingException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException, ServletException, NoSuchFieldException {
        super(context, request, classRegistry, beansPath);
        final String className = request.getParameter("className");
        final String method = request.getParameter("methodName");
        renderClassForm(request, className, method);
    }

    private static String removePathContextFromClassName(final String className, final String beansPath) {
        String simpleClassname = className.substring(beansPath.length());
        if (simpleClassname.startsWith(".")) {
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

        HTMLDiv inputJsonDiv = new HTMLDiv();
        HTMLInput inputJsonButton = new HTMLInput();
        inputJsonButton.setType(HTMLInput.Type.button);
        inputJsonButton.setValue("Read input json");
        inputJsonButton.setAttribute("onClick", "readInputJsonFunction()");
        inputJsonDiv.append(
                new HTMLLabel("input json"),
                new HTMLLabel("  "),
                new HTMLInput(HTMLInput.Type.text, "inputJson", "inputJson", "inputJson", ""),
                inputJsonButton);

        HTMLForm propertiesInputForm = new HTMLForm("submitForm");
        //propertiesInputForm.setMethod(HTMLForm.Action.post);
        //propertiesInputForm.setAction(invokerPath + "/" + simpleClassname + "/" + methodFilter);
        propertiesInputForm.setEnctype("multipart/form-data");

        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "className", classFilter);
        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "methodName", methodFilter);
        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "outputType", JsonHRResponseConverter.NAME);

//        propertiesInputForm.appendInput(HTMLInput.Type.hidden, "inputType", "PROPERTIES");
        HTMLSelect inputTypeSelect = new HTMLSelect("inputType");
        HTMLOption prop2jsonsOption = new HTMLOption("PROP2JSON", new HTMLLabel("PROP2JSON"));
        prop2jsonsOption.setSelected(false);
        inputTypeSelect.addOption(prop2jsonsOption);
        HTMLOption propertiesOption = new HTMLOption("PROPERTIES", new HTMLLabel("PROPERTIES"));
        propertiesOption.setSelected(true);
        inputTypeSelect.addOption(propertiesOption);
        propertiesInputForm.append(inputTypeSelect);

        Class handerClass = Thread.currentThread().getContextClassLoader().loadClass(classFilter);
        Method method = renderer.renderMethod(handerClass, methodFilter, pointer, propertiesInputForm, request);
        final Class resultClass = method.getReturnType();
        Type[] exceptions = method.getExceptionTypes();

        for (Type exceptionType : exceptions) {
            renderer.addExceptionHelp(exceptionType, exceptionsTable);
        }

        propertiesInputForm.appendInput(HTMLInput.Type.submit, "submit", null);
        HTMLDiv mainDiv = new HTMLDiv("mainDiv");

        mainDiv.append(pointer);
        mainDiv.append(inputJsonDiv);
        mainDiv.append(propertiesInputForm);
        mainDiv.append((HTMLElement) () -> {
            StringBuilder sb = new StringBuilder("<script type='text/javascript'>");
            sb.append("window.addEventListener('load', function () {\n")
                    .append("  var form = document.getElementById('submitForm');\n")
                    .append("\n")
                    .append("  form.addEventListener('submit', function (event) {\n")
                    .append("    event.preventDefault();\n")
                    .append("    sendData();\n")
                    .append("  });\n")
                    .append("\n")
                    .append("  handleAnchors(form);\n")
                    .append("  registerCopyButtonEventListener(form);\n")
                    .append("  registerUpdateShareLink(form);\n")
                    .append("\n")
                    .append("});\n");
            sb.append("</script>");

            return sb.toString();
        });
        mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Share this page")));
        mainDiv.append(new HTMLInput(HTMLInput.Type.text, "share it", "share_it", "share_it", "change sth in upper form"));
        mainDiv.append(new HTMLInput(HTMLInput.Type.button, "copyButton", "copyButton", "copyButton", "Copy"));
        mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Return type")));
        mainDiv.append(renderer.renderOutputObject(resultClass));
        mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Exceptions thrown by the method")));
        mainDiv.append(exceptionsTable);

        Page page = this;
        // append script responsible for saving files
        page.append((HTMLElement) () -> "<script type='application/ecmascript' async src='js/FileSaver.js'></script>\n");
        page.append((HTMLElement) () -> "<script type='application/ecmascript' async src='js/AnchorUpdater.js'></script>\n");
        page.append((HTMLElement) () -> "<script type='application/ecmascript' async src='js/shareThisPage.js'></script>\n");
        page.append((HTMLElement) () -> "<script type='application/ecmascript' async src='js/jquery.min.js'></script>\n");
        page.append((HTMLElement) () -> "<script type='application/ecmascript' async src='js/formArrayToJSON.js'></script>\n");
        page.append((HTMLElement) () -> "<script type='application/ecmascript' async src='js/jsonToFormData.js'></script>\n\n");

        String pageHrefEnding = "?page=" + NAME + "&className=" + classFilter + "&methodName=" + methodFilter;

        // append script responsible for sending data to service
        page.append((HTMLElement) () -> {
            StringBuilder sb = new StringBuilder()
                    .append("<script type='text/javascript'>\n")
                    .append("function show_hideRequestJSON() {\n")
                    .append("   var requestJSONParagraph = document.getElementById('requestJSON');\n")
                    .append("   if(requestJSONParagraph.innerHTML==='{ ... }'){\n")
                    .append("     var requestJSONArray = $('#submitForm').serializeArray();\n")
                    .append("     var requestJSON = str(toJSONRequest(requestJSONArray));\n")
                    .append("     requestJSONParagraph.innerHTML=requestJSON;\n")
                    .append("   }else{\n")
                    .append("	  requestJSONParagraph.innerHTML='{ ... }';\n")
                    .append("   }\n")
                    .append("}\n")
                    .append("function cutHref() {\n")
                    .append("   return window.location.protocol + '//' + window.location.host + window.location.pathname + '" + pageHrefEnding + "';\n")
                    .append("}\n")
                    .append("function readInputJsonFunction() {\n")
                    .append("   var inputJsonElement = document.getElementById('inputJson');\n")
                    .append("   if(inputJsonElement){\n")
                    .append("       console.log('window.location.href ' + window.location.href)\n")
                    .append("       console.log(inputJsonElement.value)\n")
                    .append("       var href = cutHref()\n")
                    .append("       console.log('cuttedHref ' + href)\n")
                    .append("       var newUrl = href + jsonToFormString(inputJsonElement.value);\n")
                    .append("       console.log(newUrl)\n")
                    .append("       window.location = newUrl\n")
                    .append("   }\n")
                    .append("}\n")
                    .append("  function sendData() {\n")
                    .append("    document.getElementById('requestJSON').innerHTML='{ ... }';\n")
                    .append("    document.getElementById('queryResult').innerHTML='Wait...';\n")
                    .append("    document.getElementById('responseDiv').style.display='block'\n")
                    .append("    document.getElementById('mainDiv').style.display='none';\n")
                    .append("    var form = document.getElementById('submitForm');\n")
                    .append("    var XHR = new XMLHttpRequest();\n")
                    .append("    var FD  = new FormData(form);\n")
                    .append("\n")
                    .append("    XHR.addEventListener('load', function(event) {\n")
                    .append("      if (event.target.getResponseHeader('tabid')) {\n")
                    .append("       sessionStorage.setItem('tabid', event.target.getResponseHeader('tabid'));\n")
                    .append("      }\n");
            if (resultClass.isAssignableFrom(Downloadable.class)) {
                sb.append("      var blob=new Blob([event.target.response], {type:event.target.getResponseHeader('Content-Type')});\n")
                        .append("      var regex = /.*filename=\"(.*)\".*/g;\n")
                        .append("      var contentTypeDispositionHeader = event.target.getResponseHeader('Content-Disposition');\n")
                        .append("      var splited =  regex.exec(contentTypeDispositionHeader);\n")
                        .append("      saveAs(blob, splited[1]);\n");
            } else {
                sb.append("      document.getElementById('queryResult').innerHTML=JSON.stringify(event.target.response, null, 2);\n");
            }
            sb.append("    });\n");
            if (resultClass.isAssignableFrom(Downloadable.class)) {
                sb.append("    XHR.addEventListener('progress', updateProgress, false);\n");
            }
            sb.append("\n")
                    .append("    XHR.open('POST', '").append(invokerPath).append("/").append(simpleClassname).append("/").append(methodFilter).append("');\n");
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
                    .append("     document.getElementById('queryResult').innerHTML='Downloading: ' + percentageComplete + '%';\n")
                    .append("   } else {\n")
                    .append("     document.getElementById('queryResult').innerHTML='Downloading file...';\n")
                    .append("   }\n")
                    .append(" }");
            sb.append("</script>");
            return sb.toString();
        });

        String[] codes = renderer.renderJSCode(methodFilter, handerClass, methodFilter);
        mainDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("JS API code")));
        mainDiv.append(new HTMLPre("js1", codes[0]));
        mainDiv.append(new HTMLPre("js2", codes[1]));
        mainDiv.append(new HTMLPre("js3", codes[2]));

        page.append(mainDiv);
        HTMLDiv responseDiv = new HTMLDiv("responseDiv");
        responseDiv.setAttribute("style", "display:none");
        responseDiv.append((HTMLElement) () -> {
            StringBuilder sb = new StringBuilder()
                    .append("<script type='text/javascript'>")
                    .append("function goBack() {\n")
                    .append("   document.getElementById('mainDiv').style.display='block';\n")
                    .append("   document.getElementById('responseDiv').style.display='none'\n")
                    .append("}")
                    .append("</script>");
            return sb.toString();
        });
        //Buttons-------
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
        //---------------

        //@@ Request part @@@@@@@@@@@@@@4
        responseDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Request")));

        HTMLInput showJSONrequestButton = new HTMLInput();
        showJSONrequestButton.setType(HTMLInput.Type.button);
        showJSONrequestButton.setValue("Show JSON request");
        showJSONrequestButton.setAttribute("onClick", "show_hideRequestJSON()");
        responseDiv.append(showJSONrequestButton);

        HTMLHeading noteHeader = new HTMLHeading(HTMLHeading.Level.H4, new HTMLPre("Note: \nMaps in JSON request below are rendered incorrectly. It will be fixed soon."));
        responseDiv.append(noteHeader);

        HTMLParagraph requestJSON = new HTMLParagraph("requestJSON");
        requestJSON.setAttribute("style", "white-space:pre-wrap;font-family:monospace");
        responseDiv.append(requestJSON);
        //@@@@@@@@@@@@@@@@@@@@@@@@@

        //@@ Response part @@@@@@@@@@@@@@        
        responseDiv.append(new HTMLHeading(HTMLHeading.Level.H2, new HTMLLabel("Response")));

        HTMLParagraph responseQueryResult = new HTMLParagraph("queryResult");
        responseQueryResult.setAttribute("style", "white-space:pre-wrap;font-family:monospace");
        responseDiv.append(responseQueryResult);

        page.append(responseDiv);
    }

}
