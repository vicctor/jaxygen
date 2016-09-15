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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Artur
 */
public class JsonResponseConverterTest {
    
    public JsonResponseConverterTest() {
    }

    public static class DateContainer {
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
    @Test
    public void shall_serializeTimeToDefaultFormat() throws Exception {
        // given
        DateContainer dc = new DateContainer();
        dc.setDate(new Date(99, 3, 1, 22, 55, 11));
        OutputStream writter = new ByteArrayOutputStream();        
        JsonResponseConverter instance = new JsonResponseConverter();
        
        // when
        instance.serialize(dc, writter);
        
        // then
        Assertions.assertThat(writter.toString())
                .isEqualTo("{\"date\":\"01-04-1999 22:55:11\"}");
    }

    
}
