package org.jaxygen.http;

import org.jaxygen.converters.xml.XMLDateAdapter;
import org.jaxygen.network.UploadedFile;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jaxygen.exceptions.InvalidRequestParameter;

/**
 * This class implements an interface of accessing HTTP request parameters.
 *
 * @author Artur Keska
 *
 */
public class HttpRequestParser implements HttpRequestParams {

  /**
   *
   */
  private static final long serialVersionUID = -377032102000216172L;
  private HttpServletRequest request;
  private Hashtable<String, UploadedFile> files = new Hashtable<String, UploadedFile>();
  private Hashtable<String, String> parameters = new Hashtable<String, String>();
  private final static DateFormat dateFormater = XMLDateAdapter.dateFormater;
  private HttpFileUploadHandler uploadHandler;

  /**
   * @param request
   * @throws Exception
   */
  public HttpRequestParser(HttpServletRequest request) throws Exception {
    this.request = request;
    process();
  }

  /**
   * @param request
   * @param uploadHandler
   * @throws Exception
   */
  public HttpRequestParser(HttpServletRequest request,
          HttpFileUploadHandler uploadHandler) throws Exception {
    this.request = request;
    this.uploadHandler = uploadHandler;
    process();
  }

  /**
   * @param item
   */
  private void processRegularField(FileItem item) throws UnsupportedEncodingException {
    String name = item.getFieldName();
    String value = item.getString("UTF-8");
    addParameter(name, value);
  }

  /**
   * @param item
   * @throws Exception
   */
  private void processUploadedFile(final FileItem item) throws Exception {
    String fieldName = item.getFieldName();
    String fileName = item.getName();
    String contentType = item.getContentType();
    // boolean isInMemory = item.isInMemory();
    long sizeInBytes = item.getSize();
    // skipp moving files without name - assume as error
    if (fileName.length() > 0) {
      //System.out.println("File upload: " + fieldName + " " + fileName + " "
      //    + contentType + " " + sizeInBytes);
      
      UploadedFile upf = new UploadedFile(item);
      //upf.setFile(uploadedFile);
      upf.setMimeType(contentType);
      upf.setOriginalName(fileName);
      files.put(fieldName, upf);
    }
  }

  /**
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void process() throws Exception {
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    if (isMultipart) {
      // Create a factory for disk-based file items
      FileItemFactory factory = null;
      // check if the handler initialized other repository then default
      if (uploadHandler != null) {
        File repository = uploadHandler.initUpload();
        factory = new DiskFileItemFactory(
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
      }
      if (factory == null) {
        factory = new DiskFileItemFactory();
      }

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);      
      // Parse the request      
      List<FileItem> items = upload.parseRequest(request);
      for (FileItem item : items) {
        if (item.isFormField()) {
          processRegularField(item);
        } else {
          processUploadedFile(item);
        }
      }
    }
    processParameters();
  }

  /**
   *
   */
  private void processParameters() throws UnsupportedEncodingException {
    Enumeration<?> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String name = (String) parameterNames.nextElement();
      Object value = request.getParameter(name);
      addParameter(name, value.toString());
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see pl.xdsnet.util.HttpRequestParams#getAsDate(java.lang.String, boolean)
   */
  @Override
  public Date getAsDate(String paramName, boolean mandatory)
          throws InvalidRequestParameter {
    Date rc = null;
    try {
      String s = null;
      Object v = parameters.get(paramName);
      if (v != null) {
        s = v.toString();
      }
      if (s != null && s.length() > 0) {
        rc = dateFormater.parse(s);
      } else if (mandatory) {
        throw new InvalidRequestParameter("Missing mandatory parameter :" + paramName);
      }
    } catch (Exception e) {
      throw new InvalidRequestParameter("Invalid date format of the parameter " + paramName);
    }
    return rc;
  }

  /*
   * (non-Javadoc)
   *
   * @see pl.xdsnet.util.HttpRequestParams#getAsEnum(java.lang.String,
   * java.lang.Class, boolean)
   */
  @Override
  public Object getAsEnum(String paramName, Class<?> enumClass,
          boolean mandatory) throws InvalidRequestParameter {
    Object rc = null;
    try {
      String value = null;
      Object v = parameters.get(paramName);
      if (v != null) {
        value = v.toString();
      }

      if (value != null && value.length() > 0) {
        Method m = enumClass.getDeclaredMethod("valueOf", value.getClass());
        rc = m.invoke(null, value);
      } else if (mandatory) {
        throw new InvalidRequestParameter("Missing mandatory parameter :" + paramName);
      }
    } catch (Exception e) {
      throw new InvalidRequestParameter(
              "Could not determinalte value of parameter " + paramName + " for enum class " + enumClass.getName());
    }
    return rc;
  }

  @Override
  public Object getAsEnum(String paramName, Class<?> enumClass,
          Object defaultValue) throws InvalidRequestParameter {
    Object rc = getAsEnum(paramName, enumClass, false);
    if (rc == null) {
      rc = defaultValue;
    }
    return rc;
  }

  /*
   * (non-Javadoc)
   *
   * @see pl.xdsnet.util.HttpRequestParams#getAsInt(java.lang.String, int, int,
   * int)
   */
  @Override
  public int getAsInt(String paramName, int min, int max, int defaultValue)
          throws InvalidRequestParameter {
    int rc = defaultValue;
    String valStr = null;
    Object v = parameters.get(paramName);
    if (v != null) {
      valStr = v.toString();
    }
    if (valStr != null && valStr.length() > 0) {
      try {
        rc = Integer.parseInt(valStr);
      } catch (Exception e) {
        throw new InvalidRequestParameter("Value of parameter " + paramName + " is not in valid numerical format");
      }
    }
    return rc;
  }

  /*
   * (non-Javadoc)
   *
   * @see pl.xdsnet.util.HttpRequestParams#getAsInt(java.lang.String, int, int,
   * boolean)
   */
  @Override
  public int getAsInt(String paramName, int min, int max, boolean mandatory)
          throws InvalidRequestParameter {
    int rc = 0;
    Object v = parameters.get(paramName);
    String valStr = null;
    if (v != null) {
      valStr = v.toString();
    }
    if (valStr != null && valStr.length() > 0) {
      try {
        rc = Integer.parseInt(valStr);
      } catch (Exception e) {
        throw new InvalidRequestParameter("Value of parameter " + paramName + " is not in valid numerical format");
      }
    } else if (mandatory) {
      throw new InvalidRequestParameter("Missing mandatory parameter :" + paramName);
    }
    return rc;
  }

  /*
   * (non-Javadoc)
   *
   * @see pl.xdsnet.util.HttpRequestParams#getAsString(java.lang.String, int,
   * int, boolean)
   */
  @Override
  public String getAsString(String paramName, int minLen, int maxLen,
          boolean mandatory) throws InvalidRequestParameter {

    String rc = null;
    Object v = parameters.get(paramName);
    if (v != null) {
      rc = v.toString();
    }
    if (rc == null && mandatory) {
      throw new InvalidRequestParameter("Missing mandatory parameter :" + paramName);
    }
    if (rc != null && rc.length() > maxLen) {
      throw new InvalidRequestParameter("String value of parameter " + paramName + " to long. (maximal size is " + maxLen + ")");
    }
    if (rc != null && rc.length() < minLen) {
      throw new InvalidRequestParameter("String value of parameter " + paramName + " too short. The minilam expected length is " + minLen);
    }
    return rc;
  }

  @Override
  public String getAsString(String paramName, int minLen, int maxLen,
          final String defaultValue) throws InvalidRequestParameter {
    String rc = null;
    Object v = parameters.get(paramName);
    if (v != null) {
      rc = v.toString();
    }
    if (rc == null) {
      rc = defaultValue;
    }
    if (rc != null && rc.length() > maxLen) {
      throw new InvalidRequestParameter("String value of parameter " + paramName + " to long. (maximal size is " + maxLen + ")");
    }
    if (rc != null && rc.length() < minLen) {
      throw new InvalidRequestParameter("String value of parameter " + paramName + " too short. The minilam expected length is " + minLen);
    }
    return rc;
  }

  @Override
  public Map<String, UploadedFile> getFiles() {
    return files;
  }

  /**
   * @param request
   * @param listName
   * @return
   * @throws InvalidRequestParameter
   */
  @SuppressWarnings("unchecked")
  private final static List<String> getIndexedList(HttpServletRequest request,
          String listName) throws InvalidRequestParameter {
    Vector<String> rc = new Vector<String>();
    Enumeration<String> names = request.getParameterNames();
    Pattern p = Pattern.compile(listName + "\\[(\\d+)\\]");

    int maxIndex = -1;
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      Matcher m = p.matcher(name);
      if (m.matches()) {
        int id = Integer.parseInt(m.group(1));
        if (id != -1) {
          if (id > maxIndex) {
            maxIndex = id;
          }
        }
      }
    }
    if (maxIndex > -1) {
      // rc.setSize(maxIndex+1);
      for (int i = 0; i <= maxIndex; i++) {
        String value = request.getParameter(listName + "[" + i + "]");
        if (value.length() > 0) {
          rc.add(i, value);
        }
      }
    }
    return rc;
  }

  /*
   * (non-Javadoc)
   *
   * @see pl.xdsnet.util.HttpRequestParams#getAsListOfInt(java.lang.String)
   */
  @Override
  public List<Integer> getAsListOfInt(String listName)
          throws InvalidRequestParameter {
    Vector<Integer> rc = new Vector<Integer>();
    List<String> sl = getIndexedList(request, listName);
    for (String s : sl) {
      if (s != null) {
        rc.add(Integer.decode(s));
      }
    }
    return rc;
  }

  @Override
  public List<String> getAsListOfStrings(String listName)
          throws InvalidRequestParameter {
    return getIndexedList(request, listName);
  }

  @Override
  public List<?> getAsEnums(String name, Class<?> clazz)
          throws InvalidRequestParameter {
    List<String> names = getAsListOfStrings(name);
    List<Object> rc = new ArrayList<Object>();
    try {
      for (String value : names) {
        if (value != null && value.length() > 0) {
          Method m = clazz.getDeclaredMethod("valueOf", value.getClass());
          Object e = m.invoke(null, value);
          rc.add(e);
        }
      }
    } catch (Exception e) {
      throw new InvalidRequestParameter(name);
    }
    return rc;
  }

  @Override
  public boolean getAsBoolean(String paramName, boolean mandatory)
          throws InvalidRequestParameter {
    String value = getAsString(paramName, 0, 20, mandatory);
    if (value == null) {
      value = "";
    }
    return value.toUpperCase().equals("TRUE");
  }

  @Override
  public boolean getAsBooleanWithDefault(String paramName, boolean defaultVal)
          throws InvalidRequestParameter {
    String value = getAsString(paramName, 0, 20, false);
    if (value == null) {
      value = "";
    }
    boolean rc = defaultVal;
    if (value.length() > 0) {
      rc = value.toUpperCase().equals("TRUE");
    }
    return rc;
  }

  @Override
  public Map<String, String> getParameters() {
    return parameters;
  }

  

  public void dispose() {
    for (UploadedFile f : files.values()) {
      f.dispose();
    }
  }

 private void addParameter(final String name, final String value) throws UnsupportedEncodingException {
  parameters.put(name, value);
 }
}
