package com.mstartup.bugtracking.repository;

import com.mstartup.bugtracking.domain.Bug;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Bug entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BugRepository extends JpaRepository<Bug, Long>, JpaSpecificationExecutor<Bug> {

    @Query("select bug from Bug bug where bug.closedBy.login = ?#{principal.username}")
    List<Bug> findByClosedByIsCurrentUser();

    @Query("select bug from Bug bug where bug.reopenedBy.login = ?#{principal.username}")
    List<Bug> findByReopenedByIsCurrentUser();
}
