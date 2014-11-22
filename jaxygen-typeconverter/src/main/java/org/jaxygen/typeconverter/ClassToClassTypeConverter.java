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

import org.jodah.typetools.TypeResolver;

/**
 * The abstract implementation of the TypeConverter which is a base for class to
 * class convertion. Class implements protected methods from() and to()
 * returning the class types of the corresponding convertion subjects.
 *
 * @author Artur
 * @param <FROM> Convert from this class.
 * @param <TO> Into this class.
 */
public abstract class ClassToClassTypeConverter<FROM, TO> implements TypeConverter<FROM, TO> {

    private final Class<FROM> fromClass;
    private final Class<TO> toClass;

    @SuppressWarnings("unchecked")
    public ClassToClassTypeConverter() {
        Class<?>[] types = TypeResolver.resolveRawArguments(ClassToClassTypeConverter.class, this.getClass());
        fromClass = (Class<FROM>) types[0];
        toClass = (Class<TO>) types[1];
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Class<FROM> from() {
        return fromClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<TO> to() {
        return toClass;
    }
}
