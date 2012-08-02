package org.jaxygen.security.basic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**The BasicSecurityProviderFactory uses this annotation in order to
 * determinate the method security gourp.
 * 
 * @author Artur Keska
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserProfile {
     String[] name() default "DEFAULT";
}
