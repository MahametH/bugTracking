package com.mstartup.bugtracking.service;

import com.mstartup.bugtracking.service.dto.BugDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mstartup.bugtracking.domain.Bug}.
 */
public interface BugService {

    /**
     * Save a bug.
     *
     * @param bugDTO the entity to save.
     * @return the persisted entity.
     */
    BugDTO save(BugDTO bugDTO);

    /**
     * Get all the bugs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BugDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bug.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BugDTO> findOne(Long id);

    /**
     * Delete the "id" bug.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
