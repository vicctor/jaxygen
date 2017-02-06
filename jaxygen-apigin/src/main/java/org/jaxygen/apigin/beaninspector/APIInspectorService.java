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
package org.jaxygen.apigin.beaninspector;

import java.util.List;
import java.util.stream.Collectors;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.apigin.beaninspector.engine.APIInspector;
import org.jaxygen.apigin.beaninspector.exceptions.InspectionError;
import org.jaxygen.apigin.beaninspector.model.APIDescriptror;
import org.jaxygen.invoker.ClassRegistry;

/**
 *
 * @author Artur
 */
@NetAPI(description = "This service provides an information about all classes registered within the "
        + "applicaiton.")
public class APIInspectorService {

    List<ClassRegistry> registries;

    @NetAPI
    List<APIDescriptror> describe() {
        return registries.stream()
                .map(APIInspectorService::registryToServiceDescriptors)
                .collect(Collectors.toList());
    }

    private static APIDescriptror registryToServiceDescriptors(final ClassRegistry reg) {
        try {
            String packageBase = reg.getPackageBase();
            List<Class> serviceClasses = reg.getRegisteredClasses();

            return new APIInspector().inspect(serviceClasses, packageBase);
        } catch (InspectionError ex) {
            throw new RuntimeException("Could not generate API descriptr", ex);
        }
    }
}
