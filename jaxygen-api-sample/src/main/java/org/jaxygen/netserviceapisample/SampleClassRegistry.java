/*
 * Copyright 2012 Artur Keska.
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
package org.jaxygen.netserviceapisample;

import java.util.HashSet;
import java.util.Set;
import org.jaxygen.netserviceapisample.business.*;
import org.jaxygen.invoker.ServiceRegistry;

/** This is the entry point of the application.
 * 
 * The ClassRegostry must be added to the web.xml configuration of
 * your web-service. Please visit web.xml file of this project to
 * check how the registry is added.
 *
 * @author Artur Keska
 */
public class SampleClassRegistry implements ServiceRegistry
{
    @Override
    public Set<Class<?>> getRegisteredClasses() {
        Set<Class<?>> clases = new HashSet<>();
        // here are added classes to the registry.
        // Go follow classes to check out the implementation details.
        clases.add(HelloWorld.class);
        clases.add(SessionHandeSample.class);
        clases.add(DTOSample.class);
        clases.add(ValidatorsSample.class);
        clases.add(SecuritySample.class);
        clases.add(FileTransferSample.class);
        clases.add(MethodsStatusesSample.class);
        clases.add(ArrayListRequestSample.class);
        return clases;
    }

    @Override
    public String getPackageBase() {
        return "org.jaxygen.netserviceapisample";
    }
}
