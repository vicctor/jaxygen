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

import com.google.common.collect.Lists;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.apibroker.dto.common.DelteEntityRequestDTO;
import org.jaxygen.apibroker.dto.servers.ServerDTO;
import org.jaxygen.apibroker.dto.servers.requests.ServerRegisterRequestDTO;
import org.jaxygen.apibroker.dto.servers.requests.ServerUpdateRequestDTO;
import org.jaxygen.apibroker.dto.servers.requests.ServersListRequestDTO;
import org.jaxygen.apibroker.dto.servers.responses.ServersListDTO;

/**
 *
 * @author Artur
 */
@NetAPI(description = "This service keeps track on the services managed by the APIBrowser2")
public class ServersRepostoryService {

    ServerDTO mockServer = new ServerDTO();

    public ServersRepostoryService() {
        mockServer.setId(1);
        mockServer.setApiGinUrl("http://localhost:8080/api-broker");
        mockServer.setName("The name is mandatory, if not specified, should be replaced by URL");
        mockServer.setDescription("This is just static sample, to demonstrate how the Server entity looks like. Note that the apiGinURL points to the server base api url withoutl specifing service path, so the UI shoudl fill it up with the rest of the pat up to let say: http://localhost:8080/api-broker/ServiceBrowser/getModules");
    }

    @NetAPI(description = "Get list os servers registered to the current user", status = Status.Mockup)
    public ServersListDTO getServices(ServersListRequestDTO request) {
        ServersListDTO list = new ServersListDTO();
        if (request.getProjectId() != null) {
            list.setSize(request.getProjectId());    
        } else {
            list.setSize(1);
        }
        list.setElements(Lists.newArrayList(mockServer));
        return list;
    }

    @NetAPI(description = "Add new server to my servers repository", status = Status.Mockup)
    public ServerDTO registerServer(ServerRegisterRequestDTO request) {
        return mockServer;
    }
    
    @NetAPI(description = "Update existing service", status = Status.Mockup)
    public ServerDTO updateServer(ServerUpdateRequestDTO request) {
        return mockServer;
    }
    
    @NetAPI(description = "Remove server from repository", status = Status.Mockup)
    public ServerDTO removeServer(DelteEntityRequestDTO request) {
        return mockServer;
    }
}
