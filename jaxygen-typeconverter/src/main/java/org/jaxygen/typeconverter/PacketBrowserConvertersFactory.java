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

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.reflections.Reflections;

/**
 * Calls all ConvertersRegistry classes from selected package name, and add it
 * to the registry.
 *
 * @author Artur
 */
public abstract class PacketBrowserConvertersFactory {
    
    /** Get instance TypeConverterFactory managed by the PacketBrowsersFactory class
     * 
     * @return Default instance of TypeConverterFactory.
     */
    public static TypeConverterFactory instance() {
        return TypeConverterFactory.instance();
    }
    
    /** Get instance TypeConverterFactory managed by the PacketBrowsersFactory class
     * 
     * @param name Name of factory instance.
     * @return Instance of TypeConverterFactory.
     */
    public static TypeConverterFactory instance(final String name) {
        return TypeConverterFactory.instance(name);
    }
    
    /**Lookup in the specified package name for all that implements 
     * ConvertersRegistry interface.
     * 
     * @param scannedPackageName Full qualified name of the java package containing converters.
     */
    protected static void init(final String scannedPackageName) {
        Reflections reflections = new Reflections(scannedPackageName);
        Set<Class<? extends ConvertersRegistry>> annotated
                = reflections.getSubTypesOf(ConvertersRegistry.class);

        for (Class<? extends ConvertersRegistry> c : annotated) {
            try {
                ConvertersRegistry cr = c.newInstance();
                for (TypeConverter tc : cr.getConverters()) {
                    instance().registerConverter(tc);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(PacketBrowserConvertersFactory.class.getName()).log(Level.SEVERE, "Could not register converter " + c.getName(), ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PacketBrowserConvertersFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Get the converter which could translate an object from class from to
     * class to.
     *
     * @param <FROM> From type.
     * @param <TO> To type.
     * @param from From converter class.
     * @param to To conversion class.
     * @return Type converter that converts FROM from to TO.
     */
    public static <FROM, TO> TypeConverter<FROM, TO> get(Class<FROM> from, Class<TO> to) {
        return (TypeConverter<FROM, TO>) instance().get(from, to);
    }

    /**
     * Get the converter which could translate an object from class from to
     * class to.
     *
     * @param <FROM> From type.
     * @param <TO> To type.
     * @param from From object.
     * @param toClass To conversion class.
     * @return Type converter that converts FROM from to TO.
     * @throws org.jaxygen.typeconverter.exceptions.ConversionError Conversion error.
     */  
  public static <FROM, TO> TO convert(FROM from, Class<TO> toClass) throws ConversionError {
        return instance().convert(from, toClass);
    }

    /**
     * Convenient method used in case if the from object could be null.
     *
     * @param <FROM> From type.
     * @param <TO> To type.
     * @param from From object.
     * @param fromClass The class from which the conversion is done. Note that this could force using 
     * another converter that that for class of form parameter.
     * @param toClass To conversion class.
     * @return Type converter that converts FROM from to TO.
     * @throws org.jaxygen.typeconverter.exceptions.ConversionError Conversion error.
     */
    public static <FROM, TO> TO convert(FROM from, Class<FROM> fromClass, Class<TO> toClass) throws ConversionError {
        return instance().convert(from, fromClass, toClass);
    }
}
