/*
 * Copyright 2014 Artur.
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

package org.jaxygen.typeconverter;

import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.jaxygen.util.BeanUtil;

/**
 * The generic implementation of the class responsible to convert one POJO into
 * anther. NOTE: class is marked abstract due to JVM generics implementation
 * converter must be instantiated to the concrete form always:
 *
 * Example usage: b = new BeanConverter&lt;AClasss, BClass&gt;{}.convert(a);
 *
 * @author Artur
 * @param <FROM> Convert from this class.
 * @param <TO> Into this class.
 */
public abstract class BeanConverter<FROM, TO> extends ClassToClassTypeConverter<FROM, TO> {

    @Override
    public TO convert(FROM from) throws ConversionError {
        TO to = null;
        try {
            if (from != null) {
                to = to().getConstructor().newInstance();
            }
        } catch (Exception ex) {
            throw new ConversionError("Could not convert parameter", ex);
        }
        if (to != null) {
            BeanUtil.translateBean(from, to);
        }
        return to;
    }
}
