package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Lingua;
import com.mycompany.myapp.repository.LinguaRepository;
import com.mycompany.myapp.service.dto.LinguaDTO;
import com.mycompany.myapp.service.mapper.LinguaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lingua}.
 */
@Service
@Transactional
public class LinguaService {

    private final Logger log = LoggerFactory.getLogger(LinguaService.class);

    private final LinguaRepository linguaRepository;

    private final LinguaMapper linguaMapper;

    public LinguaService(LinguaRepository linguaRepository, LinguaMapper linguaMapper) {
        this.linguaRepository = linguaRepository;
        this.linguaMapper = linguaMapper;
    }

    /**
     * Save a lingua.
     *
     * @param linguaDTO the entity to save.
     * @return the persisted entity.
     */
    public LinguaDTO save(LinguaDTO linguaDTO) {
        log.debug("Request to save Lingua : {}", linguaDTO);
        Lingua lingua = linguaMapper.toEntity(linguaDTO);
        lingua = linguaRepository.save(lingua);
        return linguaMapper.toDto(lingua);
    }

    /**
     * Update a lingua.
     *
     * @param linguaDTO the entity to save.
     * @return the persisted entity.
     */
    public LinguaDTO update(LinguaDTO linguaDTO) {
        log.debug("Request to update Lingua : {}", linguaDTO);
        Lingua lingua = linguaMapper.toEntity(linguaDTO);
        lingua = linguaRepository.save(lingua);
        return linguaMapper.toDto(lingua);
    }

    /**
     * Partially update a lingua.
     *
     * @param linguaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LinguaDTO> partialUpdate(LinguaDTO linguaDTO) {
        log.debug("Request to partially update Lingua : {}", linguaDTO);

        return linguaRepository
            .findById(linguaDTO.getIdLingua())
            .map(existingLingua -> {
                linguaMapper.partialUpdate(existingLingua, linguaDTO);

                return existingLingua;
            })
            .map(linguaRepository::save)
            .map(linguaMapper::toDto);
    }

    /**
     * Get all the linguas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LinguaDTO> findAll() {
        log.debug("Request to get all Linguas");
        return linguaRepository.findAll().stream().map(linguaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one lingua by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LinguaDTO> findOne(Long id) {
        log.debug("Request to get Lingua : {}", id);
        return linguaRepository.findById(id).map(linguaMapper::toDto);
    }

    /**
     * Delete the lingua by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lingua : {}", id);
        linguaRepository.deleteById(id);
    }
}
