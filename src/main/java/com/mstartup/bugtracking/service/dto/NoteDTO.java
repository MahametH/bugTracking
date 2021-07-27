package com.mstartup.bugtracking.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mstartup.bugtracking.domain.Note} entity.
 */
public class NoteDTO implements Serializable {
    
    private Long id;

    private String body;


    private Long authorId;

    private String authorLogin;

    private Long bugId;

    private String bugTitle;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long userId) {
        this.authorId = userId;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String userLogin) {
        this.authorLogin = userLogin;
    }

    public Long getBugId() {
        return bugId;
    }

    public void setBugId(Long bugId) {
        this.bugId = bugId;
    }

    public String getBugTitle() {
        return bugTitle;
    }

    public void setBugTitle(String bugTitle) {
        this.bugTitle = bugTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteDTO)) {
            return false;
        }

        return id != null && id.equals(((NoteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", authorId=" + getAuthorId() +
            ", authorLogin='" + getAuthorLogin() + "'" +
            ", bugId=" + getBugId() +
            ", bugTitle='" + getBugTitle() + "'" +
            "}";
    }
}
