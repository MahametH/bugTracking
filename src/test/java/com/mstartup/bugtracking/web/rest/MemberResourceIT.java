package com.mstartup.bugtracking.web.rest;

import com.mstartup.bugtracking.BugTrackingApp;
import com.mstartup.bugtracking.domain.Member;
import com.mstartup.bugtracking.domain.Project;
import com.mstartup.bugtracking.repository.MemberRepository;
import com.mstartup.bugtracking.service.MemberService;
import com.mstartup.bugtracking.service.dto.MemberDTO;
import com.mstartup.bugtracking.service.mapper.MemberMapper;
import com.mstartup.bugtracking.service.dto.MemberCriteria;
import com.mstartup.bugtracking.service.MemberQueryService;

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

/**
 * Integration tests for the {@link MemberResource} REST controller.
 */
@SpringBootTest(classes = BugTrackingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MemberResourceIT {

    private static final Instant DEFAULT_JOINED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOINED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberQueryService memberQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .joinedAt(DEFAULT_JOINED_AT);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        member.setProject(project);
        return member;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .joinedAt(UPDATED_JOINED_AT);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        member.setProject(project);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();
        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        restMemberMockMvc.perform(post("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getJoinedAt()).isEqualTo(DEFAULT_JOINED_AT);
    }

    @Test
    @Transactional
    public void createMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc.perform(post("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].joinedAt").value(hasItem(DEFAULT_JOINED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.joinedAt").value(DEFAULT_JOINED_AT.toString()));
    }


    @Test
    @Transactional
    public void getMembersByIdFiltering() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        Long id = member.getId();

        defaultMemberShouldBeFound("id.equals=" + id);
        defaultMemberShouldNotBeFound("id.notEquals=" + id);

        defaultMemberShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMemberShouldNotBeFound("id.greaterThan=" + id);

        defaultMemberShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMemberShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMembersByJoinedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where joinedAt equals to DEFAULT_JOINED_AT
        defaultMemberShouldBeFound("joinedAt.equals=" + DEFAULT_JOINED_AT);

        // Get all the memberList where joinedAt equals to UPDATED_JOINED_AT
        defaultMemberShouldNotBeFound("joinedAt.equals=" + UPDATED_JOINED_AT);
    }

    @Test
    @Transactional
    public void getAllMembersByJoinedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where joinedAt not equals to DEFAULT_JOINED_AT
        defaultMemberShouldNotBeFound("joinedAt.notEquals=" + DEFAULT_JOINED_AT);

        // Get all the memberList where joinedAt not equals to UPDATED_JOINED_AT
        defaultMemberShouldBeFound("joinedAt.notEquals=" + UPDATED_JOINED_AT);
    }

    @Test
    @Transactional
    public void getAllMembersByJoinedAtIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where joinedAt in DEFAULT_JOINED_AT or UPDATED_JOINED_AT
        defaultMemberShouldBeFound("joinedAt.in=" + DEFAULT_JOINED_AT + "," + UPDATED_JOINED_AT);

        // Get all the memberList where joinedAt equals to UPDATED_JOINED_AT
        defaultMemberShouldNotBeFound("joinedAt.in=" + UPDATED_JOINED_AT);
    }

    @Test
    @Transactional
    public void getAllMembersByJoinedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where joinedAt is not null
        defaultMemberShouldBeFound("joinedAt.specified=true");

        // Get all the memberList where joinedAt is null
        defaultMemberShouldNotBeFound("joinedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByProjectIsEqualToSomething() throws Exception {
        // Get already existing entity
        Project project = member.getProject();
        memberRepository.saveAndFlush(member);
        Long projectId = project.getId();

        // Get all the memberList where project equals to projectId
        defaultMemberShouldBeFound("projectId.equals=" + projectId);

        // Get all the memberList where project equals to projectId + 1
        defaultMemberShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberShouldBeFound(String filter) throws Exception {
        restMemberMockMvc.perform(get("/api/members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].joinedAt").value(hasItem(DEFAULT_JOINED_AT.toString())));

        // Check, that the count call also returns 1
        restMemberMockMvc.perform(get("/api/members/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberShouldNotBeFound(String filter) throws Exception {
        restMemberMockMvc.perform(get("/api/members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberMockMvc.perform(get("/api/members/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .joinedAt(UPDATED_JOINED_AT);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        restMemberMockMvc.perform(put("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getJoinedAt()).isEqualTo(UPDATED_JOINED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc.perform(put("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Delete the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
