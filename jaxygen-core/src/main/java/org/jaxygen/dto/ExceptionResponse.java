/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.dto;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 *
 * @author Artur Keska
 */
public class ExceptionResponse extends Response {

  public static class ExceptionData implements Serializable {

    private static final long serialVersionUID = 133887542L;

    private String name;
    private String description;
    private String exceptionClass = "UnknownException";
    private String stackTrace;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getExceptionClass() {
      return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
      this.exceptionClass = exceptionClass;
    }

    public String getStackTrace() {
      return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
      this.stackTrace = stackTrace;
    }
  }
  ExceptionData exception = new ExceptionData();

  public ExceptionResponse() {
  }

  public ExceptionResponse(String name, String description) {
    exception.setName(name);
    exception.setDescription(description);
  }

  public ExceptionResponse(Throwable ex, final String message) {
    exception.setName(ex.toString());
    exception.setDescription(message);
    exception.setExceptionClass(ex.getClass().getCanonicalName());
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final PrintWriter printWriter = new PrintWriter(bo);
    ex.printStackTrace(printWriter);
    printWriter.flush();
    exception.setStackTrace(new String(bo.toByteArray(), Charset.defaultCharset()));
  }

  public ExceptionData getExceptionData() {
    return exception;
  }
}
