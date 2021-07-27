package com.mstartup.bugtracking.service.impl;

import com.mstartup.bugtracking.service.NoteService;
import com.mstartup.bugtracking.domain.Note;
import com.mstartup.bugtracking.repository.NoteRepository;
import com.mstartup.bugtracking.service.dto.NoteDTO;
import com.mstartup.bugtracking.service.mapper.NoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    @Override
    public NoteDTO save(NoteDTO noteDTO) {
        log.debug("Request to save Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable)
            .map(noteMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<NoteDTO> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id)
            .map(noteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }
}
