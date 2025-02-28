package com.mstartup.bugtracking.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mstartup.bugtracking.web.rest.TestUtil;

public class NoteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Note.class);
        Note note1 = new Note();
        note1.setId(1L);
        Note note2 = new Note();
        note2.setId(note1.getId());
        assertThat(note1).isEqualTo(note2);
        note2.setId(2L);
        assertThat(note1).isNotEqualTo(note2);
        note1.setId(null);
        assertThat(note1).isNotEqualTo(note2);
    }
}
