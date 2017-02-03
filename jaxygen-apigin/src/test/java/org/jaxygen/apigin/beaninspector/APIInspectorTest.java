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

import org.jaxygen.apigin.beaninspector.APIInspector;
import com.google.common.collect.Lists;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.jaxygen.apigin.beaninspector.data.ServiceWithotutMethods;
import org.jaxygen.apigin.beaninspector.model.APIDescriptror;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class APIInspectorTest {
    
    public APIInspectorTest() {
    }

    @Test
    public void shall_inspectServiceNetapiAnnotation() throws Exception {
        // given
        Class serviceClass = ServiceWithotutMethods.class;
        List<Class> services = Lists.newArrayList(serviceClass);
        String basePath = null;
        
        // when
        APIDescriptror apiDescrptor = new APIInspector().inspect(services, basePath);
        
        // then
        assertThat(apiDescrptor)
                .isNotNull();
        assertThat(apiDescrptor.getServices())
                .hasSize(1);
        assertThat(apiDescrptor.getServices().get(0).getSerivicePath())
                .isEqualTo("org/jaxygen/apigin/beaninspector/data/ServiceWithotutMethods");
        assertThat(apiDescrptor.getServices().get(0).getServiceDescription())
                .isEqualTo("TEST1");
        assertThat(apiDescrptor.getServices().get(0).getServiceClassName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ServiceWithotutMethods");
        assertThat(apiDescrptor.getServices().get(0).getMethods())
                .isEmpty();
    }
    
}
