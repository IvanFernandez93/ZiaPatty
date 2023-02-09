package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categoria.class);
        Categoria categoria1 = new Categoria();
        categoria1.setIdCategoria(1L);
        Categoria categoria2 = new Categoria();
        categoria2.setIdCategoria(categoria1.getIdCategoria());
        assertThat(categoria1).isEqualTo(categoria2);
        categoria2.setIdCategoria(2L);
        assertThat(categoria1).isNotEqualTo(categoria2);
        categoria1.setIdCategoria(null);
        assertThat(categoria1).isNotEqualTo(categoria2);
    }
}
