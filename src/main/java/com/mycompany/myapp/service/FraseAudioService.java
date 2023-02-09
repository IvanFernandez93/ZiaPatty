package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FraseAudio;
import com.mycompany.myapp.repository.FraseAudioRepository;
import com.mycompany.myapp.service.dto.FraseAudioDTO;
import com.mycompany.myapp.service.mapper.FraseAudioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FraseAudio}.
 */
@Service
@Transactional
public class FraseAudioService {

    private final Logger log = LoggerFactory.getLogger(FraseAudioService.class);

    private final FraseAudioRepository fraseAudioRepository;

    private final FraseAudioMapper fraseAudioMapper;

    public FraseAudioService(FraseAudioRepository fraseAudioRepository, FraseAudioMapper fraseAudioMapper) {
        this.fraseAudioRepository = fraseAudioRepository;
        this.fraseAudioMapper = fraseAudioMapper;
    }

    /**
     * Save a fraseAudio.
     *
     * @param fraseAudioDTO the entity to save.
     * @return the persisted entity.
     */
    public FraseAudioDTO save(FraseAudioDTO fraseAudioDTO) {
        log.debug("Request to save FraseAudio : {}", fraseAudioDTO);
        FraseAudio fraseAudio = fraseAudioMapper.toEntity(fraseAudioDTO);
        fraseAudio = fraseAudioRepository.save(fraseAudio);
        return fraseAudioMapper.toDto(fraseAudio);
    }

    /**
     * Update a fraseAudio.
     *
     * @param fraseAudioDTO the entity to save.
     * @return the persisted entity.
     */
    public FraseAudioDTO update(FraseAudioDTO fraseAudioDTO) {
        log.debug("Request to update FraseAudio : {}", fraseAudioDTO);
        FraseAudio fraseAudio = fraseAudioMapper.toEntity(fraseAudioDTO);
        fraseAudio = fraseAudioRepository.save(fraseAudio);
        return fraseAudioMapper.toDto(fraseAudio);
    }

    /**
     * Partially update a fraseAudio.
     *
     * @param fraseAudioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FraseAudioDTO> partialUpdate(FraseAudioDTO fraseAudioDTO) {
        log.debug("Request to partially update FraseAudio : {}", fraseAudioDTO);

        return fraseAudioRepository
            .findById(fraseAudioDTO.getId())
            .map(existingFraseAudio -> {
                fraseAudioMapper.partialUpdate(existingFraseAudio, fraseAudioDTO);

                return existingFraseAudio;
            })
            .map(fraseAudioRepository::save)
            .map(fraseAudioMapper::toDto);
    }

    /**
     * Get all the fraseAudios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FraseAudioDTO> findAll() {
        log.debug("Request to get all FraseAudios");
        return fraseAudioRepository.findAll().stream().map(fraseAudioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one fraseAudio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FraseAudioDTO> findOne(Long id) {
        log.debug("Request to get FraseAudio : {}", id);
        return fraseAudioRepository.findById(id).map(fraseAudioMapper::toDto);
    }

    /**
     * Delete the fraseAudio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FraseAudio : {}", id);
        fraseAudioRepository.deleteById(id);
    }
}
