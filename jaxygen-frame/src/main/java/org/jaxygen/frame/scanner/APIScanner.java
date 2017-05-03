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
package org.jaxygen.frame.scanner;

import com.google.inject.Module;
import java.util.Set;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.frame.config.JaxygenModule;
import org.jaxygen.typeconverter.ConvertersRegistry;
import org.reflections.Reflections;

/**
 *
 * @author Artur
 */
public class APIScanner {

    public static Set<Class<? extends JaxygenModule>> findModules() {
        Reflections reflections = new Reflections("");
        return reflections.getSubTypesOf(JaxygenModule.class);
    }

    public static Set<Class<?>> findServices(Package pkg) {
        Reflections reflections = new Reflections(pkg.getName());
        return reflections.getTypesAnnotatedWith(NetAPI.class);
    }

    public static Set<Class<? extends ConvertersRegistry>> findConverters(Package pkg) {
        Reflections reflections = new Reflections(pkg.getName());
        return reflections.getSubTypesOf(ConvertersRegistry.class);
    }
    
    public static Set<Class<? extends Module>> findGuiceModules(Package pkg) {
        Reflections reflections = new Reflections(pkg.getName());
        return reflections.getSubTypesOf(Module.class);
    }
}
