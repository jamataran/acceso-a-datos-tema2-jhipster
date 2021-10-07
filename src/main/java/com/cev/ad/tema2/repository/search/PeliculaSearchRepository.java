package com.cev.ad.tema2.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.cev.ad.tema2.domain.Pelicula;

/**
 * Spring Data Elasticsearch repository for the {@link Pelicula} entity.
 */
public interface PeliculaSearchRepository extends ElasticsearchRepository<Pelicula, Long>, PeliculaSearchRepositoryInternal {}

interface PeliculaSearchRepositoryInternal {
    Page<Pelicula> search(String query, Pageable pageable);
}

class PeliculaSearchRepositoryInternalImpl implements PeliculaSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PeliculaSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Pelicula> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Pelicula> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Pelicula.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
