package org.jaxygen.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NumberPropertyValidator {
    long minValue() default Long.MIN_VALUE;
    long maxValue() default Long.MAX_VALUE;
}
