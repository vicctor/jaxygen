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

import org.jaxygen.frame.config.JaxygenModulePackage;
import org.jaxygen.frame.config.ModuleDescriptor;
import org.jaxygen.frame.entrypoint.JaxygenApplicationInitialsationError;
import org.jaxygen.netserviceapisample.business.*;

/**
 * This is the entry point of the application.
 *
 * The ClassRegostry must be added to the web.xml configuration of your
 * web-service. Please visit web.xml file of this project to check how the
 * registry is added.
 *
 * @author Artur Keska
 */
public class APISampleModule extends JaxygenModulePackage {
    public APISampleModule() {
        super(ModuleDescriptor.builder()
        .servicesPackage(HelloWorld.class.getPackage())
        .build());
    }

    @Override
    public String getName() {
        return "APISample";
    }
    
    @Override
    public void onInit() throws JaxygenApplicationInitialsationError {
    }

    @Override
    public void onClose() {   
    }
}
