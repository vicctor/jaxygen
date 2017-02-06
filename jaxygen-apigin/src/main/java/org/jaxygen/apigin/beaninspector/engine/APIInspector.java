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
package org.jaxygen.apigin.beaninspector.engine;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.apigin.beaninspector.exceptions.InspectionError;
import org.jaxygen.apigin.beaninspector.model.APIDescriptror;
import org.jaxygen.apigin.beaninspector.model.FieldDescriptor;
import org.jaxygen.apigin.beaninspector.model.MethodDescriptor;
import org.jaxygen.apigin.beaninspector.model.ServiceDescriptor;
import org.jaxygen.apigin.beaninspector.model.VoidField;

/**
 *
 * @author Artur
 */
public class APIInspector {

    private class InternalInspectError extends RuntimeException {

        public InternalInspectError(String string) {
            super(string);
        }

        public InternalInspectError(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }

    /**
     * Inspect web services classes and return the APIDescriptor containing
     * model of the service interface.
     *
     * @param services
     * @param basePath
     * @return
     * @throws InspectionError
     */
    public APIDescriptror inspect(Collection<Class> services, final String basePath) throws InspectionError {
        APIDescriptror descriptor = new APIDescriptror();
        descriptor.setServiceBase(Strings.emptyToNull(basePath));
        try {
            List<ServiceDescriptor> serviceDescriptors = services.stream()
                    .map(c -> classToServiceDescriptor(c, basePath))
                    .collect(Collectors.toList());
            descriptor.setServices(serviceDescriptors);
        } catch (InternalInspectError ex) {
            throw new InspectionError("Error while ispecting services", ex);
        }
        return descriptor;
    }

    private ServiceDescriptor classToServiceDescriptor(Class clazz, final String basePath) {
        NetAPI annotation = (NetAPI) clazz.getAnnotation(NetAPI.class);
        Optional.ofNullable(annotation)
                .orElseThrow(() -> new InternalInspectError("Class " + clazz.getCanonicalName() + " is not annotated by NetApi annotation"));
        ServiceDescriptor descriptor = new ServiceDescriptor();
        final String path = simplifyPath(clazz, basePath);
        descriptor.setSerivicePath(path);
        descriptor.setServiceClassName(clazz.getCanonicalName());
        descriptor.setServiceDescription(annotation.description());
        descriptor.setSinceVersion(annotation.version());
        descriptor.setStatus(annotation.status());

        List<MethodDescriptor> methodDescriptrors
                = Lists.newArrayList(clazz.getMethods()).stream()
                        .filter(m -> m.isAnnotationPresent(NetAPI.class))
                        .map(m -> methodToDescriptor(m, path))
                        .collect(Collectors.toList());

        descriptor.setMethods(methodDescriptrors);

        return descriptor;
    }

    private MethodDescriptor methodToDescriptor(Method method, String path) {
        MethodDescriptor descriptor = new MethodDescriptor();
        Class<?> returnType = method.getReturnType();
        Class<?>[] parameterTypes = method.getParameterTypes();
        NetAPI annotation = method.getAnnotation(NetAPI.class);
        if (parameterTypes != null && parameterTypes.length > 1) {
            throw new InternalInspectError("Only one parameter per method allowed. It's not that we can not handle more, but your code will be much easier to read thanks that. Check out method " + method.getName());
        }
        if (parameterTypes != null && parameterTypes.length == 1) {
            try {
                Class<?> paramType = parameterTypes[0];
                FieldDescriptor input = BeanInspector.inspect(paramType);
                descriptor.setInput(input);
            } catch (InspectionError ex) {
                throw new InternalInspectError("Could not inspect parameter of method " + method.getName(), ex);
            }
        } else {
            descriptor.setInput(VoidField.VOID);
        }
        
        if (returnType == null || returnType.equals(Void.TYPE)) {
                descriptor.setOutput(VoidField.VOID);
        } else {
            try {
                FieldDescriptor output = BeanInspector.inspect(returnType);
                descriptor.setOutput(output);
            } catch (InspectionError ex) {
                throw new InternalInspectError("Could not inspect parameter of method " + method.getName());
            }
        } 
        
        descriptor.setName(method.getName());
        descriptor.setPath(path + "/" + method.getName());
        descriptor.setDescription(annotation.description());
        descriptor.setSinceVersion(Strings.nullToEmpty(annotation.version()));
        descriptor.setStatus(descriptor.getStatus());
        return descriptor;
    }

    public String simplifyPath(Class clazz, final String basePath) {
        String path = clazz.getCanonicalName();
        path = path.replace(".", "/");
        if (basePath != null) {
            path = path.replace(basePath + "/", "");
        }
        return path;
    }
}
