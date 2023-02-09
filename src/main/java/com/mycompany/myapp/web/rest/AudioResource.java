package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AudioRepository;
import com.mycompany.myapp.service.AudioService;
import com.mycompany.myapp.service.dto.AudioDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Audio}.
 */
@RestController
@RequestMapping("/api")
public class AudioResource {

    private final Logger log = LoggerFactory.getLogger(AudioResource.class);

    private static final String ENTITY_NAME = "audio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AudioService audioService;

    private final AudioRepository audioRepository;

    public AudioResource(AudioService audioService, AudioRepository audioRepository) {
        this.audioService = audioService;
        this.audioRepository = audioRepository;
    }

    /**
     * {@code POST  /audio} : Create a new audio.
     *
     * @param audioDTO the audioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new audioDTO, or with status {@code 400 (Bad Request)} if the audio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audio")
    public ResponseEntity<AudioDTO> createAudio(@RequestBody AudioDTO audioDTO) throws URISyntaxException {
        log.debug("REST request to save Audio : {}", audioDTO);
        if (audioDTO.getIdAudio() != null) {
            throw new BadRequestAlertException("A new audio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AudioDTO result = audioService.save(audioDTO);
        return ResponseEntity
            .created(new URI("/api/audio/" + result.getIdAudio()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getIdAudio().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audio/:idAudio} : Updates an existing audio.
     *
     * @param idAudio the id of the audioDTO to save.
     * @param audioDTO the audioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated audioDTO,
     * or with status {@code 400 (Bad Request)} if the audioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the audioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audio/{idAudio}")
    public ResponseEntity<AudioDTO> updateAudio(
        @PathVariable(value = "idAudio", required = false) final Long idAudio,
        @RequestBody AudioDTO audioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Audio : {}, {}", idAudio, audioDTO);
        if (audioDTO.getIdAudio() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idAudio, audioDTO.getIdAudio())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!audioRepository.existsById(idAudio)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AudioDTO result = audioService.update(audioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, audioDTO.getIdAudio().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /audio/:idAudio} : Partial updates given fields of an existing audio, field will ignore if it is null
     *
     * @param idAudio the id of the audioDTO to save.
     * @param audioDTO the audioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated audioDTO,
     * or with status {@code 400 (Bad Request)} if the audioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the audioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the audioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/audio/{idAudio}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AudioDTO> partialUpdateAudio(
        @PathVariable(value = "idAudio", required = false) final Long idAudio,
        @RequestBody AudioDTO audioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Audio partially : {}, {}", idAudio, audioDTO);
        if (audioDTO.getIdAudio() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idAudio, audioDTO.getIdAudio())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!audioRepository.existsById(idAudio)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AudioDTO> result = audioService.partialUpdate(audioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, audioDTO.getIdAudio().toString())
        );
    }

    /**
     * {@code GET  /audio} : get all the audio.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of audio in body.
     */
    @GetMapping("/audio")
    public List<AudioDTO> getAllAudio() {
        log.debug("REST request to get all Audio");
        return audioService.findAll();
    }

    /**
     * {@code GET  /audio/:id} : get the "id" audio.
     *
     * @param id the id of the audioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the audioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audio/{id}")
    public ResponseEntity<AudioDTO> getAudio(@PathVariable Long id) {
        log.debug("REST request to get Audio : {}", id);
        Optional<AudioDTO> audioDTO = audioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(audioDTO);
    }

    /**
     * {@code DELETE  /audio/:id} : delete the "id" audio.
     *
     * @param id the id of the audioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audio/{id}")
    public ResponseEntity<Void> deleteAudio(@PathVariable Long id) {
        log.debug("REST request to delete Audio : {}", id);
        audioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
