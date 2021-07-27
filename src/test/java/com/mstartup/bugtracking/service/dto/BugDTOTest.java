package com.mstartup.bugtracking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mstartup.bugtracking.web.rest.TestUtil;

public class BugDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BugDTO.class);
        BugDTO bugDTO1 = new BugDTO();
        bugDTO1.setId(1L);
        BugDTO bugDTO2 = new BugDTO();
        assertThat(bugDTO1).isNotEqualTo(bugDTO2);
        bugDTO2.setId(bugDTO1.getId());
        assertThat(bugDTO1).isEqualTo(bugDTO2);
        bugDTO2.setId(2L);
        assertThat(bugDTO1).isNotEqualTo(bugDTO2);
        bugDTO1.setId(null);
        assertThat(bugDTO1).isNotEqualTo(bugDTO2);
    }
}
