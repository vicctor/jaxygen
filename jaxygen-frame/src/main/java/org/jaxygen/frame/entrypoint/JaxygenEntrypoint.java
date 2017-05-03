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
package org.jaxygen.frame.entrypoint;

import com.google.inject.Module;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.jaxygen.frame.config.JaxygenModule;
import org.jaxygen.frame.scanner.APIScanner;
import org.jaxygen.typeconverter.ConvertersRegistry;
import org.jaxygen.invoker.ServiceRegistry;

/**
 *
 * @author Artur
 */
public class JaxygenEntrypoint implements ServletContextListener {

    private final static Logger LOG = Logger.getLogger("JaxygenEntrypoint");

    private final static List<ServiceRegistry> SERVICES = new ArrayList<>();
    private final static List<Class<? extends ConvertersRegistry>> CONVERTERS = new ArrayList<>();
    private final static List<Class<? extends Module>> GUICE_MODULES = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Set<Class<? extends JaxygenModule>> modules = APIScanner.findModules();
        Optional.ofNullable(modules)
                .orElse(new HashSet<>())
                .stream()
                .map(clazz -> toModule(clazz))
                .filter(Objects::nonNull)
                .forEach(m ->register(m));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private void register(JaxygenModule module) {
        LOG.log(Level.INFO, "Registering module : {0}", module.getName());
        if (module.getServices() != null) {
            SERVICES.add(toClassRegistry(module));
        }
        if (module.getConverters() != null) {
            CONVERTERS.addAll(module.getConverters());
        }
        if (module.getGuiceModules()!= null) {
            GUICE_MODULES.addAll(module.getGuiceModules());
        }
    }

    private ServiceRegistry toClassRegistry(JaxygenModule module) {
        return new ServiceRegistry() {
            @Override
            public Set<Class<?>> getRegisteredClasses() {
                return module.getServices();
            }

            @Override
            public String getPackageBase() {
                return module.getServicesBasePath();
            }
        };
    }

    private JaxygenModule toModule(Class<? extends JaxygenModule> clazz) {
        JaxygenModule result = null;
        try {
            result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Unable to initialize module with class " + clazz.getCanonicalName(), ex);
        }
        return result;
    }
}
