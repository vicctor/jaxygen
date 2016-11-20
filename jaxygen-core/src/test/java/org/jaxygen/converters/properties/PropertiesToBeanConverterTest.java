/*
 * Copyright 2016 Artur.
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
package org.jaxygen.converters.properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.Pair;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.http.HttpRequestParams;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Artur
 */
public class PropertiesToBeanConverterTest {

    public static class BeanWithFileds {

        public boolean booleanVal = false;

        public boolean isBooleanVal() {
            return booleanVal;
        }

        public void setBooleanVal(boolean booleanVal) {
            this.booleanVal = booleanVal;
        }
    }

    public PropertiesToBeanConverterTest() {
    }

    @Test
    public void test_isCovertable() {
        // given
        List<Class> convertable = Lists.newArrayList(Boolean.class,
                Boolean.TYPE,
                Byte.class,
                Byte.TYPE,
                Character.class,
                Character.TYPE,
                Float.class,
                Float.TYPE,
                Double.class,
                Double.TYPE,
                double.class,
                Integer.class,
                Integer.TYPE,
                Long.class,
                Long.TYPE,
                Short.class,
                Short.TYPE,
                Enum.class,
                String.class);

        // when
        List<Class> result = convertable.stream()
                .map(c -> new Pair<>(c, PropertiesToBeanConverter.isCovertable(c)))
                .filter(t -> t.getValue())
                .map(c -> c.getKey())
                .collect(Collectors.toList());

        // then
        assertThat(result)
                .containsAll(convertable);
    }

    @Test
    public void shall_convertDeserializeBooleanToField() throws DeserialisationError {
        // given
        Map<String, String> props = Maps.newHashMap();
        props.put("booleanVal", "true");
        HttpRequestParams params = mock(HttpRequestParams.class);
        when(params.getParameters()).thenReturn(props);

        // when
        BeanWithFileds resutl = (BeanWithFileds) new PropertiesToBeanConverter().deserialise(params, BeanWithFileds.class);

        // then
        assertThat(resutl)
                .hasFieldOrPropertyWithValue("booleanVal", true);
    }

    @Test
    public void testGetName() {
        // given
        PropertiesToBeanConverter instance = new PropertiesToBeanConverter();

        // whne
        String result = instance.getName();

        // then
        String expResult = "PROPERTIES";
        assertThat(expResult).isEqualTo(result);
    }

}
