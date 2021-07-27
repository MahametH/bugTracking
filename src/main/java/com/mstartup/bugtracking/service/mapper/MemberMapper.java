package com.mstartup.bugtracking.service.mapper;


import com.mstartup.bugtracking.domain.*;
import com.mstartup.bugtracking.service.dto.MemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    MemberDTO toDto(Member member);

    @Mapping(source = "projectId", target = "project")
    Member toEntity(MemberDTO memberDTO);

    default Member fromId(Long id) {
        if (id == null) {
            return null;
        }
        Member member = new Member();
        member.setId(id);
        return member;
    }
}
