package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaFraseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaFraseDTO.class);
        CategoriaFraseDTO categoriaFraseDTO1 = new CategoriaFraseDTO();
        categoriaFraseDTO1.setId(1L);
        CategoriaFraseDTO categoriaFraseDTO2 = new CategoriaFraseDTO();
        assertThat(categoriaFraseDTO1).isNotEqualTo(categoriaFraseDTO2);
        categoriaFraseDTO2.setId(categoriaFraseDTO1.getId());
        assertThat(categoriaFraseDTO1).isEqualTo(categoriaFraseDTO2);
        categoriaFraseDTO2.setId(2L);
        assertThat(categoriaFraseDTO1).isNotEqualTo(categoriaFraseDTO2);
        categoriaFraseDTO1.setId(null);
        assertThat(categoriaFraseDTO1).isNotEqualTo(categoriaFraseDTO2);
    }
}
