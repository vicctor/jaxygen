package org.jaxygen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class tells to the HttpInterceptorBeanImplementation that the 
 * field is an HttpSession and shall be injected by the engine before
 * serivce class method call.
 * 
 * @author Artur Keska
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SessionContext {
}
