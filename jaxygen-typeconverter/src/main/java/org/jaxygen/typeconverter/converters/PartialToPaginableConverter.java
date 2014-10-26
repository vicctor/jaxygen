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
package org.jaxygen.typeconverter.converters;

import java.util.ArrayList;
import java.util.List;
import org.jaxygen.collections.PartialList;
import org.jaxygen.dto.collections.PaginableListResponseBaseDTO;
import org.jaxygen.typeconverter.ClassToClassTypeConverter;
import org.jaxygen.typeconverter.TypeConverterFactory;
import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.jodah.typetools.TypeResolver;

/**The PartialToPaginableConverter copies all elements of the instance of {@link ParialList} to
 * {@link PaginableListResponseBaseDTO}. When iterating over PartialList it call converter that
 * changes element of PartialList to object of class {@link PaginableListResponseBaseDTO}.
 * If the required converter is not registered, converter will throw {@link ConversionError} exception.
 *
 * @author Artur
 */
public abstract class PartialToPaginableConverter<FROM extends PartialList, TO extends PaginableListResponseBaseDTO>
        extends ClassToClassTypeConverter<FROM, TO> {

    private final TypeConverterFactory converters;

    /** Create PartialToPaginableConverter that uses named @see {@link TypeConverterFactory}
     * 
     * @param typeConvertersFactoryName 
     */
    protected PartialToPaginableConverter(String typeConvertersFactoryName) {
        this.converters = TypeConverterFactory.instance(typeConvertersFactoryName);
    }
    
    /**Create PartialToPaginableConverter that uses the default TypeConverterFactory 
     * 
     */
    protected PartialToPaginableConverter() {
        this.converters = TypeConverterFactory.instance();
    }

    public TO convert(FROM from) throws ConversionError {
        TO rc;
        try {
            rc = to().newInstance();
        } catch (InstantiationException ex) {
            throw new ConversionError("Could not create an instace of retun class while converting paginable collections", ex);
        } catch (IllegalAccessException ex) {
            throw new ConversionError("Could not create an instace of retun class while converting paginable collections", ex);
        }
        Class<?>[] types = TypeResolver.resolveRawArguments(PaginableListResponseBaseDTO.class, rc.getClass());
        Class<?> toContentClass = (Class<?>) types[0];
        List<Object> arrayList = new ArrayList<Object>(from.size());
        for (Object in : from) {
            Object out = converters.convert(in, toContentClass);
            arrayList.add(out);
        }
        rc.setElements(arrayList);
        rc.setSize(from.getTotalSize());
        return rc;
    }
}
