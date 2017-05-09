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
package org.jaxygen.apigin.servicesbrowser;

import java.util.List;
import java.util.stream.Collectors;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.frame.entrypoint.JaxygenModulesRegistry;

/**
 *
 * @author Artur
 */
@NetAPI(description = "This service provides an information about all classes registered within the "
        + "applicaiton.",
        version = "2.0",
        status = Status.Mockup)
public class APIInspectorService {

    //List<ServiceRegistry> registries;
    @NetAPI(description = "Return the list of registered module names")
    public List<String> getModules() {
//        List<String> result = new ArrayList<>();
//        for (JaxygenModule m : JaxygenModulesRegistry.getInstance()) {
//            result.add(m.getName());
//        }
//        return result;
        return JaxygenModulesRegistry.getInstance().stream()
                .map(module -> module.getName())
                .collect(Collectors.toList());
               
    }

    /*@NetAPI
    public List<APIDescriptror> describeModule() {
        return registries.stream()
                .map(APIInspectorService::registryToServiceDescriptors)
                .collect(Collectors.toList());
    }

    private static APIDescriptror registryToServiceDescriptors(final ServiceRegistry reg) {
        try {
            String packageBase = reg.getPackageBase();
            Set<Class<?>> serviceClasses = reg.getRegisteredClasses();

            return new APIInspector().inspect(serviceClasses, packageBase);
        } catch (InspectionError ex) {
            throw new RuntimeException("Could not generate API descriptr", ex);
        }
    }*/
}
