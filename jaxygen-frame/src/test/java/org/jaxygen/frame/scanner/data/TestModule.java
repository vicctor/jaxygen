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
package org.jaxygen.frame.scanner.data;

import com.google.inject.Module;
import java.util.Collections;
import java.util.Set;
import org.jaxygen.frame.config.JaxygenModule;
import org.jaxygen.frame.entrypoint.JaxygenApplicationInitialsationError;
import org.jaxygen.typeconverter.ConvertersRegistry;

/**
 *
 * @author Artur
 */
public class TestModule implements JaxygenModule {

    @Override
    public Set<Class<?>> getServices() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<Class<? extends ConvertersRegistry>> getConverters() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<Class<? extends Module>> getGuiceModules() {
        return Collections.EMPTY_SET;
    }

    @Override
    public void onInit() throws JaxygenApplicationInitialsationError {
    }

    @Override
    public void onClose() {
    }

    @Override
    public String getName() {
        return "TestModule";
    }

    @Override
    public String getServicesPrefix() {
        return "";
    }
}
