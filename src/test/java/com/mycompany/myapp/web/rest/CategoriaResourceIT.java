package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.domain.enumeration.Stato;
import com.mycompany.myapp.repository.CategoriaRepository;
import com.mycompany.myapp.service.dto.CategoriaDTO;
import com.mycompany.myapp.service.mapper.CategoriaMapper;
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
 * Integration tests for the {@link CategoriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_CATEGORIA_PADRE = 1L;
    private static final Long UPDATED_ID_CATEGORIA_PADRE = 2L;

    private static final Stato DEFAULT_CODICE_STATO = Stato.BOZZA;
    private static final Stato UPDATED_CODICE_STATO = Stato.PRIVATO;

    private static final ZonedDateTime DEFAULT_DATA_CREAZIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CREAZIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ULTIMA_MODIFICA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ULTIMA_MODIFICA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ELIMINATO = false;
    private static final Boolean UPDATED_ELIMINATO = true;

    private static final String ENTITY_API_URL = "/api/categorias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{idCategoria}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaMockMvc;

    private Categoria categoria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categoria createEntity(EntityManager em) {
        Categoria categoria = new Categoria()
            .nome(DEFAULT_NOME)
            .idCategoriaPadre(DEFAULT_ID_CATEGORIA_PADRE)
            .codiceStato(DEFAULT_CODICE_STATO)
            .dataCreazione(DEFAULT_DATA_CREAZIONE)
            .dataUltimaModifica(DEFAULT_DATA_ULTIMA_MODIFICA)
            .eliminato(DEFAULT_ELIMINATO);
        return categoria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categoria createUpdatedEntity(EntityManager em) {
        Categoria categoria = new Categoria()
            .nome(UPDATED_NOME)
            .idCategoriaPadre(UPDATED_ID_CATEGORIA_PADRE)
            .codiceStato(UPDATED_CODICE_STATO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        return categoria;
    }

    @BeforeEach
    public void initTest() {
        categoria = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoria() throws Exception {
        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();
        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);
        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isCreated());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate + 1);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCategoria.getIdCategoriaPadre()).isEqualTo(DEFAULT_ID_CATEGORIA_PADRE);
        assertThat(testCategoria.getCodiceStato()).isEqualTo(DEFAULT_CODICE_STATO);
        assertThat(testCategoria.getDataCreazione()).isEqualTo(DEFAULT_DATA_CREAZIONE);
        assertThat(testCategoria.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testCategoria.getEliminato()).isEqualTo(DEFAULT_ELIMINATO);
    }

    @Test
    @Transactional
    void createCategoriaWithExistingId() throws Exception {
        // Create the Categoria with an existing ID
        categoria.setIdCategoria(1L);
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategorias() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=idCategoria,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idCategoria").value(hasItem(categoria.getIdCategoria().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].idCategoriaPadre").value(hasItem(DEFAULT_ID_CATEGORIA_PADRE.intValue())))
            .andExpect(jsonPath("$.[*].codiceStato").value(hasItem(DEFAULT_CODICE_STATO.toString())))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(sameInstant(DEFAULT_DATA_CREAZIONE))))
            .andExpect(jsonPath("$.[*].dataUltimaModifica").value(hasItem(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA))))
            .andExpect(jsonPath("$.[*].eliminato").value(hasItem(DEFAULT_ELIMINATO.booleanValue())));
    }

    @Test
    @Transactional
    void getCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get the categoria
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoria.getIdCategoria()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.idCategoria").value(categoria.getIdCategoria().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.idCategoriaPadre").value(DEFAULT_ID_CATEGORIA_PADRE.intValue()))
            .andExpect(jsonPath("$.codiceStato").value(DEFAULT_CODICE_STATO.toString()))
            .andExpect(jsonPath("$.dataCreazione").value(sameInstant(DEFAULT_DATA_CREAZIONE)))
            .andExpect(jsonPath("$.dataUltimaModifica").value(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA)))
            .andExpect(jsonPath("$.eliminato").value(DEFAULT_ELIMINATO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCategoria() throws Exception {
        // Get the categoria
        restCategoriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria
        Categoria updatedCategoria = categoriaRepository.findById(categoria.getIdCategoria()).get();
        // Disconnect from session so that the updates on updatedCategoria are not directly saved in db
        em.detach(updatedCategoria);
        updatedCategoria
            .nome(UPDATED_NOME)
            .idCategoriaPadre(UPDATED_ID_CATEGORIA_PADRE)
            .codiceStato(UPDATED_CODICE_STATO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(updatedCategoria);

        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaDTO.getIdCategoria())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCategoria.getIdCategoriaPadre()).isEqualTo(UPDATED_ID_CATEGORIA_PADRE);
        assertThat(testCategoria.getCodiceStato()).isEqualTo(UPDATED_CODICE_STATO);
        assertThat(testCategoria.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testCategoria.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testCategoria.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void putNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setIdCategoria(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaDTO.getIdCategoria())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setIdCategoria(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setIdCategoria(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaWithPatch() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria using partial update
        Categoria partialUpdatedCategoria = new Categoria();
        partialUpdatedCategoria.setIdCategoria(categoria.getIdCategoria());

        partialUpdatedCategoria.nome(UPDATED_NOME).idCategoriaPadre(UPDATED_ID_CATEGORIA_PADRE).dataCreazione(UPDATED_DATA_CREAZIONE);

        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoria.getIdCategoria())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCategoria.getIdCategoriaPadre()).isEqualTo(UPDATED_ID_CATEGORIA_PADRE);
        assertThat(testCategoria.getCodiceStato()).isEqualTo(DEFAULT_CODICE_STATO);
        assertThat(testCategoria.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testCategoria.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testCategoria.getEliminato()).isEqualTo(DEFAULT_ELIMINATO);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaWithPatch() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria using partial update
        Categoria partialUpdatedCategoria = new Categoria();
        partialUpdatedCategoria.setIdCategoria(categoria.getIdCategoria());

        partialUpdatedCategoria
            .nome(UPDATED_NOME)
            .idCategoriaPadre(UPDATED_ID_CATEGORIA_PADRE)
            .codiceStato(UPDATED_CODICE_STATO)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);

        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoria.getIdCategoria())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCategoria.getIdCategoriaPadre()).isEqualTo(UPDATED_ID_CATEGORIA_PADRE);
        assertThat(testCategoria.getCodiceStato()).isEqualTo(UPDATED_CODICE_STATO);
        assertThat(testCategoria.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testCategoria.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testCategoria.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void patchNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setIdCategoria(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaDTO.getIdCategoria())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setIdCategoria(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setIdCategoria(count.incrementAndGet());

        // Create the Categoria
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoriaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeDelete = categoriaRepository.findAll().size();

        // Delete the categoria
        restCategoriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoria.getIdCategoria()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
