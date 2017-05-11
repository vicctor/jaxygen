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
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.frame.config.JaxygenModule;
import org.jaxygen.typeconverter.ConvertersRegistry;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

/**
 *
 * @author Artur
 */
public class APIScanner {

    public static Set<Class<? extends JaxygenModule>> findModules() {

        Reflections reflections = buildReflections("");
        return reflections.getSubTypesOf(JaxygenModule.class)
                .stream()
                .filter(c -> Modifier.isAbstract(c.getModifiers()) == false)
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> findServices(Package pkg) {
        return buildReflections(pkg.getName()).getTypesAnnotatedWith(NetAPI.class);
    }

    public static Set<Class<? extends ConvertersRegistry>> findConverters(Package pkg) {
        return buildReflections(pkg.getName()).getSubTypesOf(ConvertersRegistry.class);
    }

    public static Set<Class<? extends Module>> findGuiceModules(Package pkg) {
        return buildReflections(pkg.getName()).getSubTypesOf(Module.class);
    }

    public static Collection<URL> effectiveClassPathUrls(ClassLoader... classLoaders) {
        final Collection<URL> forClassLoader = ClasspathHelper.forClassLoader(classLoaders);
        return ClasspathHelper.forManifest(forClassLoader);
    }

    private static Reflections buildReflections(String packageName) {
        // good for Jetty
        URLClassLoader threadLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URLClassLoader currentLoader = (URLClassLoader) APIScanner.class.getClassLoader();
        URLClassLoader systemLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Collection<URL> urls = effectiveClassPathUrls(threadLoader, currentLoader, systemLoader);

        Reflections reflections = new Reflections(packageName, urls, threadLoader, currentLoader);
        return reflections;
    }
}
