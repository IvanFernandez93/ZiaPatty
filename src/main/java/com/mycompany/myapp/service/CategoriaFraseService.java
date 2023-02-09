package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CategoriaFrase;
import com.mycompany.myapp.repository.CategoriaFraseRepository;
import com.mycompany.myapp.service.dto.CategoriaFraseDTO;
import com.mycompany.myapp.service.mapper.CategoriaFraseMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaFrase}.
 */
@Service
@Transactional
public class CategoriaFraseService {

    private final Logger log = LoggerFactory.getLogger(CategoriaFraseService.class);

    private final CategoriaFraseRepository categoriaFraseRepository;

    private final CategoriaFraseMapper categoriaFraseMapper;

    public CategoriaFraseService(CategoriaFraseRepository categoriaFraseRepository, CategoriaFraseMapper categoriaFraseMapper) {
        this.categoriaFraseRepository = categoriaFraseRepository;
        this.categoriaFraseMapper = categoriaFraseMapper;
    }

    /**
     * Save a categoriaFrase.
     *
     * @param categoriaFraseDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriaFraseDTO save(CategoriaFraseDTO categoriaFraseDTO) {
        log.debug("Request to save CategoriaFrase : {}", categoriaFraseDTO);
        CategoriaFrase categoriaFrase = categoriaFraseMapper.toEntity(categoriaFraseDTO);
        categoriaFrase = categoriaFraseRepository.save(categoriaFrase);
        return categoriaFraseMapper.toDto(categoriaFrase);
    }

    /**
     * Update a categoriaFrase.
     *
     * @param categoriaFraseDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriaFraseDTO update(CategoriaFraseDTO categoriaFraseDTO) {
        log.debug("Request to update CategoriaFrase : {}", categoriaFraseDTO);
        CategoriaFrase categoriaFrase = categoriaFraseMapper.toEntity(categoriaFraseDTO);
        categoriaFrase = categoriaFraseRepository.save(categoriaFrase);
        return categoriaFraseMapper.toDto(categoriaFrase);
    }

    /**
     * Partially update a categoriaFrase.
     *
     * @param categoriaFraseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoriaFraseDTO> partialUpdate(CategoriaFraseDTO categoriaFraseDTO) {
        log.debug("Request to partially update CategoriaFrase : {}", categoriaFraseDTO);

        return categoriaFraseRepository
            .findById(categoriaFraseDTO.getId())
            .map(existingCategoriaFrase -> {
                categoriaFraseMapper.partialUpdate(existingCategoriaFrase, categoriaFraseDTO);

                return existingCategoriaFrase;
            })
            .map(categoriaFraseRepository::save)
            .map(categoriaFraseMapper::toDto);
    }

    /**
     * Get all the categoriaFrases.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CategoriaFraseDTO> findAll() {
        log.debug("Request to get all CategoriaFrases");
        return categoriaFraseRepository
            .findAll()
            .stream()
            .map(categoriaFraseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one categoriaFrase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoriaFraseDTO> findOne(Long id) {
        log.debug("Request to get CategoriaFrase : {}", id);
        return categoriaFraseRepository.findById(id).map(categoriaFraseMapper::toDto);
    }

    /**
     * Delete the categoriaFrase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CategoriaFrase : {}", id);
        categoriaFraseRepository.deleteById(id);
    }
}
