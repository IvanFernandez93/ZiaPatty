package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaDTO.class);
        CategoriaDTO categoriaDTO1 = new CategoriaDTO();
        categoriaDTO1.setIdCategoria(1L);
        CategoriaDTO categoriaDTO2 = new CategoriaDTO();
        assertThat(categoriaDTO1).isNotEqualTo(categoriaDTO2);
        categoriaDTO2.setIdCategoria(categoriaDTO1.getIdCategoria());
        assertThat(categoriaDTO1).isEqualTo(categoriaDTO2);
        categoriaDTO2.setIdCategoria(2L);
        assertThat(categoriaDTO1).isNotEqualTo(categoriaDTO2);
        categoriaDTO1.setIdCategoria(null);
        assertThat(categoriaDTO1).isNotEqualTo(categoriaDTO2);
    }
}
