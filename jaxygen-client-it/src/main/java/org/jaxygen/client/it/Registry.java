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

import java.util.ArrayList;
import java.util.List;
import org.jaxygen.invoker.ClassRegistry;

/**
 *
 * @author imfact02
 */
public class Registry implements ClassRegistry{

    private static List<Class> classes = new ArrayList<Class>();
    static {
        classes.add(TestBean.class);
    }
    
    public List<Class> getRegisteredClasses() {
        return classes;
    }
    
}
