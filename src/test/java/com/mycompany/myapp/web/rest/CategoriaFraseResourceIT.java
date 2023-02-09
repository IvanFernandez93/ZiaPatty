package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CategoriaFrase;
import com.mycompany.myapp.repository.CategoriaFraseRepository;
import com.mycompany.myapp.service.dto.CategoriaFraseDTO;
import com.mycompany.myapp.service.mapper.CategoriaFraseMapper;
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
 * Integration tests for the {@link CategoriaFraseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaFraseResourceIT {

    private static final Long DEFAULT_ID_CATEGORIA = 1L;
    private static final Long UPDATED_ID_CATEGORIA = 2L;

    private static final Long DEFAULT_ID_FRASE = 1L;
    private static final Long UPDATED_ID_FRASE = 2L;

    private static final String ENTITY_API_URL = "/api/categoria-frases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaFraseRepository categoriaFraseRepository;

    @Autowired
    private CategoriaFraseMapper categoriaFraseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaFraseMockMvc;

    private CategoriaFrase categoriaFrase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaFrase createEntity(EntityManager em) {
        CategoriaFrase categoriaFrase = new CategoriaFrase().idCategoria(DEFAULT_ID_CATEGORIA).idFrase(DEFAULT_ID_FRASE);
        return categoriaFrase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaFrase createUpdatedEntity(EntityManager em) {
        CategoriaFrase categoriaFrase = new CategoriaFrase().idCategoria(UPDATED_ID_CATEGORIA).idFrase(UPDATED_ID_FRASE);
        return categoriaFrase;
    }

    @BeforeEach
    public void initTest() {
        categoriaFrase = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoriaFrase() throws Exception {
        int databaseSizeBeforeCreate = categoriaFraseRepository.findAll().size();
        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);
        restCategoriaFraseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaFrase testCategoriaFrase = categoriaFraseList.get(categoriaFraseList.size() - 1);
        assertThat(testCategoriaFrase.getIdCategoria()).isEqualTo(DEFAULT_ID_CATEGORIA);
        assertThat(testCategoriaFrase.getIdFrase()).isEqualTo(DEFAULT_ID_FRASE);
    }

    @Test
    @Transactional
    void createCategoriaFraseWithExistingId() throws Exception {
        // Create the CategoriaFrase with an existing ID
        categoriaFrase.setId(1L);
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        int databaseSizeBeforeCreate = categoriaFraseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaFraseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategoriaFrases() throws Exception {
        // Initialize the database
        categoriaFraseRepository.saveAndFlush(categoriaFrase);

        // Get all the categoriaFraseList
        restCategoriaFraseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaFrase.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCategoria").value(hasItem(DEFAULT_ID_CATEGORIA.intValue())))
            .andExpect(jsonPath("$.[*].idFrase").value(hasItem(DEFAULT_ID_FRASE.intValue())));
    }

    @Test
    @Transactional
    void getCategoriaFrase() throws Exception {
        // Initialize the database
        categoriaFraseRepository.saveAndFlush(categoriaFrase);

        // Get the categoriaFrase
        restCategoriaFraseMockMvc
            .perform(get(ENTITY_API_URL_ID, categoriaFrase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoriaFrase.getId().intValue()))
            .andExpect(jsonPath("$.idCategoria").value(DEFAULT_ID_CATEGORIA.intValue()))
            .andExpect(jsonPath("$.idFrase").value(DEFAULT_ID_FRASE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCategoriaFrase() throws Exception {
        // Get the categoriaFrase
        restCategoriaFraseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoriaFrase() throws Exception {
        // Initialize the database
        categoriaFraseRepository.saveAndFlush(categoriaFrase);

        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();

        // Update the categoriaFrase
        CategoriaFrase updatedCategoriaFrase = categoriaFraseRepository.findById(categoriaFrase.getId()).get();
        // Disconnect from session so that the updates on updatedCategoriaFrase are not directly saved in db
        em.detach(updatedCategoriaFrase);
        updatedCategoriaFrase.idCategoria(UPDATED_ID_CATEGORIA).idFrase(UPDATED_ID_FRASE);
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(updatedCategoriaFrase);

        restCategoriaFraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaFraseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
        CategoriaFrase testCategoriaFrase = categoriaFraseList.get(categoriaFraseList.size() - 1);
        assertThat(testCategoriaFrase.getIdCategoria()).isEqualTo(UPDATED_ID_CATEGORIA);
        assertThat(testCategoriaFrase.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
    }

    @Test
    @Transactional
    void putNonExistingCategoriaFrase() throws Exception {
        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();
        categoriaFrase.setId(count.incrementAndGet());

        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaFraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaFraseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoriaFrase() throws Exception {
        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();
        categoriaFrase.setId(count.incrementAndGet());

        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaFraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoriaFrase() throws Exception {
        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();
        categoriaFrase.setId(count.incrementAndGet());

        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaFraseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaFraseWithPatch() throws Exception {
        // Initialize the database
        categoriaFraseRepository.saveAndFlush(categoriaFrase);

        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();

        // Update the categoriaFrase using partial update
        CategoriaFrase partialUpdatedCategoriaFrase = new CategoriaFrase();
        partialUpdatedCategoriaFrase.setId(categoriaFrase.getId());

        restCategoriaFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaFrase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaFrase))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
        CategoriaFrase testCategoriaFrase = categoriaFraseList.get(categoriaFraseList.size() - 1);
        assertThat(testCategoriaFrase.getIdCategoria()).isEqualTo(DEFAULT_ID_CATEGORIA);
        assertThat(testCategoriaFrase.getIdFrase()).isEqualTo(DEFAULT_ID_FRASE);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaFraseWithPatch() throws Exception {
        // Initialize the database
        categoriaFraseRepository.saveAndFlush(categoriaFrase);

        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();

        // Update the categoriaFrase using partial update
        CategoriaFrase partialUpdatedCategoriaFrase = new CategoriaFrase();
        partialUpdatedCategoriaFrase.setId(categoriaFrase.getId());

        partialUpdatedCategoriaFrase.idCategoria(UPDATED_ID_CATEGORIA).idFrase(UPDATED_ID_FRASE);

        restCategoriaFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaFrase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaFrase))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
        CategoriaFrase testCategoriaFrase = categoriaFraseList.get(categoriaFraseList.size() - 1);
        assertThat(testCategoriaFrase.getIdCategoria()).isEqualTo(UPDATED_ID_CATEGORIA);
        assertThat(testCategoriaFrase.getIdFrase()).isEqualTo(UPDATED_ID_FRASE);
    }

    @Test
    @Transactional
    void patchNonExistingCategoriaFrase() throws Exception {
        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();
        categoriaFrase.setId(count.incrementAndGet());

        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaFraseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoriaFrase() throws Exception {
        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();
        categoriaFrase.setId(count.incrementAndGet());

        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaFraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoriaFrase() throws Exception {
        int databaseSizeBeforeUpdate = categoriaFraseRepository.findAll().size();
        categoriaFrase.setId(count.incrementAndGet());

        // Create the CategoriaFrase
        CategoriaFraseDTO categoriaFraseDTO = categoriaFraseMapper.toDto(categoriaFrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaFraseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaFraseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaFrase in the database
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoriaFrase() throws Exception {
        // Initialize the database
        categoriaFraseRepository.saveAndFlush(categoriaFrase);

        int databaseSizeBeforeDelete = categoriaFraseRepository.findAll().size();

        // Delete the categoriaFrase
        restCategoriaFraseMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoriaFrase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoriaFrase> categoriaFraseList = categoriaFraseRepository.findAll();
        assertThat(categoriaFraseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
