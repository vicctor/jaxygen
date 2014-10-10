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
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.EmailRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.RangeRequestDTO;

/**
 * This class demonstrates validators usage
 *
 * @author artur
 */
@NetAPI(description = "Class demonstrates NetService validators",
        status = Status.ReleaseCandidate,
        version = "1.0")
public class ValidatorsSample {

    @NetAPI(description = "String validation sample")
    public String enterEmail(EmailRequestDTO request) {
        return "Passed e-mail : " + request.getEmail() + " " + request.ip;
    }

    @NetAPI(description = "String validation sample",
            status = Status.ReleaseCandidate,
            version = "1.0")
    public String range10to100(RangeRequestDTO request) {
        return "Passed value is : " + request.getValue();
    }
}
