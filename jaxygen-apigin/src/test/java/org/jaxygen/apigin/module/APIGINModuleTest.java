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
package org.jaxygen.apigin.module;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.jaxygen.apigin.servicesbrowser.APIInspectorService;
import org.jaxygen.apigin.servicesbrowser.ServicesBrowser;
import org.jaxygen.frame.entrypoint.JaxygenEntrypoint;
import org.jaxygen.registry.JaxygenRegistry;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class APIGINModuleTest {
    
    public APIGINModuleTest() {
    }

    @Test
    public void shall_returnAPIInspectorService() throws IOException {       
        // given
        APIGINModule module = new APIGINModule();
        
        // when
        Set<Class<?>> services = module.getServices();
        
        // then
        Assertions.assertThat(services)
                .contains(APIInspectorService.class);
    }
    
     @Test
    public void shall_findAPIGinService() throws IOException {   
        // given
         JaxygenEntrypoint entrypoint = new JaxygenEntrypoint();
         entrypoint.contextInitialized(null);
        
        // when
         Optional<Class<?>> clazz = JaxygenRegistry.instance().getClassByPath("/ServicesBrowser/getModules");
        
        // then
        Assertions.assertThat(clazz)
                .isPresent()
                .contains(ServicesBrowser.class);
    }
    
}
