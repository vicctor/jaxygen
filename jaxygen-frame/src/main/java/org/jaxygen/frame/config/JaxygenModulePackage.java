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
import java.util.Collections;
import java.util.Set;
import org.jaxygen.frame.entrypoint.JaxygenApplicationInitialsationError;
import org.jaxygen.frame.scanner.APIScanner;
import org.jaxygen.typeconverter.ConvertersRegistry;

/**
 *
 * @author Artur
 */
public abstract class JaxygenModulePackage implements JaxygenModule {

    private ModuleDescriptor descriptor = new ModuleDescriptor();

    public JaxygenModulePackage(ModuleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public JaxygenModulePackage() {
    }

    protected final JaxygenModulePackage withServicesPackage(Package servicesPackage) {
        descriptor.setServicesPackage(servicesPackage);
        return this;
    }

    protected final JaxygenModulePackage withConverters(Package convertersPackage) {
        descriptor.setConvertersPackage(convertersPackage);
        return this;
    }

    protected final JaxygenModulePackage withGuiceModules(Package guiceModulesPackage) {
        descriptor.setGuiceModulesPackage(guiceModulesPackage);
        return this;
    }

    @Override
    public Set<Class<?>> getServices() {
        if (descriptor.getServicesPackage() != null) {
            System.out.println("Get services from package: " + descriptor.getServicesPackage());
            return APIScanner.findServices(descriptor.getServicesPackage());
        } else {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public String getServicesPrefix() {
        return descriptor.getServicesPrefix();
    }

    @Override
    public Set<Class<? extends ConvertersRegistry>> getConverters() {
        if (descriptor.getConvertersPackage() != null) {
            return APIScanner.findConverters(descriptor.getConvertersPackage());
        } else {
            return Collections.EMPTY_SET;
        }

    }

    @Override
    public Set<Class<? extends Module>> getGuiceModules() {
        if (descriptor.getGuiceModulesPackage() != null) {
            return APIScanner.findGuiceModules(descriptor.getGuiceModulesPackage());
        } else {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public void onInit() throws JaxygenApplicationInitialsationError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
