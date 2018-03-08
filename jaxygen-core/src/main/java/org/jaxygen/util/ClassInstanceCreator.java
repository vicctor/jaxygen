/*
 * Copyright 2017 xnet.
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
package org.jaxygen.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.jaxygen.annotations.DefaultImplementation;
import org.jaxygen.exceptions.InstantiateClassException;

/**
 *
 * @author xnet
 */
public class ClassInstanceCreator {

    public static <T extends Object> Ret createObject(Class<T> clazz) throws InstantiateClassException {
        if (clazz.isInterface() || isAbstrast(clazz)) {
            return createDefaultImplementation(clazz);
        }
        Constructor<T> cons;
        try {
            cons = clazz.getConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new InstantiateClassException("Cannot get constructor for class: " + clazz.getCanonicalName(), ex);
        }
        if (cons != null) {
            try {
                return new Ret(clazz, cons.newInstance());
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new InstantiateClassException("Cannot create object of class: " + clazz.getCanonicalName(), ex);
            }
        }
        return createDefaultImplementation(clazz);

    }

    private static <T extends Object> Ret createDefaultImplementation(Class<T> clazz) throws InstantiateClassException {
        if (clazz.isAnnotationPresent(DefaultImplementation.class)) {
            DefaultImplementation implAnnot = clazz.getAnnotation(DefaultImplementation.class);
            Class<? extends T> extClass = implAnnot.clazz();
            return createObject(extClass);
        } else {
            throw new RuntimeException("there is no default constructor for class: " + clazz.getCanonicalName());
        }
    }

    private static boolean isAbstrast(Class clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static class Ret<R extends Object> {

        public Ret(Class<R> clazz, R object) {
            this.clazz = clazz;
            this.object = object;
        }

        private Class<R> clazz;
        private R object;

        public Class<R> getClazz() {
            return clazz;
        }

        public void setClazz(Class<R> clazz) {
            this.clazz = clazz;
        }

        public R getObject() {
            return object;
        }

        public void setObject(R object) {
            this.object = object;
        }

    }

}
