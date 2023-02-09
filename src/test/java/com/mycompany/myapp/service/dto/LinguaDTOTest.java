package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinguaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinguaDTO.class);
        LinguaDTO linguaDTO1 = new LinguaDTO();
        linguaDTO1.setIdLingua(1L);
        LinguaDTO linguaDTO2 = new LinguaDTO();
        assertThat(linguaDTO1).isNotEqualTo(linguaDTO2);
        linguaDTO2.setIdLingua(linguaDTO1.getIdLingua());
        assertThat(linguaDTO1).isEqualTo(linguaDTO2);
        linguaDTO2.setIdLingua(2L);
        assertThat(linguaDTO1).isNotEqualTo(linguaDTO2);
        linguaDTO1.setIdLingua(null);
        assertThat(linguaDTO1).isNotEqualTo(linguaDTO2);
    }
}
