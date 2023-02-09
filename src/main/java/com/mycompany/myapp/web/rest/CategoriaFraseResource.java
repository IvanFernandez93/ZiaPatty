package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CategoriaFraseRepository;
import com.mycompany.myapp.service.CategoriaFraseService;
import com.mycompany.myapp.service.dto.CategoriaFraseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CategoriaFrase}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaFraseResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaFraseResource.class);

    private static final String ENTITY_NAME = "categoriaFrase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaFraseService categoriaFraseService;

    private final CategoriaFraseRepository categoriaFraseRepository;

    public CategoriaFraseResource(CategoriaFraseService categoriaFraseService, CategoriaFraseRepository categoriaFraseRepository) {
        this.categoriaFraseService = categoriaFraseService;
        this.categoriaFraseRepository = categoriaFraseRepository;
    }

    /**
     * {@code POST  /categoria-frases} : Create a new categoriaFrase.
     *
     * @param categoriaFraseDTO the categoriaFraseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoriaFraseDTO, or with status {@code 400 (Bad Request)} if the categoriaFrase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categoria-frases")
    public ResponseEntity<CategoriaFraseDTO> createCategoriaFrase(@RequestBody CategoriaFraseDTO categoriaFraseDTO)
        throws URISyntaxException {
        log.debug("REST request to save CategoriaFrase : {}", categoriaFraseDTO);
        if (categoriaFraseDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoriaFrase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoriaFraseDTO result = categoriaFraseService.save(categoriaFraseDTO);
        return ResponseEntity
            .created(new URI("/api/categoria-frases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categoria-frases/:id} : Updates an existing categoriaFrase.
     *
     * @param id the id of the categoriaFraseDTO to save.
     * @param categoriaFraseDTO the categoriaFraseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaFraseDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaFraseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoriaFraseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categoria-frases/{id}")
    public ResponseEntity<CategoriaFraseDTO> updateCategoriaFrase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaFraseDTO categoriaFraseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoriaFrase : {}, {}", id, categoriaFraseDTO);
        if (categoriaFraseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaFraseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaFraseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoriaFraseDTO result = categoriaFraseService.update(categoriaFraseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaFraseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categoria-frases/:id} : Partial updates given fields of an existing categoriaFrase, field will ignore if it is null
     *
     * @param id the id of the categoriaFraseDTO to save.
     * @param categoriaFraseDTO the categoriaFraseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaFraseDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaFraseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoriaFraseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoriaFraseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categoria-frases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoriaFraseDTO> partialUpdateCategoriaFrase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaFraseDTO categoriaFraseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoriaFrase partially : {}, {}", id, categoriaFraseDTO);
        if (categoriaFraseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaFraseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaFraseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoriaFraseDTO> result = categoriaFraseService.partialUpdate(categoriaFraseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaFraseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categoria-frases} : get all the categoriaFrases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoriaFrases in body.
     */
    @GetMapping("/categoria-frases")
    public List<CategoriaFraseDTO> getAllCategoriaFrases() {
        log.debug("REST request to get all CategoriaFrases");
        return categoriaFraseService.findAll();
    }

    /**
     * {@code GET  /categoria-frases/:id} : get the "id" categoriaFrase.
     *
     * @param id the id of the categoriaFraseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoriaFraseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categoria-frases/{id}")
    public ResponseEntity<CategoriaFraseDTO> getCategoriaFrase(@PathVariable Long id) {
        log.debug("REST request to get CategoriaFrase : {}", id);
        Optional<CategoriaFraseDTO> categoriaFraseDTO = categoriaFraseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaFraseDTO);
    }

    /**
     * {@code DELETE  /categoria-frases/:id} : delete the "id" categoriaFrase.
     *
     * @param id the id of the categoriaFraseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categoria-frases/{id}")
    public ResponseEntity<Void> deleteCategoriaFrase(@PathVariable Long id) {
        log.debug("REST request to delete CategoriaFrase : {}", id);
        categoriaFraseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
