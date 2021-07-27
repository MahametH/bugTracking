package com.mstartup.bugtracking.service.impl;

import com.mstartup.bugtracking.service.BugService;
import com.mstartup.bugtracking.domain.Bug;
import com.mstartup.bugtracking.repository.BugRepository;
import com.mstartup.bugtracking.service.dto.BugDTO;
import com.mstartup.bugtracking.service.mapper.BugMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Bug}.
 */
@Service
@Transactional
public class BugServiceImpl implements BugService {

    private final Logger log = LoggerFactory.getLogger(BugServiceImpl.class);

    private final BugRepository bugRepository;

    private final BugMapper bugMapper;

    public BugServiceImpl(BugRepository bugRepository, BugMapper bugMapper) {
        this.bugRepository = bugRepository;
        this.bugMapper = bugMapper;
    }

    @Override
    public BugDTO save(BugDTO bugDTO) {
        log.debug("Request to save Bug : {}", bugDTO);
        Bug bug = bugMapper.toEntity(bugDTO);
        bug = bugRepository.save(bug);
        return bugMapper.toDto(bug);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BugDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bugs");
        return bugRepository.findAll(pageable)
            .map(bugMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BugDTO> findOne(Long id) {
        log.debug("Request to get Bug : {}", id);
        return bugRepository.findById(id)
            .map(bugMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bug : {}", id);
        bugRepository.deleteById(id);
    }
}
