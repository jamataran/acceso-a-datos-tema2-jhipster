package com.cev.ad.tema2.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.cev.ad.tema2.web.rest.TestUtil;

class EstrenoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstrenoDTO.class);
        EstrenoDTO estrenoDTO1 = new EstrenoDTO();
        estrenoDTO1.setId(1L);
        EstrenoDTO estrenoDTO2 = new EstrenoDTO();
        assertThat(estrenoDTO1).isNotEqualTo(estrenoDTO2);
        estrenoDTO2.setId(estrenoDTO1.getId());
        assertThat(estrenoDTO1).isEqualTo(estrenoDTO2);
        estrenoDTO2.setId(2L);
        assertThat(estrenoDTO1).isNotEqualTo(estrenoDTO2);
        estrenoDTO1.setId(null);
        assertThat(estrenoDTO1).isNotEqualTo(estrenoDTO2);
    }
}
