package com.cev.accesoadatos.tema2.jhipster.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.cev.accesoadatos.tema2.jhipster.domain.Pelicula;

/**
 * Spring Data Elasticsearch repository for the {@link Pelicula} entity.
 */
public interface PeliculaSearchRepository extends ElasticsearchRepository<Pelicula, Long>, PeliculaSearchRepositoryInternal {}

interface PeliculaSearchRepositoryInternal {
    Stream<Pelicula> search(String query);
}

class PeliculaSearchRepositoryInternalImpl implements PeliculaSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PeliculaSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Pelicula> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Pelicula.class).map(SearchHit::getContent).stream();
    }
}
