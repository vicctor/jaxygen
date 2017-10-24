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
package org.jaxygen.netserviceapisample.business.dto.default_impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jaxygen.dto.Response;
import org.jaxygen.customimplementation.CustomTypeAdapterFactory;
import org.jaxygen.util.BeanUtil;
import org.junit.After;
import org.junit.Test;

/**
 *
 * @author xnet
 */
public class TestInterfaceRequestTest {

    @After
    public void tearDown() {
    }

//    @Test
    public void testInterface() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new CustomTypeAdapterFactory()).create();

        String serialized = "{foo : {implementationClass : \"org.jaxygen.netserviceapisample.business.dto.default_impl.FooInterfaceImpl\", dto : { bar : \"wewnętrzne pole :D\"} },  baseStringField : \"adakjdkajsd\" }";
        TestInterfaceRequest fromJson = gson.fromJson(serialized, TestInterfaceRequest.class);

        TestInterfaceRequest request = new TestInterfaceRequest();

        request.setBaseStringField("jgfhgkjk");
        FooInterfaceImpl foo = new FooInterfaceImpl();
        foo.setBar("fkjdshkjfsdhkj");
        request.setFoo(foo);

        TestInterfaceResponse resp = new TestInterfaceResponse();
        BeanUtil.translateBean(request, resp);

        Response responseWraper = new Response(TestInterfaceResponse.class, resp);

        System.out.println(gson.toJson(responseWraper));

    }

    @Test
    public void testttt() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new CustomTypeAdapterFactory()).setPrettyPrinting().create();

//        MyTestClass testClass = new MyTestClass();
//        testClass.setFoos(Lists.newArrayList(
//                new FooInterfaceImpl("jestem impl"),
//                new FooInterfaceImpl2("jestem impl2")
//        ));
//        Map<String, FooAbstract> map = new HashMap();
//        map.put("1", new FooAbstractImpl("jeden"));
//        map.put("2", new FooAbstractImpl("dwa"));
//        testClass.setFooMap(map);
//        System.out.println(gson.toJson(testClass));

        String serialized = "{\n"
                + "	\"foos\": [\n"
                + "		{\n"
                + "			\"implementationClass\": \"org.jaxygen.netserviceapisample.business.dto.default_impl.FooInterfaceImpl\",\n"
                + "			\"dto\": {\n"
                + "				\"bar\": \"jestem impl\"\n"
                + "			}\n"
                + "		},\n"
                + "		{\n"
                + "			\"implementationClass\": \"org.jaxygen.netserviceapisample.business.dto.default_impl.FooInterfaceImpl2\",\n"
                + "			\"dto\": {\n"
                + "				\"bar\": \"jestem impl2\"\n"
                + "			}\n"
                + "		}\n"
                + "	],\n"
                + "	\"fooMap\": {\n"
                + "	\"1\":\n"
                + "		{\n"
                + "			\"implementationClass\": \"org.jaxygen.netserviceapisample.business.dto.default_impl.FooAbstractImpl\",\n"
                + "			\"dto\": {\n"
                + "				\"bar\": \"foo map 1\"\n"
                + "			}\n"
                + "		},\n"
                + "		\"2\" : {\n"
                + "			\"implementationClass\": \"org.jaxygen.netserviceapisample.business.dto.default_impl.FooAbstractImpl\",\n"
                + "			\"dto\": {\n"
                + "				\"bar\": \"foo map 2\"\n"
                + "			}\n"
                + "		}\n"
                + "	}\n"
                + "}";
        MyTestClass fromJson = gson.fromJson(serialized, MyTestClass.class);
//        String serialized = "{\n"
//                + "			\"implementationClass\": \"org.jaxygen.netserviceapisample.business.dto.default_impl.FooAbstractImpl\",\n"
//                + "			\"dto\": {\n"
//                + "				\"bar\": \"foo map 1\"\n"
//                + "			}\n"
//                + "		}";
//
//        FooAbstract fromJson = gson.fromJson(serialized, FooAbstract.class);
        System.out.println("parsed: " + fromJson);

        TestInterfaceRequest request = new TestInterfaceRequest();

        request.setBaseStringField("jgfhgkjk");
        FooInterfaceImpl foo = new FooInterfaceImpl();
        foo.setBar("fkjdshkjfsdhkj");
        request.setFoo(foo);

        TestInterfaceResponse resp = new TestInterfaceResponse();
        BeanUtil.translateBean(request, resp);

        Response responseWraper = new Response(TestInterfaceResponse.class, resp);

        System.out.println(gson.toJson(responseWraper));

    }


}
