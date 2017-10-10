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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.jaxygen.frame.config.JaxygenModule;
import org.jaxygen.frame.scanner.APIScanner;
import org.jaxygen.invoker.ServiceRegistry;
import org.jaxygen.objectsbuilder.ObjectBuilder;
import org.jaxygen.objectsbuilder.ObjectBuilderFactory;
import org.jaxygen.objectsbuilder.exceptions.ObjectCreateError;
import org.jaxygen.registry.JaxygenRegistry;
import org.jaxygen.typeconverter.ConvertersRegistry;
import org.jaxygen.typeconverter.TypeConverterFactory;

/**
 *
 * @author Artur
 */
public class JaxygenEntrypoint implements ServletContextListener {

    private final static Logger LOG = Logger.getLogger("JaxygenEntrypoint");

    private final static List<Class<? extends ConvertersRegistry>> CONVERTERS = new ArrayList<>();
    private final static List<Class<? extends Module>> GUICE_MODULES = new ArrayList<>();
    //private static Set<Class<? extends JaxygenModule>> modules = new HashSet<>();
    

    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initializing JaxyGen application");
        Set<Class<? extends JaxygenModule>> modules = APIScanner.findModules();
        LOG.info("Found " + modules.size() + " modules");
        Optional.ofNullable(modules)
                .orElse(new HashSet<>())
                .stream()
                .map(clazz -> toModule(clazz))
                .filter(Objects::nonNull)
                .forEach(m -> register(m));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JaxygenModulesRegistry.getInstance()
                .stream()
                .forEach(m -> m.onClose());
    }

    private void register(JaxygenModule module) {
        LOG.log(Level.INFO, "Registering module : {0}", module.getName());
        JaxygenModulesRegistry.getInstance().add(module);
        if (module.getServices() != null) {
            ServiceRegistry servicesRegistry = toServicesRegistry(module);
            JaxygenRegistry.instance().addClassRegistry(servicesRegistry);
        }
        if (module.getConverters() != null) {
            registerConverters(module.getConverters());
            
        }
        if (module.getGuiceModules() != null) {
            GUICE_MODULES.addAll(module.getGuiceModules());
        }
    }

    private ServiceRegistry toServicesRegistry(JaxygenModule module) {
        LOG.log(Level.INFO, "Registering services from module {0} on removing prefix {1}", new Object[]{module.getName(), module.getServicesPrefix()});
        dumpServices(module);
        return new ServiceRegistry() {
            @Override
            public Set<Class<?>> getRegisteredClasses() {
                return module.getServices();
            }

            @Override
            public String getPackageBase() {
                return module.getServicesPrefix();
            }
        };
    }

    private <T> T toModule(Class<? extends T> clazz) {
        T result = null;
        try {
            result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Unable to initialize module with class " + clazz.getCanonicalName(), ex);
        }
        return result;
    }
    
    private <T> T toObject(ObjectBuilder builder, Class<? extends T> clazz) {
        T result = null;
        try {
            result = builder.create(clazz);
        } catch (ObjectCreateError ex) {
            LOG.log(Level.SEVERE, "Unable to initialize module with class " + clazz.getCanonicalName(), ex);
        }
        return result;
    }

    private void dumpServices(JaxygenModule module) {
        String prefix = module.getServicesPrefix();

        Stream<String> stream = module.getServices().stream()
                .map(clazz -> clazz.getName());
        if (prefix != null) {
            stream = stream.map(name -> name.replace(prefix, ""));
        }
        stream.forEach(name -> LOG.log(Level.INFO, "Registered service endpoint: {0}", name));
    }

    private void registerConverters(Set<Class<? extends ConvertersRegistry>> converters) {
        ObjectBuilder builder = ObjectBuilderFactory.instance();
        converters.stream()
                .map(registryClass -> toObject(builder, registryClass))
                .filter(Objects::nonNull)
                .map(registry -> registry.getConverters())
                .flatMap(Arrays::stream)
                .forEach(converter -> TypeConverterFactory.instance().registerConverter(converter));    
    }
}
