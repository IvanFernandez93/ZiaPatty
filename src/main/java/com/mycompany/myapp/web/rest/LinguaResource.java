package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LinguaRepository;
import com.mycompany.myapp.service.LinguaService;
import com.mycompany.myapp.service.dto.LinguaDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Lingua}.
 */
@RestController
@RequestMapping("/api")
public class LinguaResource {

    private final Logger log = LoggerFactory.getLogger(LinguaResource.class);

    private static final String ENTITY_NAME = "lingua";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LinguaService linguaService;

    private final LinguaRepository linguaRepository;

    public LinguaResource(LinguaService linguaService, LinguaRepository linguaRepository) {
        this.linguaService = linguaService;
        this.linguaRepository = linguaRepository;
    }

    /**
     * {@code POST  /linguas} : Create a new lingua.
     *
     * @param linguaDTO the linguaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new linguaDTO, or with status {@code 400 (Bad Request)} if the lingua has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/linguas")
    public ResponseEntity<LinguaDTO> createLingua(@RequestBody LinguaDTO linguaDTO) throws URISyntaxException {
        log.debug("REST request to save Lingua : {}", linguaDTO);
        if (linguaDTO.getIdLingua() != null) {
            throw new BadRequestAlertException("A new lingua cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LinguaDTO result = linguaService.save(linguaDTO);
        return ResponseEntity
            .created(new URI("/api/linguas/" + result.getIdLingua()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getIdLingua().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /linguas/:idLingua} : Updates an existing lingua.
     *
     * @param idLingua the id of the linguaDTO to save.
     * @param linguaDTO the linguaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linguaDTO,
     * or with status {@code 400 (Bad Request)} if the linguaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the linguaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/linguas/{idLingua}")
    public ResponseEntity<LinguaDTO> updateLingua(
        @PathVariable(value = "idLingua", required = false) final Long idLingua,
        @RequestBody LinguaDTO linguaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Lingua : {}, {}", idLingua, linguaDTO);
        if (linguaDTO.getIdLingua() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idLingua, linguaDTO.getIdLingua())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!linguaRepository.existsById(idLingua)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LinguaDTO result = linguaService.update(linguaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, linguaDTO.getIdLingua().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /linguas/:idLingua} : Partial updates given fields of an existing lingua, field will ignore if it is null
     *
     * @param idLingua the id of the linguaDTO to save.
     * @param linguaDTO the linguaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linguaDTO,
     * or with status {@code 400 (Bad Request)} if the linguaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the linguaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the linguaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/linguas/{idLingua}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LinguaDTO> partialUpdateLingua(
        @PathVariable(value = "idLingua", required = false) final Long idLingua,
        @RequestBody LinguaDTO linguaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lingua partially : {}, {}", idLingua, linguaDTO);
        if (linguaDTO.getIdLingua() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idLingua, linguaDTO.getIdLingua())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!linguaRepository.existsById(idLingua)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LinguaDTO> result = linguaService.partialUpdate(linguaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, linguaDTO.getIdLingua().toString())
        );
    }

    /**
     * {@code GET  /linguas} : get all the linguas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of linguas in body.
     */
    @GetMapping("/linguas")
    public List<LinguaDTO> getAllLinguas() {
        log.debug("REST request to get all Linguas");
        return linguaService.findAll();
    }

    /**
     * {@code GET  /linguas/:id} : get the "id" lingua.
     *
     * @param id the id of the linguaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the linguaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/linguas/{id}")
    public ResponseEntity<LinguaDTO> getLingua(@PathVariable Long id) {
        log.debug("REST request to get Lingua : {}", id);
        Optional<LinguaDTO> linguaDTO = linguaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(linguaDTO);
    }

    /**
     * {@code DELETE  /linguas/:id} : delete the "id" lingua.
     *
     * @param id the id of the linguaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/linguas/{id}")
    public ResponseEntity<Void> deleteLingua(@PathVariable Long id) {
        log.debug("REST request to delete Lingua : {}", id);
        linguaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
