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

/**
 * Criteria class for the {@link com.mstartup.bugtracking.domain.Project} entity. This class is used
 * in {@link com.mstartup.bugtracking.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter membersId;

    private LongFilter bugsId;

    public ProjectCriteria() {
    }

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.membersId = other.membersId == null ? null : other.membersId.copy();
        this.bugsId = other.bugsId == null ? null : other.bugsId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getMembersId() {
        return membersId;
    }

    public void setMembersId(LongFilter membersId) {
        this.membersId = membersId;
    }

    public LongFilter getBugsId() {
        return bugsId;
    }

    public void setBugsId(LongFilter bugsId) {
        this.bugsId = bugsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(membersId, that.membersId) &&
            Objects.equals(bugsId, that.bugsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        membersId,
        bugsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (membersId != null ? "membersId=" + membersId + ", " : "") +
                (bugsId != null ? "bugsId=" + bugsId + ", " : "") +
            "}";
    }

}
