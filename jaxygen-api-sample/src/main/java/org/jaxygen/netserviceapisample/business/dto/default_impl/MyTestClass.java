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

import java.util.List;
import java.util.Map;

/**
 *
 * @author xnet
 */
public class MyTestClass {

    private List<FooInterface> foos;
    private Map<String, FooAbstract> fooMap;

    public List<FooInterface> getFoos() {
        return foos;
    }

    public void setFoos(List<FooInterface> foos) {
        this.foos = foos;
    }

    public Map<String, FooAbstract> getFooMap() {
        return fooMap;
    }

    public void setFooMap(Map<String, FooAbstract> fooMap) {
        this.fooMap = fooMap;
    }

}
