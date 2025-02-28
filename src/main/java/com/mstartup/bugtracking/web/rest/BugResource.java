package com.mstartup.bugtracking.web.rest;

import com.mstartup.bugtracking.service.BugService;
import com.mstartup.bugtracking.web.rest.errors.BadRequestAlertException;
import com.mstartup.bugtracking.service.dto.BugDTO;
import com.mstartup.bugtracking.service.dto.BugCriteria;
import com.mstartup.bugtracking.service.BugQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mstartup.bugtracking.domain.Bug}.
 */
@RestController
@RequestMapping("/api")
public class BugResource {

    private final Logger log = LoggerFactory.getLogger(BugResource.class);

    private static final String ENTITY_NAME = "bug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BugService bugService;

    private final BugQueryService bugQueryService;

    public BugResource(BugService bugService, BugQueryService bugQueryService) {
        this.bugService = bugService;
        this.bugQueryService = bugQueryService;
    }

    /**
     * {@code POST  /bugs} : Create a new bug.
     *
     * @param bugDTO the bugDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bugDTO, or with status {@code 400 (Bad Request)} if the bug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bugs")
    public ResponseEntity<BugDTO> createBug(@Valid @RequestBody BugDTO bugDTO) throws URISyntaxException {
        log.debug("REST request to save Bug : {}", bugDTO);
        if (bugDTO.getId() != null) {
            throw new BadRequestAlertException("A new bug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BugDTO result = bugService.save(bugDTO);
        return ResponseEntity.created(new URI("/api/bugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bugs} : Updates an existing bug.
     *
     * @param bugDTO the bugDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bugDTO,
     * or with status {@code 400 (Bad Request)} if the bugDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bugDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bugs")
    public ResponseEntity<BugDTO> updateBug(@Valid @RequestBody BugDTO bugDTO) throws URISyntaxException {
        log.debug("REST request to update Bug : {}", bugDTO);
        if (bugDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BugDTO result = bugService.save(bugDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bugDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bugs} : get all the bugs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bugs in body.
     */
    @GetMapping("/bugs")
    public ResponseEntity<List<BugDTO>> getAllBugs(BugCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Bugs by criteria: {}", criteria);
        Page<BugDTO> page = bugQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bugs/count} : count all the bugs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bugs/count")
    public ResponseEntity<Long> countBugs(BugCriteria criteria) {
        log.debug("REST request to count Bugs by criteria: {}", criteria);
        return ResponseEntity.ok().body(bugQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bugs/:id} : get the "id" bug.
     *
     * @param id the id of the bugDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bugDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bugs/{id}")
    public ResponseEntity<BugDTO> getBug(@PathVariable Long id) {
        log.debug("REST request to get Bug : {}", id);
        Optional<BugDTO> bugDTO = bugService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bugDTO);
    }

    /**
     * {@code DELETE  /bugs/:id} : delete the "id" bug.
     *
     * @param id the id of the bugDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bugs/{id}")
    public ResponseEntity<Void> deleteBug(@PathVariable Long id) {
        log.debug("REST request to delete Bug : {}", id);
        bugService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
