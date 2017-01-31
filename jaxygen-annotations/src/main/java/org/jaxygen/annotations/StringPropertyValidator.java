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
package org.jaxygen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Validates string property over the regular expression and expected length.
 * 
 * @author Artur
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StringPropertyValidator {
    /**
     * 
     * @return Java regexp that the property shall match.
     */
  String regex() default "";
  /**
   * 
   * @return Minimal length of the property.
   */
  int minimalLength() default 0;
  /**
   * 
   * @return Maximal length of the property.
   */
  int maximalLength() default Integer.MAX_VALUE;
}
