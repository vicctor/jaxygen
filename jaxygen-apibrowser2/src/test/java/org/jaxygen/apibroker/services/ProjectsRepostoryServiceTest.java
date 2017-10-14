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

import org.assertj.core.api.Assertions;
import org.jaxygen.apibroker.dto.projects.requests.ProjectRegisterRequestDTO;
import org.jaxygen.apibroker.dto.projects.requests.ProjectsListRequestDTO;
import org.jaxygen.apibroker.dto.projects.responses.ProjectsListDTO;
import org.jaxygen.frame.JaxygenApplication;
import org.jaxygen.objectsbuilder.ObjectBuilderFactory;
import org.jaxygen.objectsbuilder.exceptions.ObjectCreateError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class ProjectsRepostoryServiceTest {

    JaxygenApplication app;

    @Before
    public void before() {
        app = new JaxygenApplication();
        app.start();
    }

    @After
    public void after() {
        app.stop();
    }

    public ProjectsRepostoryServiceTest() {
    }

    @Test
    public void shall_createAndLoadProject() throws ObjectCreateError {
        // given
        ProjectRegisterRequestDTO registerRequest = new ProjectRegisterRequestDTO();
        registerRequest.setName("TEST0001");
        ProjectsListRequestDTO listRequestDTO = new ProjectsListRequestDTO();
        listRequestDTO.setPage(0);
        listRequestDTO.setPageSize(10);

        ProjectsRepostoryService projectsService = ObjectBuilderFactory.instance().create(ProjectsRepostoryService.class);

        // when
        for (int i = 0; i < 100; i++) {
            projectsService.registerProject(registerRequest);
        }
        ProjectsListDTO projectsList = projectsService.getMyProjects(listRequestDTO);

        // then
        Assertions.assertThat(projectsList.getSize()).isEqualTo(100);
        Assertions.assertThat(projectsList.getElements().size()).isEqualTo(10);
    }

}
