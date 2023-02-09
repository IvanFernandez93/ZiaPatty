package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaFraseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaFrase.class);
        CategoriaFrase categoriaFrase1 = new CategoriaFrase();
        categoriaFrase1.setId(1L);
        CategoriaFrase categoriaFrase2 = new CategoriaFrase();
        categoriaFrase2.setId(categoriaFrase1.getId());
        assertThat(categoriaFrase1).isEqualTo(categoriaFrase2);
        categoriaFrase2.setId(2L);
        assertThat(categoriaFrase1).isNotEqualTo(categoriaFrase2);
        categoriaFrase1.setId(null);
        assertThat(categoriaFrase1).isNotEqualTo(categoriaFrase2);
    }
}
