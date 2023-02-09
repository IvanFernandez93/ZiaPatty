package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FraseAudioRepository;
import com.mycompany.myapp.service.FraseAudioService;
import com.mycompany.myapp.service.dto.FraseAudioDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FraseAudio}.
 */
@RestController
@RequestMapping("/api")
public class FraseAudioResource {

    private final Logger log = LoggerFactory.getLogger(FraseAudioResource.class);

    private static final String ENTITY_NAME = "fraseAudio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FraseAudioService fraseAudioService;

    private final FraseAudioRepository fraseAudioRepository;

    public FraseAudioResource(FraseAudioService fraseAudioService, FraseAudioRepository fraseAudioRepository) {
        this.fraseAudioService = fraseAudioService;
        this.fraseAudioRepository = fraseAudioRepository;
    }

    /**
     * {@code POST  /frase-audios} : Create a new fraseAudio.
     *
     * @param fraseAudioDTO the fraseAudioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fraseAudioDTO, or with status {@code 400 (Bad Request)} if the fraseAudio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frase-audios")
    public ResponseEntity<FraseAudioDTO> createFraseAudio(@RequestBody FraseAudioDTO fraseAudioDTO) throws URISyntaxException {
        log.debug("REST request to save FraseAudio : {}", fraseAudioDTO);
        if (fraseAudioDTO.getId() != null) {
            throw new BadRequestAlertException("A new fraseAudio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FraseAudioDTO result = fraseAudioService.save(fraseAudioDTO);
        return ResponseEntity
            .created(new URI("/api/frase-audios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frase-audios/:id} : Updates an existing fraseAudio.
     *
     * @param id the id of the fraseAudioDTO to save.
     * @param fraseAudioDTO the fraseAudioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraseAudioDTO,
     * or with status {@code 400 (Bad Request)} if the fraseAudioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fraseAudioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frase-audios/{id}")
    public ResponseEntity<FraseAudioDTO> updateFraseAudio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FraseAudioDTO fraseAudioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FraseAudio : {}, {}", id, fraseAudioDTO);
        if (fraseAudioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraseAudioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraseAudioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FraseAudioDTO result = fraseAudioService.update(fraseAudioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraseAudioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frase-audios/:id} : Partial updates given fields of an existing fraseAudio, field will ignore if it is null
     *
     * @param id the id of the fraseAudioDTO to save.
     * @param fraseAudioDTO the fraseAudioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraseAudioDTO,
     * or with status {@code 400 (Bad Request)} if the fraseAudioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fraseAudioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fraseAudioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frase-audios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FraseAudioDTO> partialUpdateFraseAudio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FraseAudioDTO fraseAudioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FraseAudio partially : {}, {}", id, fraseAudioDTO);
        if (fraseAudioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraseAudioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraseAudioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FraseAudioDTO> result = fraseAudioService.partialUpdate(fraseAudioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraseAudioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frase-audios} : get all the fraseAudios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fraseAudios in body.
     */
    @GetMapping("/frase-audios")
    public List<FraseAudioDTO> getAllFraseAudios() {
        log.debug("REST request to get all FraseAudios");
        return fraseAudioService.findAll();
    }

    /**
     * {@code GET  /frase-audios/:id} : get the "id" fraseAudio.
     *
     * @param id the id of the fraseAudioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fraseAudioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frase-audios/{id}")
    public ResponseEntity<FraseAudioDTO> getFraseAudio(@PathVariable Long id) {
        log.debug("REST request to get FraseAudio : {}", id);
        Optional<FraseAudioDTO> fraseAudioDTO = fraseAudioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fraseAudioDTO);
    }

    /**
     * {@code DELETE  /frase-audios/:id} : delete the "id" fraseAudio.
     *
     * @param id the id of the fraseAudioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frase-audios/{id}")
    public ResponseEntity<Void> deleteFraseAudio(@PathVariable Long id) {
        log.debug("REST request to delete FraseAudio : {}", id);
        fraseAudioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
