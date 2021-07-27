package com.mstartup.bugtracking.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mstartup.bugtracking.domain.Member} entity.
 */
public class MemberDTO implements Serializable {
    
    private Long id;

    private Instant joinedAt;


    private Long projectId;

    private String projectName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        return id != null && id.equals(((MemberDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDTO{" +
            "id=" + getId() +
            ", joinedAt='" + getJoinedAt() + "'" +
            ", projectId=" + getProjectId() +
            ", projectName='" + getProjectName() + "'" +
            "}";
    }
}
