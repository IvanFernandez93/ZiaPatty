package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Lingua;
import com.mycompany.myapp.repository.LinguaRepository;
import com.mycompany.myapp.service.dto.LinguaDTO;
import com.mycompany.myapp.service.mapper.LinguaMapper;
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
 * Integration tests for the {@link LinguaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LinguaResourceIT {

    private static final String DEFAULT_CODICE_LINGUA = "AAAAAAAAAA";
    private static final String UPDATED_CODICE_LINGUA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_LINGUA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_LINGUA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_CREAZIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CREAZIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ULTIMA_MODIFICA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ULTIMA_MODIFICA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ELIMINATO = false;
    private static final Boolean UPDATED_ELIMINATO = true;

    private static final String ENTITY_API_URL = "/api/linguas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{idLingua}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LinguaRepository linguaRepository;

    @Autowired
    private LinguaMapper linguaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLinguaMockMvc;

    private Lingua lingua;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lingua createEntity(EntityManager em) {
        Lingua lingua = new Lingua()
            .codiceLingua(DEFAULT_CODICE_LINGUA)
            .nomeLingua(DEFAULT_NOME_LINGUA)
            .dataCreazione(DEFAULT_DATA_CREAZIONE)
            .dataUltimaModifica(DEFAULT_DATA_ULTIMA_MODIFICA)
            .eliminato(DEFAULT_ELIMINATO);
        return lingua;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lingua createUpdatedEntity(EntityManager em) {
        Lingua lingua = new Lingua()
            .codiceLingua(UPDATED_CODICE_LINGUA)
            .nomeLingua(UPDATED_NOME_LINGUA)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        return lingua;
    }

    @BeforeEach
    public void initTest() {
        lingua = createEntity(em);
    }

    @Test
    @Transactional
    void createLingua() throws Exception {
        int databaseSizeBeforeCreate = linguaRepository.findAll().size();
        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);
        restLinguaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(linguaDTO)))
            .andExpect(status().isCreated());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeCreate + 1);
        Lingua testLingua = linguaList.get(linguaList.size() - 1);
        assertThat(testLingua.getCodiceLingua()).isEqualTo(DEFAULT_CODICE_LINGUA);
        assertThat(testLingua.getNomeLingua()).isEqualTo(DEFAULT_NOME_LINGUA);
        assertThat(testLingua.getDataCreazione()).isEqualTo(DEFAULT_DATA_CREAZIONE);
        assertThat(testLingua.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testLingua.getEliminato()).isEqualTo(DEFAULT_ELIMINATO);
    }

    @Test
    @Transactional
    void createLinguaWithExistingId() throws Exception {
        // Create the Lingua with an existing ID
        lingua.setIdLingua(1L);
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        int databaseSizeBeforeCreate = linguaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinguaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(linguaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLinguas() throws Exception {
        // Initialize the database
        linguaRepository.saveAndFlush(lingua);

        // Get all the linguaList
        restLinguaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=idLingua,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idLingua").value(hasItem(lingua.getIdLingua().intValue())))
            .andExpect(jsonPath("$.[*].codiceLingua").value(hasItem(DEFAULT_CODICE_LINGUA)))
            .andExpect(jsonPath("$.[*].nomeLingua").value(hasItem(DEFAULT_NOME_LINGUA)))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(sameInstant(DEFAULT_DATA_CREAZIONE))))
            .andExpect(jsonPath("$.[*].dataUltimaModifica").value(hasItem(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA))))
            .andExpect(jsonPath("$.[*].eliminato").value(hasItem(DEFAULT_ELIMINATO.booleanValue())));
    }

    @Test
    @Transactional
    void getLingua() throws Exception {
        // Initialize the database
        linguaRepository.saveAndFlush(lingua);

        // Get the lingua
        restLinguaMockMvc
            .perform(get(ENTITY_API_URL_ID, lingua.getIdLingua()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.idLingua").value(lingua.getIdLingua().intValue()))
            .andExpect(jsonPath("$.codiceLingua").value(DEFAULT_CODICE_LINGUA))
            .andExpect(jsonPath("$.nomeLingua").value(DEFAULT_NOME_LINGUA))
            .andExpect(jsonPath("$.dataCreazione").value(sameInstant(DEFAULT_DATA_CREAZIONE)))
            .andExpect(jsonPath("$.dataUltimaModifica").value(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA)))
            .andExpect(jsonPath("$.eliminato").value(DEFAULT_ELIMINATO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLingua() throws Exception {
        // Get the lingua
        restLinguaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLingua() throws Exception {
        // Initialize the database
        linguaRepository.saveAndFlush(lingua);

        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();

        // Update the lingua
        Lingua updatedLingua = linguaRepository.findById(lingua.getIdLingua()).get();
        // Disconnect from session so that the updates on updatedLingua are not directly saved in db
        em.detach(updatedLingua);
        updatedLingua
            .codiceLingua(UPDATED_CODICE_LINGUA)
            .nomeLingua(UPDATED_NOME_LINGUA)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        LinguaDTO linguaDTO = linguaMapper.toDto(updatedLingua);

        restLinguaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linguaDTO.getIdLingua())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(linguaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
        Lingua testLingua = linguaList.get(linguaList.size() - 1);
        assertThat(testLingua.getCodiceLingua()).isEqualTo(UPDATED_CODICE_LINGUA);
        assertThat(testLingua.getNomeLingua()).isEqualTo(UPDATED_NOME_LINGUA);
        assertThat(testLingua.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testLingua.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testLingua.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void putNonExistingLingua() throws Exception {
        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();
        lingua.setIdLingua(count.incrementAndGet());

        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinguaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linguaDTO.getIdLingua())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(linguaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLingua() throws Exception {
        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();
        lingua.setIdLingua(count.incrementAndGet());

        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinguaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(linguaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLingua() throws Exception {
        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();
        lingua.setIdLingua(count.incrementAndGet());

        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinguaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(linguaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLinguaWithPatch() throws Exception {
        // Initialize the database
        linguaRepository.saveAndFlush(lingua);

        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();

        // Update the lingua using partial update
        Lingua partialUpdatedLingua = new Lingua();
        partialUpdatedLingua.setIdLingua(lingua.getIdLingua());

        partialUpdatedLingua.codiceLingua(UPDATED_CODICE_LINGUA).dataCreazione(UPDATED_DATA_CREAZIONE).eliminato(UPDATED_ELIMINATO);

        restLinguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLingua.getIdLingua())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLingua))
            )
            .andExpect(status().isOk());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
        Lingua testLingua = linguaList.get(linguaList.size() - 1);
        assertThat(testLingua.getCodiceLingua()).isEqualTo(UPDATED_CODICE_LINGUA);
        assertThat(testLingua.getNomeLingua()).isEqualTo(DEFAULT_NOME_LINGUA);
        assertThat(testLingua.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testLingua.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testLingua.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void fullUpdateLinguaWithPatch() throws Exception {
        // Initialize the database
        linguaRepository.saveAndFlush(lingua);

        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();

        // Update the lingua using partial update
        Lingua partialUpdatedLingua = new Lingua();
        partialUpdatedLingua.setIdLingua(lingua.getIdLingua());

        partialUpdatedLingua
            .codiceLingua(UPDATED_CODICE_LINGUA)
            .nomeLingua(UPDATED_NOME_LINGUA)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);

        restLinguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLingua.getIdLingua())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLingua))
            )
            .andExpect(status().isOk());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
        Lingua testLingua = linguaList.get(linguaList.size() - 1);
        assertThat(testLingua.getCodiceLingua()).isEqualTo(UPDATED_CODICE_LINGUA);
        assertThat(testLingua.getNomeLingua()).isEqualTo(UPDATED_NOME_LINGUA);
        assertThat(testLingua.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testLingua.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testLingua.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void patchNonExistingLingua() throws Exception {
        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();
        lingua.setIdLingua(count.incrementAndGet());

        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, linguaDTO.getIdLingua())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(linguaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLingua() throws Exception {
        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();
        lingua.setIdLingua(count.incrementAndGet());

        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(linguaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLingua() throws Exception {
        int databaseSizeBeforeUpdate = linguaRepository.findAll().size();
        lingua.setIdLingua(count.incrementAndGet());

        // Create the Lingua
        LinguaDTO linguaDTO = linguaMapper.toDto(lingua);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinguaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(linguaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lingua in the database
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLingua() throws Exception {
        // Initialize the database
        linguaRepository.saveAndFlush(lingua);

        int databaseSizeBeforeDelete = linguaRepository.findAll().size();

        // Delete the lingua
        restLinguaMockMvc
            .perform(delete(ENTITY_API_URL_ID, lingua.getIdLingua()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lingua> linguaList = linguaRepository.findAll();
        assertThat(linguaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
