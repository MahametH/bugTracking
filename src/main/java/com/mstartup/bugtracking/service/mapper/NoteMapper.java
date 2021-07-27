package com.mstartup.bugtracking.service.mapper;


import com.mstartup.bugtracking.domain.*;
import com.mstartup.bugtracking.service.dto.NoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BugMapper.class})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.login", target = "authorLogin")
    @Mapping(source = "bug.id", target = "bugId")
    @Mapping(source = "bug.title", target = "bugTitle")
    NoteDTO toDto(Note note);

    @Mapping(source = "authorId", target = "author")
    @Mapping(source = "bugId", target = "bug")
    Note toEntity(NoteDTO noteDTO);

    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
