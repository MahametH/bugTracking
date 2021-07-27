package com.mstartup.bugtracking.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BugMapperTest {

    private BugMapper bugMapper;

    @BeforeEach
    public void setUp() {
        bugMapper = new BugMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bugMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bugMapper.fromId(null)).isNull();
    }
}
