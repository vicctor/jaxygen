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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 *
 * @author Artur
 */
public class JsonResponseConverterTest {

    public final static String DATE_FORMAT_STRING = "dd-MM-yyyy HH:mm:ss";
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);

    public static class DateContainer {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    private final static TypeAdapter<Date> DATE_ADAPTER = new TypeAdapter<Date>() {
        @Override
        public void write(JsonWriter writer, Date t) throws IOException {
            if (t != null) {
                writer.value(DATE_FORMAT.format(t));
            } else {
                writer.nullValue();
            }
        }

        @Override
        public Date read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }

            String dateAsString = reader.nextString();
            try {
                return DATE_FORMAT.parse(dateAsString);
            } catch (ParseException ex) {
                throw new IOException("Could not parse Date", ex);
            }
        }
    };

    public static void setupGsonBuilder() {
        JSONBuilderRegistry.setGSONBuilder(new GSONBuilderFactory() {
            GsonBuilder builder = null;

            @Override
            public GsonBuilder createBuilder() {
                if (builder == null) {
                    builder = new GsonBuilder()
                            .registerTypeAdapter(Date.class, DATE_ADAPTER);
                }
                return builder;
            }

            @Override
            public Gson build() {

                createBuilder = createBuilder();
                return createBuilder.create();
            }
            private GsonBuilder createBuilder;
        });
    }

    @BeforeClass
    public static void setUpClass() {
        setupGsonBuilder();
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
