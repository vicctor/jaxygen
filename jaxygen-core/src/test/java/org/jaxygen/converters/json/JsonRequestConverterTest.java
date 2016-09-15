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
package org.jaxygen.converters.json;

import java.util.Date;
import org.assertj.core.api.Assertions;
import org.jaxygen.http.HttpRequestParams;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author Artur
 */
public class JsonRequestConverterTest {
    
    public static class DateContainer {
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
    
    public JsonRequestConverterTest() {
    }


    @Test
    public void shall_parseDateIntoObject() throws Exception {
        // given    
        HttpRequestParams params = Mockito.mock(HttpRequestParams.class);
        Mockito.when(params.getAsString(DateContainer.class.getName(), 1, Integer.MAX_VALUE, true))
                .thenReturn("{\"date\":\"01-01-1970 01:00:00\"}");
        JsonRequestConverter instance = new JsonRequestConverter();
        
        // when
        Object result = instance.deserialise(params, DateContainer.class);
        
        // then
        Assertions.assertThat(result)
                .isInstanceOf(DateContainer.class);
                
    }
    
}
