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
 * Criteria class for the {@link com.mstartup.bugtracking.domain.Note} entity. This class is used
 * in {@link com.mstartup.bugtracking.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter body;

    private LongFilter authorId;

    private LongFilter bugId;

    public NoteCriteria() {
    }

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.body = other.body == null ? null : other.body.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.bugId = other.bugId == null ? null : other.bugId.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBody() {
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
    }

    public LongFilter getBugId() {
        return bugId;
    }

    public void setBugId(LongFilter bugId) {
        this.bugId = bugId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NoteCriteria that = (NoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(body, that.body) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(bugId, that.bugId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        body,
        authorId,
        bugId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (body != null ? "body=" + body + ", " : "") +
                (authorId != null ? "authorId=" + authorId + ", " : "") +
                (bugId != null ? "bugId=" + bugId + ", " : "") +
            "}";
    }

}
