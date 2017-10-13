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
package org.jaxygen.apibroker.dao;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import org.jaxygen.apibroker.dao.filters.ProjectsFilter;
import org.jaxygen.apibroker.entities.ProjectEntity;
import org.jaxygen.apibroker.model.Project;
import org.jaxygen.collections.PartialList;

/**
 *
 * @author Artur
 */
public class ProjectsDAO {

    @Inject
    private EntityManager em;

    ProjectEntity createProject(Project project) {
        em.persist(project);
        return null;
    }
    
    PartialList<ProjectEntity> getProjects(ProjectsFilter filter) {
        return null;
    }
}
