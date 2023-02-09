package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LinguaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lingua.class);
        Lingua lingua1 = new Lingua();
        lingua1.setIdLingua(1L);
        Lingua lingua2 = new Lingua();
        lingua2.setIdLingua(lingua1.getIdLingua());
        assertThat(lingua1).isEqualTo(lingua2);
        lingua2.setIdLingua(2L);
        assertThat(lingua1).isNotEqualTo(lingua2);
        lingua1.setIdLingua(null);
        assertThat(lingua1).isNotEqualTo(lingua2);
    }
}
