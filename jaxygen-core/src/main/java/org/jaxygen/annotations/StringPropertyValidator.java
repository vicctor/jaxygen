package org.jaxygen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StringPropertyValidator {
  String regex() default "";
  int minimalLength() default 0;
  int maximalLength() default Integer.MAX_VALUE;
}
