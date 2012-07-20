package org.jaxygen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class tells to the HttpInterceptorBeanImplementation that the method
 * parameter class is an query arguments list.
 * 
 * @author Artur Keska
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QueryMessage {

}
