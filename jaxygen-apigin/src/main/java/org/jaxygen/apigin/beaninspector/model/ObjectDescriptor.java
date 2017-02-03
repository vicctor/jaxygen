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
package org.jaxygen.apigin.beaninspector.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Artur
 */
@lombok.Getter
@lombok.Setter
public class ObjectDescriptor extends FieldDescriptor {
    public final static String TYPE = "OBJECT";
    private String name;
    private String className;
    private String classFullName;
    private List<FieldDescriptor> fields;
    private List<ObjectDescriptor> derivedClasses;

    public ObjectDescriptor() {
        super(TYPE);
    }

    

    @Override
    public int hashCode() {
        return Objects.hashCode(name) + 
                Objects.hashCode(className) + 
                Objects.hashCode(classFullName) + 
                Objects.hashCode(fields) + 
                Objects.hashCode(derivedClasses);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectDescriptor other = (ObjectDescriptor) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.className, other.className)) {
            return false;
        }
        if (!Objects.equals(this.classFullName, other.classFullName)) {
            return false;
        }
        if (!this.fields.containsAll(other.fields)) {
            return false;
        }
        if (!Objects.equals(this.derivedClasses, other.derivedClasses)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OBJECT(" + name + ")[" + String.join(",", fields.stream().map(Object::toString).collect(Collectors.toList()));
    }
    
    
}
