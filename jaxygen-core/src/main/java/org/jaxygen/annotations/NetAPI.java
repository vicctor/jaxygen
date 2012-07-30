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
  public enum Status  {
    Placeholder,       /** The method is just a placeholder to keep in mind that probably one needed sych method */
    Mockup,            /** Method is implemened just as a mockup. It gives a result which fullfills the application design rules */
    Nonfunctional,     /** Method is implemented but not functional */
    ReleaseCandidate,  /** Method is implemented and realy for testing */
    GenerallyAvailable /** Method is implemented and accepted by testers team */
  }
  // Description shown in the APIBrowser
 String description() default "";
 // Method implementation status
 Status status = null;
}
