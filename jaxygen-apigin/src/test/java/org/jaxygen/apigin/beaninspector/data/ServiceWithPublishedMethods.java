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
package org.jaxygen.apigin.beaninspector.data;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;

/**
 *
 * @author Artur
 */
@NetAPI(description = "Service used for testing", status = Status.GenerallyAvailable)
public class ServiceWithPublishedMethods {
    @NetAPI(description = "void_void")
    public void voidTovoidTest() {}
    
    @NetAPI(description = "int_int")
    public int intToIntTest(int i) {return 0;}
    
    @NetAPI(description = "string_string")
    public String stringToStringTest(String s) {return s;}
}
