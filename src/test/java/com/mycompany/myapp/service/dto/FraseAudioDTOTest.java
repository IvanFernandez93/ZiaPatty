package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FraseAudioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FraseAudioDTO.class);
        FraseAudioDTO fraseAudioDTO1 = new FraseAudioDTO();
        fraseAudioDTO1.setId(1L);
        FraseAudioDTO fraseAudioDTO2 = new FraseAudioDTO();
        assertThat(fraseAudioDTO1).isNotEqualTo(fraseAudioDTO2);
        fraseAudioDTO2.setId(fraseAudioDTO1.getId());
        assertThat(fraseAudioDTO1).isEqualTo(fraseAudioDTO2);
        fraseAudioDTO2.setId(2L);
        assertThat(fraseAudioDTO1).isNotEqualTo(fraseAudioDTO2);
        fraseAudioDTO1.setId(null);
        assertThat(fraseAudioDTO1).isNotEqualTo(fraseAudioDTO2);
    }
}
