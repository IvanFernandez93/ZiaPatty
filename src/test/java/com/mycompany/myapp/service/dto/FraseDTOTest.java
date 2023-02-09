package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FraseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FraseDTO.class);
        FraseDTO fraseDTO1 = new FraseDTO();
        fraseDTO1.setId(1L);
        FraseDTO fraseDTO2 = new FraseDTO();
        assertThat(fraseDTO1).isNotEqualTo(fraseDTO2);
        fraseDTO2.setId(fraseDTO1.getId());
        assertThat(fraseDTO1).isEqualTo(fraseDTO2);
        fraseDTO2.setId(2L);
        assertThat(fraseDTO1).isNotEqualTo(fraseDTO2);
        fraseDTO1.setId(null);
        assertThat(fraseDTO1).isNotEqualTo(fraseDTO2);
    }
}
