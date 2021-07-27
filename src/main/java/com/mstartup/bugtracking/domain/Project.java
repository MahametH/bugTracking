package com.mstartup.bugtracking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Member> members = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Bug> bugs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public Project members(Set<Member> members) {
        this.members = members;
        return this;
    }

    public Project addMembers(Member member) {
        this.members.add(member);
        member.setProject(this);
        return this;
    }

    public Project removeMembers(Member member) {
        this.members.remove(member);
        member.setProject(null);
        return this;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Set<Bug> getBugs() {
        return bugs;
    }

    public Project bugs(Set<Bug> bugs) {
        this.bugs = bugs;
        return this;
    }

    public Project addBugs(Bug bug) {
        this.bugs.add(bug);
        bug.setProject(this);
        return this;
    }

    public Project removeBugs(Bug bug) {
        this.bugs.remove(bug);
        bug.setProject(null);
        return this;
    }

    public void setBugs(Set<Bug> bugs) {
        this.bugs = bugs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
