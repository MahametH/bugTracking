package com.mstartup.bugtracking.service.mapper;


import com.mstartup.bugtracking.domain.*;
import com.mstartup.bugtracking.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {


    @Mapping(target = "members", ignore = true)
    @Mapping(target = "removeMembers", ignore = true)
    @Mapping(target = "bugs", ignore = true)
    @Mapping(target = "removeBugs", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
