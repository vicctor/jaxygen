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
package org.jaxygen.frame;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jaxygen.frame.config.JaxygenModule;
import org.jaxygen.frame.converters.BaseConversionHandler;
import org.jaxygen.frame.entrypoint.JaxygenModulesRegistry;
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
public class JaxygenApplication {

    private final static Logger LOG = Logger.getLogger("JaxygenEntrypoint");
    private final List<Class<? extends Module>> GUICE_MODULES = new ArrayList<>();
    private Injector injector;

    private final ObjectBuilder objectBuilder = new ObjectBuilder() {
        @Override
        public <T> T create(Class<T> clazz) throws ObjectCreateError {
            return injector.getInstance(clazz);
        }
    };

    private void registerDefaultConvertersHandler() {
        BaseConversionHandler handler = new BaseConversionHandler();
        TypeConverterFactory.instance().registerHandler(handler);
    }

    class CoreModule extends AbstractModule {

        public CoreModule() {
        }

        @Override
        protected void configure() {
            bind(TypeConverterFactory.class).toInstance(TypeConverterFactory.instance());
        }
    };

    public void start() {
        LOG.info("Initializing JaxyGen application");
        Set<Class<? extends JaxygenModule>> modules = APIScanner.findModules();
        LOG.log(Level.INFO, "Found {0} modules", modules.size());
        Optional.ofNullable(modules)
                .orElse(new HashSet<>())
                .stream()
                .map(clazz -> toModule(clazz))
                .filter(Objects::nonNull)
                .forEach(m -> register(m));

        registerInjectors();
        registerDefaultConvertersHandler();
        ObjectBuilderFactory.configure(objectBuilder);
    }

    public void stop() {
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

    private static <T> T newObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Jaxygen was unable to instantiate class " + clazz.getCanonicalName(), ex);
            return null;
        }
    }

    private void registerInjectors() {
        List<Module> modules = GUICE_MODULES.stream()
                .map(clazz -> newObject(clazz))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        modules.add(new CoreModule());
        injector = Guice.createInjector(modules);
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
