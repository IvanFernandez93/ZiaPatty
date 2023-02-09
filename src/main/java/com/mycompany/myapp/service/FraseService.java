package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Frase;
import com.mycompany.myapp.repository.FraseRepository;
import com.mycompany.myapp.service.dto.FraseDTO;
import com.mycompany.myapp.service.mapper.FraseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Frase}.
 */
@Service
@Transactional
public class FraseService {

    private final Logger log = LoggerFactory.getLogger(FraseService.class);

    private final FraseRepository fraseRepository;

    private final FraseMapper fraseMapper;

    public FraseService(FraseRepository fraseRepository, FraseMapper fraseMapper) {
        this.fraseRepository = fraseRepository;
        this.fraseMapper = fraseMapper;
    }

    /**
     * Save a frase.
     *
     * @param fraseDTO the entity to save.
     * @return the persisted entity.
     */
    public FraseDTO save(FraseDTO fraseDTO) {
        log.debug("Request to save Frase : {}", fraseDTO);
        Frase frase = fraseMapper.toEntity(fraseDTO);
        frase = fraseRepository.save(frase);
        return fraseMapper.toDto(frase);
    }

    /**
     * Update a frase.
     *
     * @param fraseDTO the entity to save.
     * @return the persisted entity.
     */
    public FraseDTO update(FraseDTO fraseDTO) {
        log.debug("Request to update Frase : {}", fraseDTO);
        Frase frase = fraseMapper.toEntity(fraseDTO);
        frase = fraseRepository.save(frase);
        return fraseMapper.toDto(frase);
    }

    /**
     * Partially update a frase.
     *
     * @param fraseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FraseDTO> partialUpdate(FraseDTO fraseDTO) {
        log.debug("Request to partially update Frase : {}", fraseDTO);

        return fraseRepository
            .findById(fraseDTO.getId())
            .map(existingFrase -> {
                fraseMapper.partialUpdate(existingFrase, fraseDTO);

                return existingFrase;
            })
            .map(fraseRepository::save)
            .map(fraseMapper::toDto);
    }

    /**
     * Get all the frases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FraseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frases");
        return fraseRepository.findAll(pageable).map(fraseMapper::toDto);
    }

    /**
     * Get one frase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FraseDTO> findOne(Long id) {
        log.debug("Request to get Frase : {}", id);
        return fraseRepository.findById(id).map(fraseMapper::toDto);
    }

    /**
     * Delete the frase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Frase : {}", id);
        fraseRepository.deleteById(id);
    }
}
