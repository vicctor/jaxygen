/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.dto;

/**
 *
 * @author Artur Keska
 */
public class ExceptionResponse extends Response {

    public static class ExceptionData {

        private String name;
        private String description;
        private String exceptionClass = "UnknownException";

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
    }
}
