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
     * @return Description that will be visible in in browsers.
  */
 String description() default "";
 /** Method implementation status.
     * @return Status of method implementation.
  */
 Status status() default Status.Undefined;
 /** Declares since which version of the software given method is avaliable.
     * @return Version name.
  */
 String version() default "";
}
