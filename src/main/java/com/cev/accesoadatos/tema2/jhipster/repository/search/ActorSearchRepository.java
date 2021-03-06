package com.cev.accesoadatos.tema2.jhipster.repository.search;

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

import com.cev.accesoadatos.tema2.jhipster.domain.Actor;

/**
 * Spring Data Elasticsearch repository for the {@link Actor} entity.
 */
public interface ActorSearchRepository extends ElasticsearchRepository<Actor, Long>, ActorSearchRepositoryInternal {}

interface ActorSearchRepositoryInternal {
    Page<Actor> search(String query, Pageable pageable);
}

class ActorSearchRepositoryInternalImpl implements ActorSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ActorSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Actor> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Actor> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Actor.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
