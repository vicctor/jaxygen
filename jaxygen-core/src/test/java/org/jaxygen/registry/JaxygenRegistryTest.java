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
package org.jaxygen.registry;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.jaxygen.registry.services.Service1;
import org.jaxygen.registry.services.Service2;
import org.jaxygen.registry.services.UnitTestRegistry;
import org.jaxygen.registry.services.UnitTestRegistryWithLongerPath;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class JaxygenRegistryTest {

    public JaxygenRegistryTest() {
    }

    @Before
    public void beforeTest() {
        JaxygenRegistry.instance().addClassRegistry(new UnitTestRegistry());
        JaxygenRegistry.instance().addClassRegistry(new UnitTestRegistryWithLongerPath());
    }

    @Test
    public void shall_returnService1() {
        // given
        JaxygenRegistry.instance().addClassRegistry(new UnitTestRegistry());

        // when
        Optional<Class<?>> result = JaxygenRegistry.instance().getClassByPath("Service1/method");

        // then
        Assertions.assertThat(result)
                .contains(Service1.class);
    }

    @Test
    public void shall_returnService2() {
        // given

        // when
        Optional<Class<?>> result = JaxygenRegistry.instance().getClassByPath("Service2/method");

        // then
        Assertions.assertThat(result)
                .contains(Service2.class);
    }
    
    @Test
    public void shall_returnService2ByLonkPath() {
        // given

        // when
        Optional<Class<?>> result = JaxygenRegistry.instance().getClassByPath("registry.services.Service2/method");

        // then
        Assertions.assertThat(result)
                .contains(Service2.class);
    }

    @Test
    public void shall_returnEmptyResutl() {
        // given
        JaxygenRegistry.instance().addClassRegistry(new UnitTestRegistry());

        // when
        Optional<Class<?>> result = JaxygenRegistry.instance().getClassByPath("Service/method");

        // then
        Assertions.assertThat(result)
                .isEmpty();
    }

}
