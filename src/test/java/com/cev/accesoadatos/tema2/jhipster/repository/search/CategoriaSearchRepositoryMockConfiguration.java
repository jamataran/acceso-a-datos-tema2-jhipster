package com.cev.accesoadatos.tema2.jhipster.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CategoriaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CategoriaSearchRepositoryMockConfiguration {

    @MockBean
    private CategoriaSearchRepository mockCategoriaSearchRepository;
}
