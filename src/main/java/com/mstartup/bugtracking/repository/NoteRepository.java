package com.mstartup.bugtracking.repository;

import com.mstartup.bugtracking.domain.Note;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {

    @Query("select note from Note note where note.author.login = ?#{principal.username}")
    List<Note> findByAuthorIsCurrentUser();
}
