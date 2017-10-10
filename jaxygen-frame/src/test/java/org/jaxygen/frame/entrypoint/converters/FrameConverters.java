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
package org.jaxygen.frame.entrypoint.converters;

import org.jaxygen.typeconverter.ClassToClassTypeConverter;
import org.jaxygen.typeconverter.ConvertersRegistry;
import org.jaxygen.typeconverter.TypeConverter;
import org.jaxygen.typeconverter.exceptions.ConversionError;

/**
 *
 * @author Artur
 */
public class FrameConverters implements ConvertersRegistry {

    @Override
    public TypeConverter[] getConverters() {
        return new TypeConverter[]{
            new ClassToClassTypeConverter<String, Integer>() {
                @Override
                public Integer convert(String from) throws ConversionError {
                    return Integer.parseInt(from);
                }
            }
        };
    }
}