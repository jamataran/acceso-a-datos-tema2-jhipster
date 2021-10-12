package com.cev.accesoadatos.tema2.jhipster.domain;

import org.junit.jupiter.api.Test;

import com.cev.accesoadatos.tema2.jhipster.web.rest.TestUtil;

class PeliculaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pelicula.class);
        Pelicula pelicula1 = new Pelicula();
        pelicula1.setId(1L);
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setId(pelicula1.getId());
        assertThat(pelicula1).isEqualTo(pelicula2);
        pelicula2.setId(2L);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
        pelicula1.setId(null);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
    }
}
