package com.mstartup.bugtracking.web.rest;

import com.mstartup.bugtracking.BugTrackingApp;
import com.mstartup.bugtracking.domain.Note;
import com.mstartup.bugtracking.domain.User;
import com.mstartup.bugtracking.domain.Bug;
import com.mstartup.bugtracking.repository.NoteRepository;
import com.mstartup.bugtracking.service.NoteService;
import com.mstartup.bugtracking.service.dto.NoteDTO;
import com.mstartup.bugtracking.service.mapper.NoteMapper;
import com.mstartup.bugtracking.service.dto.NoteCriteria;
import com.mstartup.bugtracking.service.NoteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@SpringBootTest(classes = BugTrackingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NoteResourceIT {

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteQueryService noteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteMockMvc;

    private Note note;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity(EntityManager em) {
        Note note = new Note()
            .body(DEFAULT_BODY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        note.setAuthor(user);
        // Add required entity
        Bug bug;
        if (TestUtil.findAll(em, Bug.class).isEmpty()) {
            bug = BugResourceIT.createEntity(em);
            em.persist(bug);
            em.flush();
        } else {
            bug = TestUtil.findAll(em, Bug.class).get(0);
        }
        note.setBug(bug);
        return note;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        Note note = new Note()
            .body(UPDATED_BODY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        note.setAuthor(user);
        // Add required entity
        Bug bug;
        if (TestUtil.findAll(em, Bug.class).isEmpty()) {
            bug = BugResourceIT.createUpdatedEntity(em);
            em.persist(bug);
            em.flush();
        } else {
            bug = TestUtil.findAll(em, Bug.class).get(0);
        }
        note.setBug(bug);
        return note;
    }

    @BeforeEach
    public void initTest() {
        note = createEntity(em);
    }

    @Test
    @Transactional
    public void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();
        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isCreated());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getBody()).isEqualTo(DEFAULT_BODY);
    }

    @Test
    @Transactional
    public void createNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note with an existing ID
        note.setId(1L);
        NoteDTO noteDTO = noteMapper.toDto(note);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)));
    }
    
    @Test
    @Transactional
    public void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY));
    }


    @Test
    @Transactional
    public void getNotesByIdFiltering() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        Long id = note.getId();

        defaultNoteShouldBeFound("id.equals=" + id);
        defaultNoteShouldNotBeFound("id.notEquals=" + id);

        defaultNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotesByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where body equals to DEFAULT_BODY
        defaultNoteShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the noteList where body equals to UPDATED_BODY
        defaultNoteShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllNotesByBodyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where body not equals to DEFAULT_BODY
        defaultNoteShouldNotBeFound("body.notEquals=" + DEFAULT_BODY);

        // Get all the noteList where body not equals to UPDATED_BODY
        defaultNoteShouldBeFound("body.notEquals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllNotesByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where body in DEFAULT_BODY or UPDATED_BODY
        defaultNoteShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the noteList where body equals to UPDATED_BODY
        defaultNoteShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllNotesByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where body is not null
        defaultNoteShouldBeFound("body.specified=true");

        // Get all the noteList where body is null
        defaultNoteShouldNotBeFound("body.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotesByBodyContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where body contains DEFAULT_BODY
        defaultNoteShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the noteList where body contains UPDATED_BODY
        defaultNoteShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllNotesByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where body does not contain DEFAULT_BODY
        defaultNoteShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the noteList where body does not contain UPDATED_BODY
        defaultNoteShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }


    @Test
    @Transactional
    public void getAllNotesByAuthorIsEqualToSomething() throws Exception {
        // Get already existing entity
        User author = note.getAuthor();
        noteRepository.saveAndFlush(note);
        Long authorId = author.getId();

        // Get all the noteList where author equals to authorId
        defaultNoteShouldBeFound("authorId.equals=" + authorId);

        // Get all the noteList where author equals to authorId + 1
        defaultNoteShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }


    @Test
    @Transactional
    public void getAllNotesByBugIsEqualToSomething() throws Exception {
        // Get already existing entity
        Bug bug = note.getBug();
        noteRepository.saveAndFlush(note);
        Long bugId = bug.getId();

        // Get all the noteList where bug equals to bugId
        defaultNoteShouldBeFound("bugId.equals=" + bugId);

        // Get all the noteList where bug equals to bugId + 1
        defaultNoteShouldNotBeFound("bugId.equals=" + (bugId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNoteShouldBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)));

        // Check, that the count call also returns 1
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNoteShouldNotBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).get();
        // Disconnect from session so that the updates on updatedNote are not directly saved in db
        em.detach(updatedNote);
        updatedNote
            .body(UPDATED_BODY);
        NoteDTO noteDTO = noteMapper.toDto(updatedNote);

        restNoteMockMvc.perform(put("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getBody()).isEqualTo(UPDATED_BODY);
    }

    @Test
    @Transactional
    public void updateNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc.perform(put("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Delete the note
        restNoteMockMvc.perform(delete("/api/notes/{id}", note.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
