package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FraseAudioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FraseAudio.class);
        FraseAudio fraseAudio1 = new FraseAudio();
        fraseAudio1.setId(1L);
        FraseAudio fraseAudio2 = new FraseAudio();
        fraseAudio2.setId(fraseAudio1.getId());
        assertThat(fraseAudio1).isEqualTo(fraseAudio2);
        fraseAudio2.setId(2L);
        assertThat(fraseAudio1).isNotEqualTo(fraseAudio2);
        fraseAudio1.setId(null);
        assertThat(fraseAudio1).isNotEqualTo(fraseAudio2);
    }
}
