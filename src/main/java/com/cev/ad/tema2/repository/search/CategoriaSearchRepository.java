package com.cev.ad.tema2.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.stream.Stream;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.cev.ad.tema2.domain.Categoria;

/**
 * Spring Data Elasticsearch repository for the {@link Categoria} entity.
 */
public interface CategoriaSearchRepository extends ElasticsearchRepository<Categoria, Long>, CategoriaSearchRepositoryInternal {}

interface CategoriaSearchRepositoryInternal {
    Stream<Categoria> search(String query);
}

class CategoriaSearchRepositoryInternalImpl implements CategoriaSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CategoriaSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Categoria> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Categoria.class).map(SearchHit::getContent).stream();
    }
}
