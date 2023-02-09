package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AudioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AudioDTO.class);
        AudioDTO audioDTO1 = new AudioDTO();
        audioDTO1.setIdAudio(1L);
        AudioDTO audioDTO2 = new AudioDTO();
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
        audioDTO2.setIdAudio(audioDTO1.getIdAudio());
        assertThat(audioDTO1).isEqualTo(audioDTO2);
        audioDTO2.setIdAudio(2L);
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
        audioDTO1.setIdAudio(null);
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
    }
}
