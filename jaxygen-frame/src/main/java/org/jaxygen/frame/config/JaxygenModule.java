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
import org.jaxygen.frame.entrypoint.JaxygenApplicationInitialsationError;
import org.jaxygen.typeconverter.ConvertersRegistry;

/** Entry point for the Jaxygen module. Every module should implement one
 * instance of this interface in order to the 
 *
 * @author Artur
 */
public interface JaxygenModule {

    String getName();

    void onInit() throws JaxygenApplicationInitialsationError;

    void onClose();

    Set<Class<?>> getServices();

    String getServicesPrefix();

    Set<Class<? extends ConvertersRegistry>> getConverters();

    Set<Class<? extends Module>> getGuiceModules();
}
