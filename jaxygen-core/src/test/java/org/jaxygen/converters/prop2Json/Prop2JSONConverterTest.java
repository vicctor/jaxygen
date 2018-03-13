/*
 * Copyright 2017 xnet.
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
package org.jaxygen.converters.prop2Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jaxygen.converters.json.GSONBuilderFactory;
import org.jaxygen.converters.json.JSONBuilderRegistry;
import org.jaxygen.converters.prop2Json.pojos.ArrayListTestPojo;
import org.jaxygen.converters.prop2Json.pojos.ImplTestPojo;
import org.jaxygen.converters.prop2Json.pojos.Sex;
import org.jaxygen.converters.prop2Json.pojos.TestInterfaceRequest;
import org.jaxygen.converters.prop2Json.pojos.UserTestPojo;
import org.jaxygen.customimplementation.CustomTypeAdapterFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jknast
 */
public class Prop2JSONConverterTest {

    public static void setupGsonBuilder() {
        JSONBuilderRegistry.setGSONBuilder(new GSONBuilderFactory() {
            GsonBuilder builder = null;

            @Override
            public GsonBuilder createBuilder() {
                if (builder == null) {
                    builder = new GsonBuilder()
                            .registerTypeAdapterFactory(new CustomTypeAdapterFactory());
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

    @Before
    public void setUp() {

    }

    @Test
    public void testConvertPropertiesToJSON() {
        //given
        Map<String, String> properties = new HashMap();
        properties.put("baseStringField", "foo");
        String expected = "{\"baseStringField\":\"foo\"}";
        //when
        String result = Prop2JSONConverter.convertPropertiesToJSON(properties);

        //then
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testConvertPropertiesToJSON2() {
        //given
        Map<String, String> properties = new HashMap();
        properties.put("outher.stringField", "foo");
        properties.put("interfaceField<impl>org.ImplTestPojo#bar1", "pole z klasy implementującej");
        properties.put("outher.interfacesListField[0]<impl>org.ImplTestPojo#bar1", "pole w liscie");
        String expected = ""
                + "{"
                + "\"interfaceField\":{"
                + "\"implementationClass\":\"org.ImplTestPojo\","
                + "\"dto\":{"
                + "\"bar1\":\"pole z klasy implementującej\""
                + "}"
                + "},"
                + "\"outher\":{"
                + "\"stringField\":\"foo\","
                + "\"interfacesListField\":[{"
                + "\"implementationClass\":\"org.ImplTestPojo\","
                + "\"dto\":{"
                + "\"bar1\":\"pole w liscie\""
                + "}"
                + "}]}"
                + "}";
        //when
        String result = Prop2JSONConverter.convertPropertiesToJSON(properties);

        //then
//        System.out.println("exp " + expected);
//        System.out.println("res " + result);
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void test_sortToBucket() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //given
        Map<String, String> complex = new HashMap();
        complex.put("outher.stringField", "foo");
        complex.put("otherOuther.interfaceField<impl>org.ImplTestPojo#bar1", "pole z klasy implementującej");
        complex.put("outher.interfacesListField[0]<impl>org.ImplTestPojo#bar1", "pole w liscie");
        complex.put("superOuther.outher.interfacesListField[0]<impl>org.ImplTestPojo#barrrrrrr", "rrrr");

        Map<String, Map<String, String>> expected = new HashMap();
        expected.put("outher", new HashMap());
        expected.get("outher").put("interfacesListField[0]<impl>org.ImplTestPojo#bar1", "pole w liscie");
        expected.get("outher").put("stringField", "foo");
        expected.put("otherOuther", new HashMap());
        expected.get("otherOuther").put("interfaceField<impl>org.ImplTestPojo#bar1", "pole z klasy implementującej");
        expected.put("superOuther", new HashMap());
        expected.get("superOuther").put("outher.interfacesListField[0]<impl>org.ImplTestPojo#barrrrrrr", "rrrr");

        //when
        Method method = Prop2JSONConverter.class.getDeclaredMethod("sortToBucket", Map.class);
        method.setAccessible(true);
        Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) method.invoke(null, complex);

        //then
//        System.out.println("exp " + expected);
//        System.out.println("res " + result);
        Assertions.assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void testConvertPropertiesToObj() {
        //given
        Map<String, String> properties = new HashMap();
        properties.put("name", "Andrzej");
        properties.put("age", "23");
        properties.put("sex", "male");

        UserTestPojo expected = new UserTestPojo("Andrzej", 23, Sex.male);
        //when
        String resultJson = Prop2JSONConverter.convertPropertiesToJSON(properties);
        Object resultPojo = Prop2JSONConverter.convertJSONToPojo(resultJson, UserTestPojo.class);

        //then
        Assertions.assertThat(resultPojo).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void testConvertPropertiesToObj2() {
        //given

        Map<String, String> properties = new HashMap();
        properties.put("className", "org.jaxygen.converters.prop2Json.pojos.ArrayListTestPojo");
        properties.put("methodName", "arrayListOfObjectsRequest");
        properties.put("outputType", "JSONHR");
        properties.put("inputType", "PROP2JSON");
        properties.put("list[0].age", "43");
        properties.put("list[0].name", "Anita");
        properties.put("list[0].sex", "female");

        ArrayListTestPojo expected = new ArrayListTestPojo();
        expected.getList().add(new UserTestPojo("Anita", 43, Sex.female));

        //when
        String resultJson = Prop2JSONConverter.convertPropertiesToJSON(properties);
        Object resultPojo = Prop2JSONConverter.convertJSONToPojo(resultJson, ArrayListTestPojo.class);

        //then
        Assertions.assertThat(resultPojo).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void testConvertPropertiesToObj3() {
        //given

        Map<String, String> properties = new HashMap();
        properties.put("intField", "12");
        properties.put("stringField", "sialala");
        properties.put("interfaceField<impl>org.jaxygen.converters.prop2Json.pojos.ImplTestPojo#bar1", "pole z klasy implementującej");
        properties.put("interfacesListField[0]<impl>org.jaxygen.converters.prop2Json.pojos.ImplTestPojo#bar1", "pole w liscie");

        TestInterfaceRequest expected = new TestInterfaceRequest();
        expected.setIntField(12);
        expected.setStringField("sialala");
        expected.setInterfaceField(new ImplTestPojo("pole z klasy implementującej"));
        expected.getInterfacesListField().add(new ImplTestPojo("pole w liscie"));

        //when
        String resultJson = Prop2JSONConverter.convertPropertiesToJSON(properties);
        Object resultPojo = Prop2JSONConverter.convertJSONToPojo(resultJson, TestInterfaceRequest.class);

        //then
        Assertions.assertThat(resultPojo).isEqualToComparingFieldByFieldRecursively(expected);
    }

}
