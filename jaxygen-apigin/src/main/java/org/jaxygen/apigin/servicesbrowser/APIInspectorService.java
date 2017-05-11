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

import java.util.stream.Collectors;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.apigin.beaninspector.engine.APIInspector;
import org.jaxygen.apigin.beaninspector.exceptions.InspectionError;
import org.jaxygen.apigin.beaninspector.model.APIDescriptror;
import org.jaxygen.apigin.servicesbrowser.dto.GetModuleServicesRequestDTO;
import org.jaxygen.apigin.servicesbrowser.dto.ModulesListResponseDTO;
import org.jaxygen.frame.config.JaxygenModule;
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
    public ModulesListResponseDTO getModules() {
        ModulesListResponseDTO rc = new ModulesListResponseDTO();
        rc.setModuleNames(JaxygenModulesRegistry.getInstance().stream()
                .map(module -> module.getName())
                .collect(Collectors.toList()));
        return rc;
    }

    @NetAPI
    public APIDescriptror getModuleServices(GetModuleServicesRequestDTO request) throws JaxygenServiceNotFound, InspectionError {
        JaxygenModule module
                = JaxygenModulesRegistry.getInstance().stream()
                        .filter(m -> request.getServiceName().equals(m.getName()))
                        .findFirst()
                        .orElseThrow(() -> new JaxygenServiceNotFound());
        return new APIInspector().inspect(module.getServices(), module.getServicesPrefix());
    }
}
