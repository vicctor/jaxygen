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
package org.jaxygen.apibroker;

import com.google.inject.Binder;
import org.jaxygen.apibroker.services.AuthorizatonService;
import org.jaxygen.frame.config.JaxygenModulePackage;
import org.jaxygen.jaxygen.jpa.JxJPASetup;

/**
 *
 * @author Artur
 */
public class Module extends JaxygenModulePackage implements com.google.inject.Module{

     static class JPAConfig implements JxJPASetup {
        @Override
        public String getFactoryName() {
            return "test-persistence-unit";
        }
     
    }
    
     @Override
    public void configure(Binder binder) {
        binder.bind(JxJPASetup.class).toInstance(new JPAConfig());
    }
    public Module() {
        super
                .withServicesPackage(AuthorizatonService.class.getPackage())
                .withGuiceModules(Module.class.getPackage())
                .withName("JaxygenAPIBroker");
    }
    
}
