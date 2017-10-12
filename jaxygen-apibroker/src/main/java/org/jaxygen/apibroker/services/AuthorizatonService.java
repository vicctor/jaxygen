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
package org.jaxygen.apibroker.services;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.security.SecurityProfile;
import org.jaxygen.security.annotations.LoginMethod;
import org.jaxygen.security.annotations.LogoutMethod;
import org.jaxygen.security.basic.BasicSecurityProviderFactory;

/**
 *
 * @author Artur
 */
@NetAPI(description = "User access verification and session management functions")
public class AuthorizatonService {

    @LoginMethod
    @NetAPI(description = "Authorise user in the system")
    public SecurityProfile login() {
        return new BasicSecurityProviderFactory("guest").getProvider();
    }

    @LogoutMethod()
    @NetAPI(description = "Logout user from this server instance")
    public void logout() {
    }
}
