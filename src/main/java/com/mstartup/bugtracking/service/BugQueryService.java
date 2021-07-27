package com.mstartup.bugtracking.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mstartup.bugtracking.domain.Bug;
import com.mstartup.bugtracking.domain.*; // for static metamodels
import com.mstartup.bugtracking.repository.BugRepository;
import com.mstartup.bugtracking.service.dto.BugCriteria;
import com.mstartup.bugtracking.service.dto.BugDTO;
import com.mstartup.bugtracking.service.mapper.BugMapper;

/**
 * Service for executing complex queries for {@link Bug} entities in the database.
 * The main input is a {@link BugCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BugDTO} or a {@link Page} of {@link BugDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BugQueryService extends QueryService<Bug> {

    private final Logger log = LoggerFactory.getLogger(BugQueryService.class);

    private final BugRepository bugRepository;

    private final BugMapper bugMapper;

    public BugQueryService(BugRepository bugRepository, BugMapper bugMapper) {
        this.bugRepository = bugRepository;
        this.bugMapper = bugMapper;
    }

    /**
     * Return a {@link List} of {@link BugDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BugDTO> findByCriteria(BugCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bug> specification = createSpecification(criteria);
        return bugMapper.toDto(bugRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BugDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BugDTO> findByCriteria(BugCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bug> specification = createSpecification(criteria);
        return bugRepository.findAll(specification, page)
            .map(bugMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BugCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bug> specification = createSpecification(criteria);
        return bugRepository.count(specification);
    }

    /**
     * Function to convert {@link BugCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bug> createSpecification(BugCriteria criteria) {
        Specification<Bug> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bug_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Bug_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Bug_.description));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), Bug_.priority));
            }
            if (criteria.getIsResolved() != null) {
                specification = specification.and(buildSpecification(criteria.getIsResolved(), Bug_.isResolved));
            }
            if (criteria.getClosedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosedAt(), Bug_.closedAt));
            }
            if (criteria.getReopenedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReopenedAt(), Bug_.reopenedAt));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(Bug_.project, JoinType.LEFT).get(Project_.id)));
            }
            if (criteria.getNotesId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotesId(),
                    root -> root.join(Bug_.notes, JoinType.LEFT).get(Note_.id)));
            }
            if (criteria.getClosedById() != null) {
                specification = specification.and(buildSpecification(criteria.getClosedById(),
                    root -> root.join(Bug_.closedBy, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getReopenedById() != null) {
                specification = specification.and(buildSpecification(criteria.getReopenedById(),
                    root -> root.join(Bug_.reopenedBy, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
