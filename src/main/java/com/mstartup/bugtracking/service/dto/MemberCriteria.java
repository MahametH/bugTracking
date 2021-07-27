package com.mstartup.bugtracking.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mstartup.bugtracking.domain.Member} entity. This class is used
 * in {@link com.mstartup.bugtracking.web.rest.MemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter joinedAt;

    private LongFilter projectId;

    public MemberCriteria() {
    }

    public MemberCriteria(MemberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.joinedAt = other.joinedAt == null ? null : other.joinedAt.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public MemberCriteria copy() {
        return new MemberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(InstantFilter joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberCriteria that = (MemberCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(joinedAt, that.joinedAt) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        joinedAt,
        projectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (joinedAt != null ? "joinedAt=" + joinedAt + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
