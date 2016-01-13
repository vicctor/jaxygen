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
package org.jaxygen.objectsbuilder;

/** The factory used by the Jaxygen engine to create a new instance of invoked service classes.
 * 
 * The common usage pattern of this class is replacement of the standard new function with the
 * custom version - like guice. This way one can implement a dependency injection in the application.
 *
 * @author Artur
 */
public class ObjectBuilderFactory {
    private static ObjectBuilder builder = new DefautlObjectBuilder();
    
    public static ObjectBuilder instance() {
        return builder;
    }
    
    public static void configure(ObjectBuilder builder) {
        ObjectBuilderFactory.builder = builder;
    }
}
