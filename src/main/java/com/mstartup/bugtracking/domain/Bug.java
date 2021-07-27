package com.mstartup.bugtracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.mstartup.bugtracking.domain.enumeration.Priority;

/**
 * A Bug.
 */
@Entity
@Table(name = "bug")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "is_resolved")
    private Boolean isResolved;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "reopened_at")
    private Instant reopenedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "bugs", allowSetters = true)
    private Project project;

    @OneToMany(mappedBy = "bug")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Note> notes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "bugs", allowSetters = true)
    private User closedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "bugs", allowSetters = true)
    private User reopenedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Bug title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Bug description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Bug priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Boolean isIsResolved() {
        return isResolved;
    }

    public Bug isResolved(Boolean isResolved) {
        this.isResolved = isResolved;
        return this;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public Bug closedAt(Instant closedAt) {
        this.closedAt = closedAt;
        return this;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getReopenedAt() {
        return reopenedAt;
    }

    public Bug reopenedAt(Instant reopenedAt) {
        this.reopenedAt = reopenedAt;
        return this;
    }

    public void setReopenedAt(Instant reopenedAt) {
        this.reopenedAt = reopenedAt;
    }

    public Project getProject() {
        return project;
    }

    public Bug project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public Bug notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    public Bug addNotes(Note note) {
        this.notes.add(note);
        note.setBug(this);
        return this;
    }

    public Bug removeNotes(Note note) {
        this.notes.remove(note);
        note.setBug(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public User getClosedBy() {
        return closedBy;
    }

    public Bug closedBy(User user) {
        this.closedBy = user;
        return this;
    }

    public void setClosedBy(User user) {
        this.closedBy = user;
    }

    public User getReopenedBy() {
        return reopenedBy;
    }

    public Bug reopenedBy(User user) {
        this.reopenedBy = user;
        return this;
    }

    public void setReopenedBy(User user) {
        this.reopenedBy = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bug)) {
            return false;
        }
        return id != null && id.equals(((Bug) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bug{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", isResolved='" + isIsResolved() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", reopenedAt='" + getReopenedAt() + "'" +
            "}";
    }
}
