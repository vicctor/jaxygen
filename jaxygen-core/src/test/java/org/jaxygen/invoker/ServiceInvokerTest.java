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
package org.jaxygen.invoker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.jaxygen.invoker.services.UnitServicesTestRegistry;
import org.jaxygen.registry.JaxygenRegistry;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Artur
 */
public class ServiceInvokerTest {

    private ByteArrayOutputStream output;
    private ServletOutputStream servletOut;

    public ServiceInvokerTest() {
    }

    @Before
    public void beforeTest() {
        JaxygenRegistry.instance().addClassRegistry(new UnitServicesTestRegistry());
        output = new ByteArrayOutputStream();
        servletOut = new ServletOutputStream() {
            @Override
            public void write(int i) throws IOException {
                output.write(i);
            }
        };
    }

    @Test
    public void shall_returnSimpleJSONWithIntegerValue() throws Exception {
        // given       
        HttpSession session = mock(HttpSession.class);
        Enumeration<?> params = Collections.emptyEnumeration();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterNames()).thenReturn(params);
        when(request.getQueryString()).thenReturn("");
        when(request.getPathInfo()).thenReturn("ServiceA/getInt10Value");
        when(request.getMethod()).thenReturn("get");
        when(request.getSession(true)).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(servletOut);

        // when
        new ServiceInvoker().doGet(request, response);

        // then
        Assertions.assertThat(new String(output.toByteArray()))
                .isEqualTo("{\"dto\":{\"responseClass\":\"java.lang.Integer\",\"responseObject\":10}}");
    }
    
    @Test
    public void shall_returnSum_InProperties_OutJSON() throws Exception {
        // given       
        HttpSession session = mock(HttpSession.class);
        Properties params = new Properties();
        params.put("a", "11");
        params.put("b", "12");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterNames()).thenReturn(params.keys());
        when(request.getParameter(any())).thenAnswer(p -> params.getProperty(p.getArgumentAt(0, String.class)));
        when(request.getQueryString()).thenReturn("");
        when(request.getPathInfo()).thenReturn("ServiceA/sum");
        when(request.getMethod()).thenReturn("get");
        when(request.getSession(true)).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(servletOut);

        // when
        new ServiceInvoker().doGet(request, response);

        // then
        Assertions.assertThat(new String(output.toByteArray()))
                .isEqualTo("{\"dto\":{\"responseClass\":\"java.lang.Integer\",\"responseObject\":23}}");
    }
    
    @Test
    public void shall_returnSum_InJSON_OutJSON() throws Exception {
        // given       
        HttpSession session = mock(HttpSession.class);
        Properties params = new Properties();
        params.put("inputType", "JSON");
        params.put("org.jaxygen.invoker.dto.RequestDTO", "{a:1, b:3}");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterNames()).thenReturn(params.keys());
        when(request.getParameter(any())).thenAnswer(p -> params.getProperty(p.getArgumentAt(0, String.class)));
        when(request.getQueryString()).thenReturn("");
        when(request.getPathInfo()).thenReturn("ServiceA/sum");
        when(request.getMethod()).thenReturn("get");
        when(request.getSession(true)).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(servletOut);

        // when
        new ServiceInvoker().doGet(request, response);

        // then
        Assertions.assertThat(new String(output.toByteArray()))
                .isEqualTo("{\"dto\":{\"responseClass\":\"java.lang.Integer\",\"responseObject\":4}}");
    }

}
