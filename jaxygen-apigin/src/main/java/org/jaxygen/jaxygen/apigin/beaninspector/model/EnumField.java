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
package org.jaxygen.jaxygen.apigin.beaninspector.model;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Artur
 */
@lombok.Getter
@lombok.Setter
public class EnumField extends FieldBase {
    public static String TYPE = "ENUM";
    private List<String> values = Lists.newArrayList();
    
    public EnumField() {
        super(TYPE);
    }

    public EnumField(final String name) {
        super(TYPE, name);
    }
    
    public EnumField(final String name, String... elements) {
        super(TYPE, name);
        this.values = Lists.newArrayList(elements);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.values) + super.hashCode();
        return hash;
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
        final EnumField other = (EnumField) obj;
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return super.equals(obj);
    }
    
    
}
