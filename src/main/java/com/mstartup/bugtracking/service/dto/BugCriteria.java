package com.mstartup.bugtracking.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mstartup.bugtracking.domain.enumeration.Priority;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mstartup.bugtracking.domain.Bug} entity. This class is used
 * in {@link com.mstartup.bugtracking.web.rest.BugResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bugs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BugCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {
        }

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private PriorityFilter priority;

    private BooleanFilter isResolved;

    private InstantFilter closedAt;

    private InstantFilter reopenedAt;

    private LongFilter projectId;

    private LongFilter notesId;

    private LongFilter closedById;

    private LongFilter reopenedById;

    public BugCriteria() {
    }

    public BugCriteria(BugCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.isResolved = other.isResolved == null ? null : other.isResolved.copy();
        this.closedAt = other.closedAt == null ? null : other.closedAt.copy();
        this.reopenedAt = other.reopenedAt == null ? null : other.reopenedAt.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.notesId = other.notesId == null ? null : other.notesId.copy();
        this.closedById = other.closedById == null ? null : other.closedById.copy();
        this.reopenedById = other.reopenedById == null ? null : other.reopenedById.copy();
    }

    @Override
    public BugCriteria copy() {
        return new BugCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public PriorityFilter getPriority() {
        return priority;
    }

    public void setPriority(PriorityFilter priority) {
        this.priority = priority;
    }

    public BooleanFilter getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(BooleanFilter isResolved) {
        this.isResolved = isResolved;
    }

    public InstantFilter getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(InstantFilter closedAt) {
        this.closedAt = closedAt;
    }

    public InstantFilter getReopenedAt() {
        return reopenedAt;
    }

    public void setReopenedAt(InstantFilter reopenedAt) {
        this.reopenedAt = reopenedAt;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getNotesId() {
        return notesId;
    }

    public void setNotesId(LongFilter notesId) {
        this.notesId = notesId;
    }

    public LongFilter getClosedById() {
        return closedById;
    }

    public void setClosedById(LongFilter closedById) {
        this.closedById = closedById;
    }

    public LongFilter getReopenedById() {
        return reopenedById;
    }

    public void setReopenedById(LongFilter reopenedById) {
        this.reopenedById = reopenedById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BugCriteria that = (BugCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(isResolved, that.isResolved) &&
            Objects.equals(closedAt, that.closedAt) &&
            Objects.equals(reopenedAt, that.reopenedAt) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(notesId, that.notesId) &&
            Objects.equals(closedById, that.closedById) &&
            Objects.equals(reopenedById, that.reopenedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        priority,
        isResolved,
        closedAt,
        reopenedAt,
        projectId,
        notesId,
        closedById,
        reopenedById
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BugCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (isResolved != null ? "isResolved=" + isResolved + ", " : "") +
                (closedAt != null ? "closedAt=" + closedAt + ", " : "") +
                (reopenedAt != null ? "reopenedAt=" + reopenedAt + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
                (notesId != null ? "notesId=" + notesId + ", " : "") +
                (closedById != null ? "closedById=" + closedById + ", " : "") +
                (reopenedById != null ? "reopenedById=" + reopenedById + ", " : "") +
            "}";
    }

}
