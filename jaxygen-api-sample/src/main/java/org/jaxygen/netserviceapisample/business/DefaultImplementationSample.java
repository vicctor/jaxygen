/*
 * Copyright 2012 Artur Keska.
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
package org.jaxygen.netserviceapisample.business;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.netserviceapisample.business.dto.default_impl.TestAbstract;
import org.jaxygen.netserviceapisample.business.dto.default_impl.TestInterface;

/**
 *
 * @author jknast
 */
public class DefaultImplementationSample {

    @NetAPI
    public TestInterface testDefaultInterfaceImplementation(TestInterface sampleBase) {
        return sampleBase;
    }

    @NetAPI
    public TestAbstract testDefaultAbstractClassImplementation(TestAbstract sampleBase) {
        return sampleBase;
    }

}
