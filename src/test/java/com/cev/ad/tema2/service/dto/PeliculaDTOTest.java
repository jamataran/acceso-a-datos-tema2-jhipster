package com.cev.ad.tema2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cev.ad.tema2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeliculaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeliculaDTO.class);
        PeliculaDTO peliculaDTO1 = new PeliculaDTO();
        peliculaDTO1.setId(1L);
        PeliculaDTO peliculaDTO2 = new PeliculaDTO();
        assertThat(peliculaDTO1).isNotEqualTo(peliculaDTO2);
        peliculaDTO2.setId(peliculaDTO1.getId());
        assertThat(peliculaDTO1).isEqualTo(peliculaDTO2);
        peliculaDTO2.setId(2L);
        assertThat(peliculaDTO1).isNotEqualTo(peliculaDTO2);
        peliculaDTO1.setId(null);
        assertThat(peliculaDTO1).isNotEqualTo(peliculaDTO2);
    }
}
