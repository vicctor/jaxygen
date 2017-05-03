/*
 * Copyright 2017 Artur.
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
package org.jaxygen.frame.config;

/**
 *
 * @author Artur
 */
@lombok.Data
public class ServicesPackageDescriptor extends ModuleDescriptor{
    public final static String SAME_AS_PATH = null;

    /** Class path base, the base will be removed from the beginning of the 
     * service entrypoint URL name.
     * 
     * E.g if your class name is foo.blah.C3P0
     * and you specify the classpath base as foo, then, resulting URL will be
     * invoker/blah/C3P0
     * 
     */
    private String packageBase = SAME_AS_PATH;
}
