package org.jaxygen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks the method as a NetAPI interface. NetAPI method is exposed over the
 * web interface and accessible over the invoker and ApiBrowser interfaces.
 * 
 * @author Artur Keska
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NetAPI {
 /** Description shown in the APIBrowser
  */
 String description() default "";
 /** Method implementation status
  */
 Status status() default Status.Undefined;
 /** Declares since which version of the seftware given method is avaliable
  */
 String version() default "";
}
