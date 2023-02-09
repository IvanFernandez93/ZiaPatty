package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FraseAudio;
import com.mycompany.myapp.repository.FraseAudioRepository;
import com.mycompany.myapp.service.dto.FraseAudioDTO;
import com.mycompany.myapp.service.mapper.FraseAudioMapper;
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
 * Integration tests for the {@link FraseAudioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FraseAudioResourceIT {

    private static final Long DEFAULT_ID_CATEGORIA = 1L;
    private static final Long UPDATED_ID_CATEGORIA = 2L;

    private static final Long DEFAULT_ID_FRASE = 1L;
    private static final Long UPDATED_ID_FRASE = 2L;

    private static final String ENTITY_API_URL = "/api/frase-audios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraseAudioRepository fraseAudioRepository;

    @Autowired
    private FraseAudioMapper fraseAudioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraseAudioMockMvc;

    private FraseAudio fraseAudio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraseAudio createEntity(EntityManager em) {
        FraseAudio fraseAudio = new FraseAudio().idCategoria(DEFAULT_ID_CATEGORIA).idFrase(DEFAULT_ID_FRASE);
        return fraseAudio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraseAudio createUpdatedEntity(EntityManager em) {
        FraseAudio fraseAudio = new FraseAudio().idCategoria(UPDATED_ID_CATEGORIA).idFrase(UPDATED_ID_FRASE);
        return fraseAudio;
    }

    @BeforeEach
    public void initTest() {
        fraseAudio = createEntity(em);
    }

    @Test
    @Transactional
    void createFraseAudio() throws Exception {
        int databaseSizeBeforeCreate = fraseAudioRepository.findAll().size();
        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);
        restFraseAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO)))
            .andExpect(status().isCreated());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeCreate + 1);
        FraseAudio testFraseAudio = fraseAudioList.get(fraseAudioList.size() - 1);
        assertThat(testFraseAudio.getIdCategoria()).isEqualTo(DEFAULT_ID_CATEGORIA);
        assertThat(testFraseAudio.getIdFrase()).isEqualTo(DEFAULT_ID_FRASE);
    }

    @Test
    @Transactional
    void createFraseAudioWithExistingId() throws Exception {
        // Create the FraseAudio with an existing ID
        fraseAudio.setId(1L);
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        int databaseSizeBeforeCreate = fraseAudioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraseAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFraseAudios() throws Exception {
        // Initialize the database
        fraseAudioRepository.saveAndFlush(fraseAudio);

        // Get all the fraseAudioList
        restFraseAudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraseAudio.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCategoria").value(hasItem(DEFAULT_ID_CATEGORIA.intValue())))
            .andExpect(jsonPath("$.[*].idFrase").value(hasItem(DEFAULT_ID_FRASE.intValue())));
    }

    @Test
    @Transactional
    void getFraseAudio() throws Exception {
        // Initialize the database
        fraseAudioRepository.saveAndFlush(fraseAudio);

        // Get the fraseAudio
        restFraseAudioMockMvc
            .perform(get(ENTITY_API_URL_ID, fraseAudio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fraseAudio.getId().intValue()))
            .andExpect(jsonPath("$.idCategoria").value(DEFAULT_ID_CATEGORIA.intValue()))
            .andExpect(jsonPath("$.idFrase").value(DEFAULT_ID_FRASE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFraseAudio() throws Exception {
        // Get the fraseAudio
        restFraseAudioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFraseAudio() throws Exception {
        // Initialize the database
        fraseAudioRepository.saveAndFlush(fraseAudio);

        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();

        // Update the fraseAudio
        FraseAudio updatedFraseAudio = fraseAudioRepository.findById(fraseAudio.getId()).get();
        // Disconnect from session so that the updates on updatedFraseAudio are not directly saved in db
        em.detach(updatedFraseAudio);
        updatedFraseAudio.idCategoria(UPDATED_ID_CATEGORIA).idFrase(UPDATED_ID_FRASE);
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(updatedFraseAudio);

        restFraseAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraseAudioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO))
            )
            .andExpect(status().isOk());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
        FraseAudio testFraseAudio = fraseAudioList.get(fraseAudioList.size() - 1);
        assertThat(testFraseAudio.getIdCategoria()).isEqualTo(UPDATED_ID_CATEGORIA);
        assertThat(testFraseAudio.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
    }

    @Test
    @Transactional
    void putNonExistingFraseAudio() throws Exception {
        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();
        fraseAudio.setId(count.incrementAndGet());

        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraseAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraseAudioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFraseAudio() throws Exception {
        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();
        fraseAudio.setId(count.incrementAndGet());

        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFraseAudio() throws Exception {
        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();
        fraseAudio.setId(count.incrementAndGet());

        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseAudioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFraseAudioWithPatch() throws Exception {
        // Initialize the database
        fraseAudioRepository.saveAndFlush(fraseAudio);

        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();

        // Update the fraseAudio using partial update
        FraseAudio partialUpdatedFraseAudio = new FraseAudio();
        partialUpdatedFraseAudio.setId(fraseAudio.getId());

        restFraseAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraseAudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFraseAudio))
            )
            .andExpect(status().isOk());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
        FraseAudio testFraseAudio = fraseAudioList.get(fraseAudioList.size() - 1);
        assertThat(testFraseAudio.getIdCategoria()).isEqualTo(DEFAULT_ID_CATEGORIA);
        assertThat(testFraseAudio.getIdFrase()).isEqualTo(DEFAULT_ID_FRASE);
    }

    @Test
    @Transactional
    void fullUpdateFraseAudioWithPatch() throws Exception {
        // Initialize the database
        fraseAudioRepository.saveAndFlush(fraseAudio);

        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();

        // Update the fraseAudio using partial update
        FraseAudio partialUpdatedFraseAudio = new FraseAudio();
        partialUpdatedFraseAudio.setId(fraseAudio.getId());

        partialUpdatedFraseAudio.idCategoria(UPDATED_ID_CATEGORIA).idFrase(UPDATED_ID_FRASE);

        restFraseAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraseAudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFraseAudio))
            )
            .andExpect(status().isOk());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
        FraseAudio testFraseAudio = fraseAudioList.get(fraseAudioList.size() - 1);
        assertThat(testFraseAudio.getIdCategoria()).isEqualTo(UPDATED_ID_CATEGORIA);
        assertThat(testFraseAudio.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
    }

    @Test
    @Transactional
    void patchNonExistingFraseAudio() throws Exception {
        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();
        fraseAudio.setId(count.incrementAndGet());

        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraseAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraseAudioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFraseAudio() throws Exception {
        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();
        fraseAudio.setId(count.incrementAndGet());

        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFraseAudio() throws Exception {
        int databaseSizeBeforeUpdate = fraseAudioRepository.findAll().size();
        fraseAudio.setId(count.incrementAndGet());

        // Create the FraseAudio
        FraseAudioDTO fraseAudioDTO = fraseAudioMapper.toDto(fraseAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraseAudioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fraseAudioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraseAudio in the database
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFraseAudio() throws Exception {
        // Initialize the database
        fraseAudioRepository.saveAndFlush(fraseAudio);

        int databaseSizeBeforeDelete = fraseAudioRepository.findAll().size();

        // Delete the fraseAudio
        restFraseAudioMockMvc
            .perform(delete(ENTITY_API_URL_ID, fraseAudio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FraseAudio> fraseAudioList = fraseAudioRepository.findAll();
        assertThat(fraseAudioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
