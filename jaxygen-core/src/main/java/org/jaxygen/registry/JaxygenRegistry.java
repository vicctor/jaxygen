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

import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.invoker.ServiceRegistry;

/**
 *
 * @author Artur
 */
public class JaxygenRegistry {

    private static final JaxygenRegistry REGISTRY = new JaxygenRegistry();

    private final Map<String, Optional<Class<?>>> servicesCache = new HashMap<>();
    private final Set<ServiceRegistry> classRegistries;

    private JaxygenRegistry() {
        this.classRegistries = new HashSet<>();
    }

    public static JaxygenRegistry instance() {
        return REGISTRY;
    }

    public void addClassRegistry(ServiceRegistry classRegistry) {
        classRegistries.add(classRegistry);
    }

    public Set<ServiceRegistry> getClassRegistries() {
        return classRegistries;
    }

    public Optional<Class<?>> getClassByPath(String path) {
        Optional<Class<?>> rc = Optional.empty();
        if (servicesCache.containsKey(path)) {
            rc = servicesCache.get(path);
        } else {
            final Optional<String> serviceName = getServicenameFromPath(path);
            if (serviceName.isPresent()) {
                rc = classRegistries.stream()
                        .map(reg -> pathToClass(reg, serviceName.get()))
                        .filter(Optional::isPresent)
                        .findFirst()
                        .orElse(rc);
            }
            servicesCache.put(path, rc);
        }
        return rc;
    }

    public Optional<String> getServicenameFromPath(String path) {
        Optional<String> rc;
        String[] chunks = path.split("/");
        if (chunks.length >= 2) {
            final String className = chunks[chunks.length - 2];
            rc = Optional.of(className);
        } else {
            rc = Optional.empty();
        }
        return rc;
    }

    public Optional<String> getMethodNameFromPath(String path) {
        Optional<String> rc;
        String[] chunks = path.split("/");
        if (chunks.length >= 2) {
            final String className = chunks[chunks.length - 1];
            rc = Optional.of(className);
        } else {
            rc = Optional.empty();
        }
        return rc;
    }

    private Optional<Class<?>> pathToClass(ServiceRegistry reg, String path) {
        String fullPath = buildClassName(reg.getPackageBase(), path);
        return reg.getRegisteredClasses().stream()
                .filter(clazz -> clazz.getName().equals(fullPath))
                .findFirst();
    }

    public Optional<Method> getMethodByName(Class clazz, String methodName) {                        
        return Lists.newArrayList(clazz.getMethods())
                .stream()
                .filter(m -> m.isAnnotationPresent(NetAPI.class))
                .filter(m -> m.getName().equals(methodName))
                .findFirst();
    }
    
    private String buildClassName(final String servicesRoot, final String className) {
        String fullClassName = className.replace("/", ".");
        if (servicesRoot != null && !servicesRoot.isEmpty()) {
            fullClassName = servicesRoot + "." + fullClassName;
        }
        return fullClassName;
    }
}
