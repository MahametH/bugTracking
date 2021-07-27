package com.mstartup.bugtracking.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.mstartup.bugtracking.domain.enumeration.Priority;

/**
 * A DTO for the {@link com.mstartup.bugtracking.domain.Bug} entity.
 */
public class BugDTO implements Serializable {
    
    private Long id;

    private String title;

    private String description;

    private Priority priority;

    private Boolean isResolved;

    private Instant closedAt;

    private Instant reopenedAt;


    private Long projectId;

    private String projectName;

    private Long closedById;

    private String closedByLogin;

    private Long reopenedById;

    private String reopenedByLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Boolean isIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Instant getReopenedAt() {
        return reopenedAt;
    }

    public void setReopenedAt(Instant reopenedAt) {
        this.reopenedAt = reopenedAt;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getClosedById() {
        return closedById;
    }

    public void setClosedById(Long userId) {
        this.closedById = userId;
    }

    public String getClosedByLogin() {
        return closedByLogin;
    }

    public void setClosedByLogin(String userLogin) {
        this.closedByLogin = userLogin;
    }

    public Long getReopenedById() {
        return reopenedById;
    }

    public void setReopenedById(Long userId) {
        this.reopenedById = userId;
    }

    public String getReopenedByLogin() {
        return reopenedByLogin;
    }

    public void setReopenedByLogin(String userLogin) {
        this.reopenedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BugDTO)) {
            return false;
        }

        return id != null && id.equals(((BugDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BugDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", isResolved='" + isIsResolved() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", reopenedAt='" + getReopenedAt() + "'" +
            ", projectId=" + getProjectId() +
            ", projectName='" + getProjectName() + "'" +
            ", closedById=" + getClosedById() +
            ", closedByLogin='" + getClosedByLogin() + "'" +
            ", reopenedById=" + getReopenedById() +
            ", reopenedByLogin='" + getReopenedByLogin() + "'" +
            "}";
    }
}
