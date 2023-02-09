package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Audio;
import com.mycompany.myapp.domain.enumeration.Stato;
import com.mycompany.myapp.repository.AudioRepository;
import com.mycompany.myapp.service.dto.AudioDTO;
import com.mycompany.myapp.service.mapper.AudioMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AudioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AudioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Stato DEFAULT_CODICE_STATO = Stato.BOZZA;
    private static final Stato UPDATED_CODICE_STATO = Stato.PRIVATO;

    private static final byte[] DEFAULT_TRACK = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TRACK = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TRACK_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TRACK_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_DATA_CREAZIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CREAZIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_ULTIMA_MODIFICA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ULTIMA_MODIFICA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ELIMINATO = false;
    private static final Boolean UPDATED_ELIMINATO = true;

    private static final String ENTITY_API_URL = "/api/audio";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{idAudio}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private AudioMapper audioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAudioMockMvc;

    private Audio audio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audio createEntity(EntityManager em) {
        Audio audio = new Audio()
            .nome(DEFAULT_NOME)
            .codiceStato(DEFAULT_CODICE_STATO)
            .track(DEFAULT_TRACK)
            .trackContentType(DEFAULT_TRACK_CONTENT_TYPE)
            .dataCreazione(DEFAULT_DATA_CREAZIONE)
            .dataUltimaModifica(DEFAULT_DATA_ULTIMA_MODIFICA)
            .eliminato(DEFAULT_ELIMINATO);
        return audio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audio createUpdatedEntity(EntityManager em) {
        Audio audio = new Audio()
            .nome(UPDATED_NOME)
            .codiceStato(UPDATED_CODICE_STATO)
            .track(UPDATED_TRACK)
            .trackContentType(UPDATED_TRACK_CONTENT_TYPE)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        return audio;
    }

    @BeforeEach
    public void initTest() {
        audio = createEntity(em);
    }

    @Test
    @Transactional
    void createAudio() throws Exception {
        int databaseSizeBeforeCreate = audioRepository.findAll().size();
        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);
        restAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isCreated());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeCreate + 1);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAudio.getCodiceStato()).isEqualTo(DEFAULT_CODICE_STATO);
        assertThat(testAudio.getTrack()).isEqualTo(DEFAULT_TRACK);
        assertThat(testAudio.getTrackContentType()).isEqualTo(DEFAULT_TRACK_CONTENT_TYPE);
        assertThat(testAudio.getDataCreazione()).isEqualTo(DEFAULT_DATA_CREAZIONE);
        assertThat(testAudio.getDataUltimaModifica()).isEqualTo(DEFAULT_DATA_ULTIMA_MODIFICA);
        assertThat(testAudio.getEliminato()).isEqualTo(DEFAULT_ELIMINATO);
    }

    @Test
    @Transactional
    void createAudioWithExistingId() throws Exception {
        // Create the Audio with an existing ID
        audio.setIdAudio(1L);
        AudioDTO audioDTO = audioMapper.toDto(audio);

        int databaseSizeBeforeCreate = audioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList
        restAudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=idAudio,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idAudio").value(hasItem(audio.getIdAudio().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].codiceStato").value(hasItem(DEFAULT_CODICE_STATO.toString())))
            .andExpect(jsonPath("$.[*].trackContentType").value(hasItem(DEFAULT_TRACK_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].track").value(hasItem(Base64Utils.encodeToString(DEFAULT_TRACK))))
            .andExpect(jsonPath("$.[*].dataCreazione").value(hasItem(sameInstant(DEFAULT_DATA_CREAZIONE))))
            .andExpect(jsonPath("$.[*].dataUltimaModifica").value(hasItem(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA))))
            .andExpect(jsonPath("$.[*].eliminato").value(hasItem(DEFAULT_ELIMINATO.booleanValue())));
    }

    @Test
    @Transactional
    void getAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get the audio
        restAudioMockMvc
            .perform(get(ENTITY_API_URL_ID, audio.getIdAudio()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.idAudio").value(audio.getIdAudio().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.codiceStato").value(DEFAULT_CODICE_STATO.toString()))
            .andExpect(jsonPath("$.trackContentType").value(DEFAULT_TRACK_CONTENT_TYPE))
            .andExpect(jsonPath("$.track").value(Base64Utils.encodeToString(DEFAULT_TRACK)))
            .andExpect(jsonPath("$.dataCreazione").value(sameInstant(DEFAULT_DATA_CREAZIONE)))
            .andExpect(jsonPath("$.dataUltimaModifica").value(sameInstant(DEFAULT_DATA_ULTIMA_MODIFICA)))
            .andExpect(jsonPath("$.eliminato").value(DEFAULT_ELIMINATO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAudio() throws Exception {
        // Get the audio
        restAudioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Update the audio
        Audio updatedAudio = audioRepository.findById(audio.getIdAudio()).get();
        // Disconnect from session so that the updates on updatedAudio are not directly saved in db
        em.detach(updatedAudio);
        updatedAudio
            .nome(UPDATED_NOME)
            .codiceStato(UPDATED_CODICE_STATO)
            .track(UPDATED_TRACK)
            .trackContentType(UPDATED_TRACK_CONTENT_TYPE)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);
        AudioDTO audioDTO = audioMapper.toDto(updatedAudio);

        restAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, audioDTO.getIdAudio())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(audioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAudio.getCodiceStato()).isEqualTo(UPDATED_CODICE_STATO);
        assertThat(testAudio.getTrack()).isEqualTo(UPDATED_TRACK);
        assertThat(testAudio.getTrackContentType()).isEqualTo(UPDATED_TRACK_CONTENT_TYPE);
        assertThat(testAudio.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testAudio.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testAudio.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void putNonExistingAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();
        audio.setIdAudio(count.incrementAndGet());

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, audioDTO.getIdAudio())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(audioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();
        audio.setIdAudio(count.incrementAndGet());

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(audioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();
        audio.setIdAudio(count.incrementAndGet());

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAudioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAudioWithPatch() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Update the audio using partial update
        Audio partialUpdatedAudio = new Audio();
        partialUpdatedAudio.setIdAudio(audio.getIdAudio());

        partialUpdatedAudio
            .nome(UPDATED_NOME)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);

        restAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAudio.getIdAudio())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAudio))
            )
            .andExpect(status().isOk());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAudio.getCodiceStato()).isEqualTo(DEFAULT_CODICE_STATO);
        assertThat(testAudio.getTrack()).isEqualTo(DEFAULT_TRACK);
        assertThat(testAudio.getTrackContentType()).isEqualTo(DEFAULT_TRACK_CONTENT_TYPE);
        assertThat(testAudio.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testAudio.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testAudio.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void fullUpdateAudioWithPatch() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Update the audio using partial update
        Audio partialUpdatedAudio = new Audio();
        partialUpdatedAudio.setIdAudio(audio.getIdAudio());

        partialUpdatedAudio
            .nome(UPDATED_NOME)
            .codiceStato(UPDATED_CODICE_STATO)
            .track(UPDATED_TRACK)
            .trackContentType(UPDATED_TRACK_CONTENT_TYPE)
            .dataCreazione(UPDATED_DATA_CREAZIONE)
            .dataUltimaModifica(UPDATED_DATA_ULTIMA_MODIFICA)
            .eliminato(UPDATED_ELIMINATO);

        restAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAudio.getIdAudio())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAudio))
            )
            .andExpect(status().isOk());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAudio.getCodiceStato()).isEqualTo(UPDATED_CODICE_STATO);
        assertThat(testAudio.getTrack()).isEqualTo(UPDATED_TRACK);
        assertThat(testAudio.getTrackContentType()).isEqualTo(UPDATED_TRACK_CONTENT_TYPE);
        assertThat(testAudio.getDataCreazione()).isEqualTo(UPDATED_DATA_CREAZIONE);
        assertThat(testAudio.getDataUltimaModifica()).isEqualTo(UPDATED_DATA_ULTIMA_MODIFICA);
        assertThat(testAudio.getEliminato()).isEqualTo(UPDATED_ELIMINATO);
    }

    @Test
    @Transactional
    void patchNonExistingAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();
        audio.setIdAudio(count.incrementAndGet());

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, audioDTO.getIdAudio())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(audioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();
        audio.setIdAudio(count.incrementAndGet());

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(audioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();
        audio.setIdAudio(count.incrementAndGet());

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAudioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        int databaseSizeBeforeDelete = audioRepository.findAll().size();

        // Delete the audio
        restAudioMockMvc
            .perform(delete(ENTITY_API_URL_ID, audio.getIdAudio()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
