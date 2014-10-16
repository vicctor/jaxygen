/*
 * Copyright 2014 Artur.
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
package org.jaxygen.dto;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.dto.properties.PropertyDTO;

/**
 * This class is used to send exception messages as a response to the method
 * calls.
 *
 * The purpose of the exception class it to provide an unified form of passing
 * error information to the various types of clients. The form of the exception
 * is simplified in that meaner it's possible to transfer the most important
 * exception/error information without sending the whole Java exception class
 * information.
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
        private PropertyDTO[] arguments;

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

        public PropertyDTO[] getArguments() {
            return arguments;
        }

        public void setArguments(PropertyDTO[] arguments) {
            this.arguments = arguments;
        }
    }
    ExceptionData exception = new ExceptionData();

    public ExceptionResponse() {
    }

    public ExceptionResponse(String name, String description, PropertyDTO... args) {
        exception.setName(name);
        exception.setDescription(description);
        exception.setArguments(args);
    }

    public ExceptionResponse(Throwable ex, final String message) {
        exception.setName(ex.toString());
        exception.setDescription(message);
        exception.setExceptionClass(ex.getClass().getCanonicalName());
        setArgs(ex);

        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        final PrintWriter printWriter = new PrintWriter(bo);
        ex.printStackTrace(printWriter);
        printWriter.flush();
        exception.setStackTrace(new String(bo.toByteArray(), Charset.defaultCharset()));
    }

    private void setArgs(Throwable ex) {
        try {
            List<PropertyDTO> args = new ArrayList<PropertyDTO>();
            BeanInfo info = Introspector.getBeanInfo(ex.getClass());
            PropertyDescriptor[] descriptor = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : descriptor) {
                if (pd.getReadMethod().isAnnotationPresent(NetAPI.class)) {
                    PropertyDTO arg = new PropertyDTO();
                    arg.setKey(pd.getName());
                    arg.setValue(pd.getReadMethod().invoke(ex));
                    args.add(arg);
                }
            }
            exception.setArguments(args.toArray(new PropertyDTO[args.size()]));
        } catch (IntrospectionException ex1) {
            Logger.getLogger(ExceptionResponse.class.getName()).log(Level.WARNING, "Could not populate bean properties", ex1);
        } catch (IllegalAccessException ex1) {
            Logger.getLogger(ExceptionResponse.class.getName()).log(Level.WARNING, "Could not populate bean properties", ex1);
        } catch (IllegalArgumentException ex1) {
            Logger.getLogger(ExceptionResponse.class.getName()).log(Level.WARNING, "Could not populate bean properties", ex1);
        } catch (InvocationTargetException ex1) {
            Logger.getLogger(ExceptionResponse.class.getName()).log(Level.WARNING, "Could not populate bean properties", ex1);
        }
    }

    public ExceptionData getExceptionData() {
        return exception;
    }

}
