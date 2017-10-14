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
package org.jaxygen.frame.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jodah.typetools.TypeResolver;
import org.jaxygen.collections.PartialArrayList;
import org.jaxygen.dto.collections.PaginableListResponseBaseDTO;
import org.jaxygen.frame.Converters;
import org.jaxygen.frame.dto.base.DTOObject;
import org.jaxygen.frame.entity.base.EntityObject;
import org.jaxygen.typeconverter.ConversionHandler;
import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.jaxygen.util.BeanUtil;

/**
 *
 * @author Artur
 */
public class BaseConversionHandler implements ConversionHandler<Object, Object> {

    @Override
    public boolean canConvert(Class<?> fromClass, Class<?> toClass) {
        return areDataObjects(fromClass, toClass) ||
                arePartialAndPaginable(fromClass, toClass);
    }

    private static boolean areDataObjects(Class<?> fromClass, Class<?> toClass) {
        return EntityObject.class.isAssignableFrom(fromClass) && DTOObject.class.isAssignableFrom(toClass)
                || EntityObject.class.isAssignableFrom(toClass) && DTOObject.class.isAssignableFrom(fromClass);
    }

    private static boolean arePartialAndPaginable(Class<?> fromClass, Class<?> toClass) {
        return PartialArrayList.class.isAssignableFrom(fromClass) && PaginableListResponseBaseDTO.class.isAssignableFrom(toClass);
    }

    @Override
    public Object convert(Object from, Class<Object> fromClass, Class<Object> toClass) {
        Object rc = null;
        try {
            if (areDataObjects(fromClass, toClass)) {

                rc = toClass.newInstance();
                BeanUtil.translateBean(from, rc);

            }
            if (arePartialAndPaginable(fromClass, toClass)) {
                rc = toClass.newInstance();
                PaginableListResponseBaseDTO paginable = (PaginableListResponseBaseDTO) rc;

                PartialArrayList partial = (PartialArrayList) from;

                Class<?>[] types = TypeResolver.resolveRawArguments(PaginableListResponseBaseDTO.class, toClass);
                Class<?> paginableEntity = types[0];

                final List listOfConverterEntities = new ArrayList(); 
                partial.stream()
                        .map(o -> {
                            return (Object)Converters.convert(o, paginableEntity);
                        })
                        .forEach(o -> listOfConverterEntities.add(o));
                
                paginable.setSize(partial.getTotalSize());
                paginable.setElements(listOfConverterEntities);

            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BaseConversionHandler.class.getName()).log(Level.WARNING, "Cann not create new instance of object during conversion", ex);
            throw new ConversionError("Cann not create new instance of object during conversion", ex);
        }
        return rc;
    }

}
