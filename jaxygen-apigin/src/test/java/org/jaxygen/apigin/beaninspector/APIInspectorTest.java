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
package org.jaxygen.apigin.beaninspector;

import org.jaxygen.apigin.beaninspector.engine.APIInspector;
import com.google.common.collect.Lists;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.jaxygen.annotations.Status;
import org.jaxygen.apigin.beaninspector.data.ServiceWithPublishedMethods;
import org.jaxygen.apigin.beaninspector.data.ServiceWithotutMethods;
import org.jaxygen.apigin.beaninspector.exceptions.InspectionError;
import org.jaxygen.apigin.beaninspector.model.APIDescriptror;
import org.jaxygen.apigin.beaninspector.model.ArrayField;
import org.jaxygen.apigin.beaninspector.model.IntegerField;
import org.jaxygen.apigin.beaninspector.model.MethodDescriptor;
import org.jaxygen.apigin.beaninspector.model.StringField;
import org.jaxygen.apigin.beaninspector.model.VoidField;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class APIInspectorTest {

    public APIInspectorTest() {
    }

    @Test
    public void shall_twoMethodDescriptrBeEqual() {
        // given
        ArrayField input = new ArrayField("arrayOfStrings", new StringField());
        ArrayField output = new ArrayField("arrayOfStrings", new StringField());
        MethodDescriptor d1 = new MethodDescriptor("name1", "path1", "descriptrion", input, output, Status.Nonfunctional, "");
        MethodDescriptor d2 = new MethodDescriptor("name1", "path1", "descriptrion",input, output, Status.Nonfunctional, "");

        // when
        boolean res = d1.equals(d2);

        // then
        assertThat(res)
                .isTrue();
    }

    @Test
    public void shall_twoMethodsWithVoidsBeEuql() {
        // given
        MethodDescriptor m1 = new MethodDescriptor("voidTovoidTest", "ServiceWithPublishedMethods/voidTovoidTest", "descriptrion", VoidField.VOID, VoidField.VOID, Status.Nonfunctional, "");
        MethodDescriptor m2 = new MethodDescriptor("voidTovoidTest", "ServiceWithPublishedMethods/voidTovoidTest", "descriptrion", VoidField.VOID, VoidField.VOID, Status.Nonfunctional, "");
        
        // when
        boolean rc = m1.equals(m2);
        
        // then
        assertThat(rc)
                .isTrue();
    }

    @Test
    public void shall_inspectServiceNetapiAnnotation() throws Exception {
        // given
        Class serviceClass = ServiceWithotutMethods.class;
        List<Class> services = Lists.newArrayList(serviceClass);
        String basePath = null;

        // when
        APIDescriptror apiDescrptor = new APIInspector().inspect(services, basePath);

        // then
        assertThat(apiDescrptor)
                .isNotNull();
        assertThat(apiDescrptor.getServices())
                .hasSize(1);
        assertThat(apiDescrptor.getServices().get(0).getSerivicePath())
                .isEqualTo("org/jaxygen/apigin/beaninspector/data/ServiceWithotutMethods");
        assertThat(apiDescrptor.getServices().get(0).getServiceDescription())
                .isEqualTo("TEST1");
        assertThat(apiDescrptor.getServices().get(0).getServiceClassName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ServiceWithotutMethods");
        assertThat(apiDescrptor.getServices().get(0).getMethods())
                .isEmpty();
    }

    @Test
    public void shall_pathShallBeShorten() throws Exception {
        // given
        Class serviceClass = ServiceWithotutMethods.class;
        List<Class> services = Lists.newArrayList(serviceClass);
        String basePath = "org/jaxygen/apigin/beaninspector/data";

        // when
        APIDescriptror apiDescrptor = new APIInspector().inspect(services, basePath);

        // then
        assertThat(apiDescrptor)
                .isNotNull();
        assertThat(apiDescrptor.getServices())
                .hasSize(1);
        assertThat(apiDescrptor.getServices().get(0).getSerivicePath())
                .isEqualTo("ServiceWithotutMethods");
    }

    @Test
    public void shall_throwExceptionIfNetAPIMissing() {
        // given
        final Class clazz = this.getClass();
        final List<Class> services = Lists.newArrayList(clazz);

        // when
        Throwable res = catchThrowable(() -> new APIInspector().inspect(services, null));

        // then
        assertThat(res)
                .isInstanceOf(InspectionError.class)
                .hasMessage("Error while ispecting services");
    }

    @Test
    public void shall_inspectServiceWithPublishedMethods() throws InspectionError {
        // given
        final Class clazz = ServiceWithPublishedMethods.class;
        final List<Class> services = Lists.newArrayList(clazz);
        String basePath = "org/jaxygen/apigin/beaninspector/data";

        // when
        APIDescriptror res = new APIInspector().inspect(services, basePath);

        // then
        assertThat(res)
                .isNotNull();
        assertThat(res.getServices())
                .hasSize(1);
        assertThat(res.getServices().get(0).getMethods())
                .isNotNull()
                .hasSize(3);
        MethodDescriptor voidToVoid = new MethodDescriptor("voidTovoidTest", "ServiceWithPublishedMethods/voidTovoidTest", "void_void", VoidField.VOID, VoidField.VOID, Status.Undefined, "");
        MethodDescriptor intToInt = new MethodDescriptor("intToIntTest", "ServiceWithPublishedMethods/intToIntTest", "int_int", new IntegerField(), new IntegerField(), Status.Undefined, "");
        MethodDescriptor stringToString = new MethodDescriptor("stringToStringTest", "ServiceWithPublishedMethods/stringToStringTest", "string_string", new StringField(), new StringField(), Status.Undefined, "");
        assertThat(res.getServices().get(0).getMethods())
                .contains(voidToVoid, intToInt, stringToString);
    }
}
