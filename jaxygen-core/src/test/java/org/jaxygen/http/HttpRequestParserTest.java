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
package org.jaxygen.http;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Artur
 */
public class HttpRequestParserTest {

    HttpServletRequest request;
    
    public HttpRequestParserTest() {
        request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("get");
    }
    
    @Test
    public void shall_parseIndexedListOfStringRequestProperties() throws Exception {
        // given
        final Properties props = new Properties();
        props.setProperty("p[0]", "A");
        props.setProperty("p[1]", "B");
        props.setProperty("p[2]", "C");        
        when(request.getParameterNames()).thenAnswer(i -> props.propertyNames());
        when(request.getParameter(anyString())).thenAnswer(i -> props.getProperty(i.getArgumentAt(0, String.class)));
        
        // when
        HttpRequestParser parser = new HttpRequestParser(request);
        
        // then
        Assertions.assertThat(parser.getAsListOfStrings("p"))
                .containsSequence("A", "B", "C");
    }
    
    @Test
    public void shall_parseIndexedListOfIntRequestProperties() throws Exception {
        // given
        final Properties props = new Properties();
        props.setProperty("p[0]", "10");
        props.setProperty("p[1]", "11");
        props.setProperty("p[2]", "12");        
        when(request.getParameterNames()).thenAnswer(i -> props.propertyNames());
        when(request.getParameter(anyString())).thenAnswer(i -> props.getProperty(i.getArgumentAt(0, String.class)));
        
        // when
        HttpRequestParser parser = new HttpRequestParser(request);
        
        // then
        Assertions.assertThat(parser.getAsListOfInt("p"))
                .containsSequence(10, 11, 12);
    }
    
}
