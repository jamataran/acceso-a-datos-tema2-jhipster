package com.cev.accesoadatos.tema2.jhipster.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PeliculaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PeliculaSearchRepositoryMockConfiguration {

    @MockBean
    private PeliculaSearchRepository mockPeliculaSearchRepository;
}
