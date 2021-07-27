package com.mstartup.bugtracking.service.mapper;


import com.mstartup.bugtracking.domain.*;
import com.mstartup.bugtracking.service.dto.BugDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bug} and its DTO {@link BugDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class})
public interface BugMapper extends EntityMapper<BugDTO, Bug> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "closedBy.id", target = "closedById")
    @Mapping(source = "closedBy.login", target = "closedByLogin")
    @Mapping(source = "reopenedBy.id", target = "reopenedById")
    @Mapping(source = "reopenedBy.login", target = "reopenedByLogin")
    BugDTO toDto(Bug bug);

    @Mapping(source = "projectId", target = "project")
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "removeNotes", ignore = true)
    @Mapping(source = "closedById", target = "closedBy")
    @Mapping(source = "reopenedById", target = "reopenedBy")
    Bug toEntity(BugDTO bugDTO);

    default Bug fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bug bug = new Bug();
        bug.setId(id);
        return bug;
    }
}
