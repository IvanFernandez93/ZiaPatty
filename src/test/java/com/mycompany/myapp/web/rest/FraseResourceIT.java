package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Frase;
import com.mycompany.myapp.domain.enumeration.Stato;
import com.mycompany.myapp.repository.FraseRepository;
import com.mycompany.myapp.service.dto.FraseDTO;
import com.mycompany.myapp.service.mapper.FraseMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FraseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FraseResourceIT {

    private static final Long DEFAULT_ID_FRASE = 1L;
    private static final Long UPDATED_ID_FRASE = 2L;

    private static final String DEFAULT_FRASE = "AAAAAAAAAA";
    private static final String UPDATED_FRASE = "BBBBBBBBBB";

    private static final Stato DEFAULT_CODICE_STATO = Stato.BOZZA;
    private static final Stato UPDATED_CODICE_STATO = Stato.PRIVATO;

    private static final ZonedDateTime DEFAULT_DATA_CREAZIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CREAZIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ULTIMA_MODIFICA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ULTIMA_MODIFICA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ELIMINATO = false;
    private static final Boolean UPDATED_ELIMINATO = true;

    private static final String ENTITY_API_URL = "/api/frases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraseRepository fraseRepository;

    @Autowired
    private FraseMapper fraseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraseMockMvc;

    private Frase frase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frase createEntity(EntityManager em) {
        Frase frase = new Frase()
            .idFrase(DEFAULT_ID_FRASE)
            .frase(DEFAULT_FRASE)
            .codiceStato(DEFAULT_CODICE_STATO)
            .dataCreazione(DEFAULT_DATA_CREAZIONE)
            .dataUltimaModifica(DEFAULT_DATA_ULTIMA_MODIFICA)
            .eliminato(DEFAULT_ELIMINATO);
        return frase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frase createUpdatedEntity(EntityManager em) {
        Frase frase = new Frase()
            .idFrase(UPDATED_ID_FRASE)
            .frase(UPDATED_FRASE)
            .codiceStato(UPDATED_CODICE_STATO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        return frase;
    }

    @BeforeEach
    public void initTest() {
        frase = createEntity(em);
    }

    @Test
    @Transactional
    void createFrase() throws Exception {
        int databaseSizeBeforeCreate = fraseRepository.findAll().size();
        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);
        restFraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraseDTO)))
            .andExpect(status().isCreated());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeCreate + 1);
        Frase testFrase = fraseList.get(fraseList.size() - 1);
        assertThat(testFrase.getIdFrase()).isEqualTo(DEFAULT_ID_FRASE);
        assertThat(testFrase.getFrase()).isEqualTo(DEFAULT_FRASE);
        assertThat(testFrase.getCodiceStato()).isEqualTo(DEFAULT_CODICE_STATO);
        assertThat(testFrase.getDataCreazione()).isEqualTo(DEFAULT_DATA_CREAZIONE);
        assertThat(testFrase.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testFrase.getEliminato()).isEqualTo(DEFAULT_ELIMINATO);
    }

    @Test
    @Transactional
    void createFraseWithExistingId() throws Exception {
        // Create the Frase with an existing ID
        frase.setId(1L);
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        int databaseSizeBeforeCreate = fraseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFrases() throws Exception {
        // Initialize the database
        fraseRepository.saveAndFlush(frase);

        // Get all the fraseList
        restFraseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frase.getId().intValue())))
            .andExpect(jsonPath("$.[*].idFrase").value(hasItem(DEFAULT_ID_FRASE.intValue())))
            .andExpect(jsonPath("$.[*].frase").value(hasItem(DEFAULT_FRASE)))
            .andExpect(jsonPath("$.[*].codiceStato").value(hasItem(DEFAULT_CODICE_STATO.toString())))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(sameInstant(DEFAULT_DATA_CREAZIONE))))
            .andExpect(jsonPath("$.[*].dataUltimaModifica").value(hasItem(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA))))
            .andExpect(jsonPath("$.[*].eliminato").value(hasItem(DEFAULT_ELIMINATO.booleanValue())));
    }

    @Test
    @Transactional
    void getFrase() throws Exception {
        // Initialize the database
        fraseRepository.saveAndFlush(frase);

        // Get the frase
        restFraseMockMvc
            .perform(get(ENTITY_API_URL_ID, frase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frase.getId().intValue()))
            .andExpect(jsonPath("$.idFrase").value(DEFAULT_ID_FRASE.intValue()))
            .andExpect(jsonPath("$.frase").value(DEFAULT_FRASE))
            .andExpect(jsonPath("$.codiceStato").value(DEFAULT_CODICE_STATO.toString()))
            .andExpect(jsonPath("$.dataCreazione").value(sameInstant(DEFAULT_DATA_CREAZIONE)))
            .andExpect(jsonPath("$.dataUltimaModifica").value(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA)))
            .andExpect(jsonPath("$.eliminato").value(DEFAULT_ELIMINATO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFrase() throws Exception {
        // Get the frase
        restFraseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFrase() throws Exception {
        // Initialize the database
        fraseRepository.saveAndFlush(frase);

        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();

        // Update the frase
        Frase updatedFrase = fraseRepository.findById(frase.getId()).get();
        // Disconnect from session so that the updates on updatedFrase are not directly saved in db
        em.detach(updatedFrase);
        updatedFrase
            .idFrase(UPDATED_ID_FRASE)
            .frase(UPDATED_FRASE)
            .codiceStato(UPDATED_CODICE_STATO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        FraseDTO fraseDTO = fraseMapper.toDto(updatedFrase);

        restFraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
        Frase testFrase = fraseList.get(fraseList.size() - 1);
        assertThat(testFrase.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
        assertThat(testFrase.getFrase()).isEqualTo(UPDATED_FRASE);
        assertThat(testFrase.getCodiceStato()).isEqualTo(UPDATED_CODICE_STATO);
        assertThat(testFrase.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testFrase.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testFrase.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void putNonExistingFrase() throws Exception {
        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();
        frase.setId(count.incrementAndGet());

        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrase() throws Exception {
        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();
        frase.setId(count.incrementAndGet());

        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrase() throws Exception {
        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();
        frase.setId(count.incrementAndGet());

        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFraseWithPatch() throws Exception {
        // Initialize the database
        fraseRepository.saveAndFlush(frase);

        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();

        // Update the frase using partial update
        Frase partialUpdatedFrase = new Frase();
        partialUpdatedFrase.setId(frase.getId());

        partialUpdatedFrase.idFrase(UPDATED_ID_FRASE).dataCreazione(UPDATED_DATA_CREAZIONE).eliminato(UPDATED_ELIMINATO);

        restFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrase))
            )
            .andExpect(status().isOk());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
        Frase testFrase = fraseList.get(fraseList.size() - 1);
        assertThat(testFrase.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
        assertThat(testFrase.getFrase()).isEqualTo(DEFAULT_FRASE);
        assertThat(testFrase.getCodiceStato()).isEqualTo(DEFAULT_CODICE_STATO);
        assertThat(testFrase.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testFrase.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testFrase.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void fullUpdateFraseWithPatch() throws Exception {
        // Initialize the database
        fraseRepository.saveAndFlush(frase);

        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();

        // Update the frase using partial update
        Frase partialUpdatedFrase = new Frase();
        partialUpdatedFrase.setId(frase.getId());

        partialUpdatedFrase
            .idFrase(UPDATED_ID_FRASE)
            .frase(UPDATED_FRASE)
            .codiceStato(UPDATED_CODICE_STATO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);

        restFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrase))
            )
            .andExpect(status().isOk());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
        Frase testFrase = fraseList.get(fraseList.size() - 1);
        assertThat(testFrase.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
        assertThat(testFrase.getFrase()).isEqualTo(UPDATED_FRASE);
        assertThat(testFrase.getCodiceStato()).isEqualTo(UPDATED_CODICE_STATO);
        assertThat(testFrase.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testFrase.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testFrase.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void patchNonExistingFrase() throws Exception {
        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();
        frase.setId(count.incrementAndGet());

        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrase() throws Exception {
        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();
        frase.setId(count.incrementAndGet());

        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrase() throws Exception {
        int databaseSizeBeforeUpdate = fraseRepository.findAll().size();
        frase.setId(count.incrementAndGet());

        // Create the Frase
        FraseDTO fraseDTO = fraseMapper.toDto(frase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fraseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frase in the database
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrase() throws Exception {
        // Initialize the database
        fraseRepository.saveAndFlush(frase);

        int databaseSizeBeforeDelete = fraseRepository.findAll().size();

        // Delete the frase
        restFraseMockMvc
            .perform(delete(ENTITY_API_URL_ID, frase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Frase> fraseList = fraseRepository.findAll();
        assertThat(fraseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
