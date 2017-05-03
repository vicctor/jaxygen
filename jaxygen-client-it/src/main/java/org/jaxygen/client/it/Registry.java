/*
 * Copyright 2012 imfact02.
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
package org.jaxygen.client.it;

import java.util.HashSet;
import java.util.Set;
import org.jaxygen.invoker.ServiceRegistry;

/**
 *
 * @author imfact02
 */
public class Registry implements ServiceRegistry{

    private static Set<Class<?>> classes = new HashSet<>();
    static {
        classes.add(TestBean.class);
    }

    @Override
    public String getPackageBase() {
        return "org.jaxygen.client.it";
    }
    
    public Set<Class<?>> getRegisteredClasses() {
        return classes;
    }
    
}
