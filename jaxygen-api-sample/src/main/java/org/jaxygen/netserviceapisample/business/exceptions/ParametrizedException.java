/*
 * Copyright 2014 Artur.
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

package org.jaxygen.netserviceapisample.business.exceptions;

import org.jaxygen.annotations.NetAPI;

/**This exception demonstrates how to pass an extended argument to
 * exception DTO response sent to the client
 *
 * @author Artur
 */
public class ParametrizedException extends Exception {
    private String customMessage;

    // Value of this property will be visible in exception DTO response
    @NetAPI
    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessa) {
        this.customMessage = customMessa;
    }
}
