package com.mstartup.bugtracking.web.rest;

import com.mstartup.bugtracking.BugTrackingApp;
import com.mstartup.bugtracking.domain.Bug;
import com.mstartup.bugtracking.domain.Project;
import com.mstartup.bugtracking.domain.Note;
import com.mstartup.bugtracking.domain.User;
import com.mstartup.bugtracking.repository.BugRepository;
import com.mstartup.bugtracking.service.BugService;
import com.mstartup.bugtracking.service.dto.BugDTO;
import com.mstartup.bugtracking.service.mapper.BugMapper;
import com.mstartup.bugtracking.service.dto.BugCriteria;
import com.mstartup.bugtracking.service.BugQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mstartup.bugtracking.domain.enumeration.Priority;
/**
 * Integration tests for the {@link BugResource} REST controller.
 */
@SpringBootTest(classes = BugTrackingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BugResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Priority DEFAULT_PRIORITY = Priority.LOW;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final Boolean DEFAULT_IS_RESOLVED = false;
    private static final Boolean UPDATED_IS_RESOLVED = true;

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REOPENED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REOPENED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private BugMapper bugMapper;

    @Autowired
    private BugService bugService;

    @Autowired
    private BugQueryService bugQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBugMockMvc;

    private Bug bug;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bug createEntity(EntityManager em) {
        Bug bug = new Bug()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .isResolved(DEFAULT_IS_RESOLVED)
            .closedAt(DEFAULT_CLOSED_AT)
            .reopenedAt(DEFAULT_REOPENED_AT);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        bug.setProject(project);
        return bug;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bug createUpdatedEntity(EntityManager em) {
        Bug bug = new Bug()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isResolved(UPDATED_IS_RESOLVED)
            .closedAt(UPDATED_CLOSED_AT)
            .reopenedAt(UPDATED_REOPENED_AT);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        bug.setProject(project);
        return bug;
    }

    @BeforeEach
    public void initTest() {
        bug = createEntity(em);
    }

    @Test
    @Transactional
    public void createBug() throws Exception {
        int databaseSizeBeforeCreate = bugRepository.findAll().size();
        // Create the Bug
        BugDTO bugDTO = bugMapper.toDto(bug);
        restBugMockMvc.perform(post("/api/bugs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bugDTO)))
            .andExpect(status().isCreated());

        // Validate the Bug in the database
        List<Bug> bugList = bugRepository.findAll();
        assertThat(bugList).hasSize(databaseSizeBeforeCreate + 1);
        Bug testBug = bugList.get(bugList.size() - 1);
        assertThat(testBug.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBug.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBug.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testBug.isIsResolved()).isEqualTo(DEFAULT_IS_RESOLVED);
        assertThat(testBug.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testBug.getReopenedAt()).isEqualTo(DEFAULT_REOPENED_AT);
    }

    @Test
    @Transactional
    public void createBugWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bugRepository.findAll().size();

        // Create the Bug with an existing ID
        bug.setId(1L);
        BugDTO bugDTO = bugMapper.toDto(bug);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBugMockMvc.perform(post("/api/bugs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bugDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bug in the database
        List<Bug> bugList = bugRepository.findAll();
        assertThat(bugList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBugs() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList
        restBugMockMvc.perform(get("/api/bugs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bug.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].isResolved").value(hasItem(DEFAULT_IS_RESOLVED.booleanValue())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].reopenedAt").value(hasItem(DEFAULT_REOPENED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getBug() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get the bug
        restBugMockMvc.perform(get("/api/bugs/{id}", bug.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bug.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.isResolved").value(DEFAULT_IS_RESOLVED.booleanValue()))
            .andExpect(jsonPath("$.closedAt").value(DEFAULT_CLOSED_AT.toString()))
            .andExpect(jsonPath("$.reopenedAt").value(DEFAULT_REOPENED_AT.toString()));
    }


    @Test
    @Transactional
    public void getBugsByIdFiltering() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        Long id = bug.getId();

        defaultBugShouldBeFound("id.equals=" + id);
        defaultBugShouldNotBeFound("id.notEquals=" + id);

        defaultBugShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBugShouldNotBeFound("id.greaterThan=" + id);

        defaultBugShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBugShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBugsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where title equals to DEFAULT_TITLE
        defaultBugShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the bugList where title equals to UPDATED_TITLE
        defaultBugShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBugsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where title not equals to DEFAULT_TITLE
        defaultBugShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the bugList where title not equals to UPDATED_TITLE
        defaultBugShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBugsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBugShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the bugList where title equals to UPDATED_TITLE
        defaultBugShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBugsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where title is not null
        defaultBugShouldBeFound("title.specified=true");

        // Get all the bugList where title is null
        defaultBugShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllBugsByTitleContainsSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where title contains DEFAULT_TITLE
        defaultBugShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the bugList where title contains UPDATED_TITLE
        defaultBugShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBugsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where title does not contain DEFAULT_TITLE
        defaultBugShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the bugList where title does not contain UPDATED_TITLE
        defaultBugShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllBugsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where description equals to DEFAULT_DESCRIPTION
        defaultBugShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the bugList where description equals to UPDATED_DESCRIPTION
        defaultBugShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBugsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where description not equals to DEFAULT_DESCRIPTION
        defaultBugShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the bugList where description not equals to UPDATED_DESCRIPTION
        defaultBugShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBugsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBugShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the bugList where description equals to UPDATED_DESCRIPTION
        defaultBugShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBugsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where description is not null
        defaultBugShouldBeFound("description.specified=true");

        // Get all the bugList where description is null
        defaultBugShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllBugsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where description contains DEFAULT_DESCRIPTION
        defaultBugShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the bugList where description contains UPDATED_DESCRIPTION
        defaultBugShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBugsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where description does not contain DEFAULT_DESCRIPTION
        defaultBugShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the bugList where description does not contain UPDATED_DESCRIPTION
        defaultBugShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllBugsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where priority equals to DEFAULT_PRIORITY
        defaultBugShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the bugList where priority equals to UPDATED_PRIORITY
        defaultBugShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllBugsByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where priority not equals to DEFAULT_PRIORITY
        defaultBugShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the bugList where priority not equals to UPDATED_PRIORITY
        defaultBugShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllBugsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultBugShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the bugList where priority equals to UPDATED_PRIORITY
        defaultBugShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllBugsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where priority is not null
        defaultBugShouldBeFound("priority.specified=true");

        // Get all the bugList where priority is null
        defaultBugShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    public void getAllBugsByIsResolvedIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where isResolved equals to DEFAULT_IS_RESOLVED
        defaultBugShouldBeFound("isResolved.equals=" + DEFAULT_IS_RESOLVED);

        // Get all the bugList where isResolved equals to UPDATED_IS_RESOLVED
        defaultBugShouldNotBeFound("isResolved.equals=" + UPDATED_IS_RESOLVED);
    }

    @Test
    @Transactional
    public void getAllBugsByIsResolvedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where isResolved not equals to DEFAULT_IS_RESOLVED
        defaultBugShouldNotBeFound("isResolved.notEquals=" + DEFAULT_IS_RESOLVED);

        // Get all the bugList where isResolved not equals to UPDATED_IS_RESOLVED
        defaultBugShouldBeFound("isResolved.notEquals=" + UPDATED_IS_RESOLVED);
    }

    @Test
    @Transactional
    public void getAllBugsByIsResolvedIsInShouldWork() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where isResolved in DEFAULT_IS_RESOLVED or UPDATED_IS_RESOLVED
        defaultBugShouldBeFound("isResolved.in=" + DEFAULT_IS_RESOLVED + "," + UPDATED_IS_RESOLVED);

        // Get all the bugList where isResolved equals to UPDATED_IS_RESOLVED
        defaultBugShouldNotBeFound("isResolved.in=" + UPDATED_IS_RESOLVED);
    }

    @Test
    @Transactional
    public void getAllBugsByIsResolvedIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where isResolved is not null
        defaultBugShouldBeFound("isResolved.specified=true");

        // Get all the bugList where isResolved is null
        defaultBugShouldNotBeFound("isResolved.specified=false");
    }

    @Test
    @Transactional
    public void getAllBugsByClosedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where closedAt equals to DEFAULT_CLOSED_AT
        defaultBugShouldBeFound("closedAt.equals=" + DEFAULT_CLOSED_AT);

        // Get all the bugList where closedAt equals to UPDATED_CLOSED_AT
        defaultBugShouldNotBeFound("closedAt.equals=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllBugsByClosedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where closedAt not equals to DEFAULT_CLOSED_AT
        defaultBugShouldNotBeFound("closedAt.notEquals=" + DEFAULT_CLOSED_AT);

        // Get all the bugList where closedAt not equals to UPDATED_CLOSED_AT
        defaultBugShouldBeFound("closedAt.notEquals=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllBugsByClosedAtIsInShouldWork() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where closedAt in DEFAULT_CLOSED_AT or UPDATED_CLOSED_AT
        defaultBugShouldBeFound("closedAt.in=" + DEFAULT_CLOSED_AT + "," + UPDATED_CLOSED_AT);

        // Get all the bugList where closedAt equals to UPDATED_CLOSED_AT
        defaultBugShouldNotBeFound("closedAt.in=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllBugsByClosedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where closedAt is not null
        defaultBugShouldBeFound("closedAt.specified=true");

        // Get all the bugList where closedAt is null
        defaultBugShouldNotBeFound("closedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllBugsByReopenedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where reopenedAt equals to DEFAULT_REOPENED_AT
        defaultBugShouldBeFound("reopenedAt.equals=" + DEFAULT_REOPENED_AT);

        // Get all the bugList where reopenedAt equals to UPDATED_REOPENED_AT
        defaultBugShouldNotBeFound("reopenedAt.equals=" + UPDATED_REOPENED_AT);
    }

    @Test
    @Transactional
    public void getAllBugsByReopenedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where reopenedAt not equals to DEFAULT_REOPENED_AT
        defaultBugShouldNotBeFound("reopenedAt.notEquals=" + DEFAULT_REOPENED_AT);

        // Get all the bugList where reopenedAt not equals to UPDATED_REOPENED_AT
        defaultBugShouldBeFound("reopenedAt.notEquals=" + UPDATED_REOPENED_AT);
    }

    @Test
    @Transactional
    public void getAllBugsByReopenedAtIsInShouldWork() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where reopenedAt in DEFAULT_REOPENED_AT or UPDATED_REOPENED_AT
        defaultBugShouldBeFound("reopenedAt.in=" + DEFAULT_REOPENED_AT + "," + UPDATED_REOPENED_AT);

        // Get all the bugList where reopenedAt equals to UPDATED_REOPENED_AT
        defaultBugShouldNotBeFound("reopenedAt.in=" + UPDATED_REOPENED_AT);
    }

    @Test
    @Transactional
    public void getAllBugsByReopenedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        // Get all the bugList where reopenedAt is not null
        defaultBugShouldBeFound("reopenedAt.specified=true");

        // Get all the bugList where reopenedAt is null
        defaultBugShouldNotBeFound("reopenedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllBugsByProjectIsEqualToSomething() throws Exception {
        // Get already existing entity
        Project project = bug.getProject();
        bugRepository.saveAndFlush(bug);
        Long projectId = project.getId();

        // Get all the bugList where project equals to projectId
        defaultBugShouldBeFound("projectId.equals=" + projectId);

        // Get all the bugList where project equals to projectId + 1
        defaultBugShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }


    @Test
    @Transactional
    public void getAllBugsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);
        Note notes = NoteResourceIT.createEntity(em);
        em.persist(notes);
        em.flush();
        bug.addNotes(notes);
        bugRepository.saveAndFlush(bug);
        Long notesId = notes.getId();

        // Get all the bugList where notes equals to notesId
        defaultBugShouldBeFound("notesId.equals=" + notesId);

        // Get all the bugList where notes equals to notesId + 1
        defaultBugShouldNotBeFound("notesId.equals=" + (notesId + 1));
    }


    @Test
    @Transactional
    public void getAllBugsByClosedByIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);
        User closedBy = UserResourceIT.createEntity(em);
        em.persist(closedBy);
        em.flush();
        bug.setClosedBy(closedBy);
        bugRepository.saveAndFlush(bug);
        Long closedById = closedBy.getId();

        // Get all the bugList where closedBy equals to closedById
        defaultBugShouldBeFound("closedById.equals=" + closedById);

        // Get all the bugList where closedBy equals to closedById + 1
        defaultBugShouldNotBeFound("closedById.equals=" + (closedById + 1));
    }


    @Test
    @Transactional
    public void getAllBugsByReopenedByIsEqualToSomething() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);
        User reopenedBy = UserResourceIT.createEntity(em);
        em.persist(reopenedBy);
        em.flush();
        bug.setReopenedBy(reopenedBy);
        bugRepository.saveAndFlush(bug);
        Long reopenedById = reopenedBy.getId();

        // Get all the bugList where reopenedBy equals to reopenedById
        defaultBugShouldBeFound("reopenedById.equals=" + reopenedById);

        // Get all the bugList where reopenedBy equals to reopenedById + 1
        defaultBugShouldNotBeFound("reopenedById.equals=" + (reopenedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBugShouldBeFound(String filter) throws Exception {
        restBugMockMvc.perform(get("/api/bugs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bug.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].isResolved").value(hasItem(DEFAULT_IS_RESOLVED.booleanValue())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].reopenedAt").value(hasItem(DEFAULT_REOPENED_AT.toString())));

        // Check, that the count call also returns 1
        restBugMockMvc.perform(get("/api/bugs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBugShouldNotBeFound(String filter) throws Exception {
        restBugMockMvc.perform(get("/api/bugs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBugMockMvc.perform(get("/api/bugs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBug() throws Exception {
        // Get the bug
        restBugMockMvc.perform(get("/api/bugs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBug() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        int databaseSizeBeforeUpdate = bugRepository.findAll().size();

        // Update the bug
        Bug updatedBug = bugRepository.findById(bug.getId()).get();
        // Disconnect from session so that the updates on updatedBug are not directly saved in db
        em.detach(updatedBug);
        updatedBug
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isResolved(UPDATED_IS_RESOLVED)
            .closedAt(UPDATED_CLOSED_AT)
            .reopenedAt(UPDATED_REOPENED_AT);
        BugDTO bugDTO = bugMapper.toDto(updatedBug);

        restBugMockMvc.perform(put("/api/bugs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bugDTO)))
            .andExpect(status().isOk());

        // Validate the Bug in the database
        List<Bug> bugList = bugRepository.findAll();
        assertThat(bugList).hasSize(databaseSizeBeforeUpdate);
        Bug testBug = bugList.get(bugList.size() - 1);
        assertThat(testBug.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBug.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBug.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testBug.isIsResolved()).isEqualTo(UPDATED_IS_RESOLVED);
        assertThat(testBug.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testBug.getReopenedAt()).isEqualTo(UPDATED_REOPENED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingBug() throws Exception {
        int databaseSizeBeforeUpdate = bugRepository.findAll().size();

        // Create the Bug
        BugDTO bugDTO = bugMapper.toDto(bug);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBugMockMvc.perform(put("/api/bugs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bugDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bug in the database
        List<Bug> bugList = bugRepository.findAll();
        assertThat(bugList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBug() throws Exception {
        // Initialize the database
        bugRepository.saveAndFlush(bug);

        int databaseSizeBeforeDelete = bugRepository.findAll().size();

        // Delete the bug
        restBugMockMvc.perform(delete("/api/bugs/{id}", bug.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bug> bugList = bugRepository.findAll();
        assertThat(bugList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
