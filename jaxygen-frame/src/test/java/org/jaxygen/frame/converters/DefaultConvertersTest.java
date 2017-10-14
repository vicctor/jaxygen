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

import org.assertj.core.api.Assertions;
import org.jaxygen.collections.PartialArrayList;
import org.jaxygen.dto.collections.PaginableListResponseBaseDTO;
import org.jaxygen.frame.Converters;
import org.jaxygen.frame.JaxygenApplication;
import org.jaxygen.frame.dto.base.DTOObject;
import org.jaxygen.frame.entity.base.EntityObject;
import org.jaxygen.typeconverter.BeanConverter;
import org.jaxygen.typeconverter.ClassToClassTypeConverter;
import org.jaxygen.typeconverter.TypeConverterFactory;
import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class DefaultConvertersTest {

    JaxygenApplication app;

    @lombok.Data
    public static class BaseOfAll {

        private int x;
        private int y;
    }

    
    public static class TestEntity extends BaseOfAll implements EntityObject {

    }

    public static class TestEntityStrict extends BaseOfAll implements EntityObject {

    }
   
    public static class TestDTO extends BaseOfAll implements DTOObject {

    }
    
    public static class PartialEntityList extends PartialArrayList<TestEntity>{};
    
    public static class PaginableDTO extends PaginableListResponseBaseDTO<TestDTO>{};
    
    
    ClassToClassTypeConverter<TestDTO, TestEntityStrict> directConverter = new BeanConverter<TestDTO, TestEntityStrict>() {
        @Override
        public TestEntityStrict convert(TestDTO from) throws ConversionError {
            TestEntityStrict rc = super.convert(from);
            rc.setX(-1);
            return rc;
        }
        
};
    

    public DefaultConvertersTest() {
    }

    @Before
    public void before() {
        app = new JaxygenApplication();
        app.start();
        TypeConverterFactory.instance().registerConverter(directConverter);
    }

    @After
    public void after() {
        app.stop();
    }

    @Test
    public void should_convertEntryToDTO() {
        // given
        TestEntity entity = new TestEntity();
        entity.setX(10);
        entity.setY(100);

        // when
        TestDTO dto = Converters.convert(entity, TestDTO.class);

        // then
        Assertions.assertThat(dto).isEqualToComparingFieldByField(entity);
    }
    
    @Test
    public void should_convertPartialToPagibable() {
        // given
        TestEntity entity = new TestEntity();
        entity.setX(10);
        entity.setY(100);
        PartialEntityList partial = new PartialEntityList();
        partial.setTotalSize(1000);
        partial.add(entity);

        // when
        PaginableDTO dto = Converters.convert(partial, PaginableDTO.class);

        // then
        TestDTO expected = new TestDTO();
        expected.setX(10);
        expected.setY(100);
        Assertions.assertThat(dto.getSize()).isEqualTo(1000);
        Assertions.assertThat(dto.getElements()).hasSize(1);
        Assertions.assertThat(dto.getElements().get(0)).isEqualToComparingFieldByField(expected);
    }
    
    @Test
    public void should_convertUsingConverterClass() {
        // given
        TestDTO dto = new TestDTO();
        dto.setX(10);
        dto.setY(100);

        // when
        TestEntityStrict entity = Converters.convert(dto, TestEntityStrict.class);

        // then
        Assertions.assertThat(entity.getX()).isEqualTo(-1);
    }

}
