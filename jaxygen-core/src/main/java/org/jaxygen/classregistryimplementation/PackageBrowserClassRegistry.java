/*
 * Copyright 2014 Artur.
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
package org.jaxygen.classregistryimplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.invoker.ClassRegistry;
import org.reflections.Reflections;

/**
 * Utility implementation of the ClassRegistry. This class registry
 * implementation browses for all classes with @NetAPI annotation within the
 * specified package name.
 *
 * One has to extend the service specific implementation in the web-service
 * module, and pass the implementation name into the classRegistry init-param of
 * your APIBrowser. Not that the concrete implementation of this class requires
 * a constructor without parameters.
 *
 * @author Artur
 */
public abstract class PackageBrowserClassRegistry implements ClassRegistry {

    private final String scannedPackageName;

    protected PackageBrowserClassRegistry(String scannedPackageName) {
        this.scannedPackageName = scannedPackageName;
    }

    @Override
    public String getPackageBase() {
        return this.scannedPackageName;
    }

    @Override
    public List<Class> getRegisteredClasses() {
        List<Class> classes = new ArrayList<>();
        Reflections reflections = new Reflections(scannedPackageName);
        Set<Class<?>> annotated
                = reflections.getTypesAnnotatedWith(NetAPI.class);
        classes.addAll(annotated);
        return classes;
    }

}
