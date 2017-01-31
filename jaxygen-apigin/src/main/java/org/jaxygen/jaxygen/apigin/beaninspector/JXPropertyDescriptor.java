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
package org.jaxygen.jaxygen.apigin.beaninspector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Artur
 */
@lombok.Getter
@lombok.Setter
class JXPropertyDescriptor {

    private String propertyName;
    private Class type;
    private Field field; // if class contains field with the same name as the property, field should be bound to here.
    private List<Annotation> annotations = new ArrayList<>();
    private boolean writtable;
    private boolean readable;
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotation) {
        return (T)annotations.stream().filter(a -> a.annotationType().equals(annotation)).findFirst().orElse(null);
    }
}
