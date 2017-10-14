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

import com.google.inject.Inject;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.apibroker.dao.ProjectsDAO;
import org.jaxygen.apibroker.dao.filters.ProjectsFilter;
import org.jaxygen.apibroker.dto.common.DelteEntityRequestDTO;
import org.jaxygen.apibroker.dto.projects.ProjectDTO;
import org.jaxygen.apibroker.dto.projects.requests.ProjectRegisterRequestDTO;
import org.jaxygen.apibroker.dto.projects.requests.ProjectUpdateRequestDTO;
import org.jaxygen.apibroker.dto.projects.requests.ProjectsListRequestDTO;
import org.jaxygen.apibroker.dto.projects.responses.ProjectsListDTO;
import org.jaxygen.apibroker.entities.ProjectEntity;
import org.jaxygen.apibroker.entities.collections.ProjectEntityList;
import org.jaxygen.frame.Converters;

/**
 *
 * @author Artur
 */
@NetAPI(description = "This service keeps track on the services managed by the APIBrowser2")
public class ProjectsRepostoryService {

    @Inject
    private ProjectsDAO projectsDAO;

    ProjectDTO mockProject = new ProjectDTO();

    public ProjectsRepostoryService() {
        mockProject.setId(1);
        mockProject.setName("The name is mandatory, and must be provided by user");
        mockProject.setDescription("This is just static sample, to demonstrate how the Project entity looks like.");
    }

    @NetAPI(description = "Get list os projects registered to the current user", status = Status.Mockup)
    public ProjectsListDTO getMyProjects(ProjectsListRequestDTO request) {
        ProjectsFilter filter = Converters.convert(request, ProjectsFilter.class);
        ProjectEntityList projects = projectsDAO.getProjects(filter);
        return Converters.convert(projects, ProjectsListDTO.class);
    }

    @NetAPI(description = "Add new server to my projects repository", status = Status.Mockup)
    public ProjectDTO registerProject(ProjectRegisterRequestDTO request) {
        ProjectEntity projectEntity = projectsDAO.createProject(request);
        return Converters.convert(projectEntity, ProjectDTO.class);
    }

    @NetAPI(description = "Update existing service", status = Status.Mockup)
    public ProjectDTO updateProject(ProjectUpdateRequestDTO request) {
        return mockProject;
    }

    @NetAPI(description = "Remove server from repository", status = Status.Mockup)
    public ProjectDTO removeProject(DelteEntityRequestDTO request) {
        return mockProject;
    }
}
