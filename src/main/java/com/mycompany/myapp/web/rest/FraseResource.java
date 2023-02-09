package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FraseRepository;
import com.mycompany.myapp.service.FraseService;
import com.mycompany.myapp.service.dto.FraseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Frase}.
 */
@RestController
@RequestMapping("/api")
public class FraseResource {

    private final Logger log = LoggerFactory.getLogger(FraseResource.class);

    private static final String ENTITY_NAME = "frase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FraseService fraseService;

    private final FraseRepository fraseRepository;

    public FraseResource(FraseService fraseService, FraseRepository fraseRepository) {
        this.fraseService = fraseService;
        this.fraseRepository = fraseRepository;
    }

    /**
     * {@code POST  /frases} : Create a new frase.
     *
     * @param fraseDTO the fraseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fraseDTO, or with status {@code 400 (Bad Request)} if the frase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frases")
    public ResponseEntity<FraseDTO> createFrase(@RequestBody FraseDTO fraseDTO) throws URISyntaxException {
        log.debug("REST request to save Frase : {}", fraseDTO);
        if (fraseDTO.getId() != null) {
            throw new BadRequestAlertException("A new frase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FraseDTO result = fraseService.save(fraseDTO);
        return ResponseEntity
            .created(new URI("/api/frases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frases/:id} : Updates an existing frase.
     *
     * @param id the id of the fraseDTO to save.
     * @param fraseDTO the fraseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraseDTO,
     * or with status {@code 400 (Bad Request)} if the fraseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fraseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frases/{id}")
    public ResponseEntity<FraseDTO> updateFrase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FraseDTO fraseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Frase : {}, {}", id, fraseDTO);
        if (fraseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FraseDTO result = fraseService.update(fraseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frases/:id} : Partial updates given fields of an existing frase, field will ignore if it is null
     *
     * @param id the id of the fraseDTO to save.
     * @param fraseDTO the fraseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraseDTO,
     * or with status {@code 400 (Bad Request)} if the fraseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fraseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fraseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FraseDTO> partialUpdateFrase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FraseDTO fraseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Frase partially : {}, {}", id, fraseDTO);
        if (fraseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FraseDTO> result = fraseService.partialUpdate(fraseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frases} : get all the frases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frases in body.
     */
    @GetMapping("/frases")
    public ResponseEntity<List<FraseDTO>> getAllFrases(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Frases");
        Page<FraseDTO> page = fraseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frases/:id} : get the "id" frase.
     *
     * @param id the id of the fraseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fraseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frases/{id}")
    public ResponseEntity<FraseDTO> getFrase(@PathVariable Long id) {
        log.debug("REST request to get Frase : {}", id);
        Optional<FraseDTO> fraseDTO = fraseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fraseDTO);
    }

    /**
     * {@code DELETE  /frases/:id} : delete the "id" frase.
     *
     * @param id the id of the fraseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frases/{id}")
    public ResponseEntity<Void> deleteFrase(@PathVariable Long id) {
        log.debug("REST request to delete Frase : {}", id);
        fraseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
