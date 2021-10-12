package com.cev.accesoadatos.tema2.jhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.cev.accesoadatos.tema2.jhipster.web.rest.TestUtil;

class EstrenoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estreno.class);
        Estreno estreno1 = new Estreno();
        estreno1.setId(1L);
        Estreno estreno2 = new Estreno();
        estreno2.setId(estreno1.getId());
        assertThat(estreno1).isEqualTo(estreno2);
        estreno2.setId(2L);
        assertThat(estreno1).isNotEqualTo(estreno2);
        estreno1.setId(null);
        assertThat(estreno1).isNotEqualTo(estreno2);
    }
}
