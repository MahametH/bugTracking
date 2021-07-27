package com.mstartup.bugtracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "joined_at")
    private Instant joinedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "members", allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public Member joinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
        return this;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Project getProject() {
        return project;
    }

    public Member project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", joinedAt='" + getJoinedAt() + "'" +
            "}";
    }
}
