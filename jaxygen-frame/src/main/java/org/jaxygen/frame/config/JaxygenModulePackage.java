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

import com.google.inject.Module;
import java.util.Set;
import org.jaxygen.frame.scanner.APIScanner;
import org.jaxygen.typeconverter.ConvertersRegistry;

/**
 *
 * @author Artur
 */
public abstract class JaxygenModulePackage implements JaxygenModule {

    private ModuleDescriptor descriptor;

    public JaxygenModulePackage(ModuleDescriptor descriptor) {
        this.descriptor = descriptor;
    }
    
    
    @Override
    public Set<Class<?>> getServices() {
        return APIScanner.findServices(descriptor.getServicesPackage());
    }

    @Override
    public String getServicesBasePath() {
        return descriptor.getServicesPath();
    }

    @Override
    public Set<Class<? extends ConvertersRegistry>> getConverters() {
        return APIScanner.findConverters(descriptor.getConvertersPackage());
    }

    @Override
    public Set<Class<? extends Module>> getGuiceModules() {
        return APIScanner.findGuiceModules(descriptor.getGuiceModulesPackage());
    }
}
