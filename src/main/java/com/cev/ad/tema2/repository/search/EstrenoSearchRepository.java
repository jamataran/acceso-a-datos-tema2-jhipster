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

import com.cev.ad.tema2.domain.Estreno;

/**
 * Spring Data Elasticsearch repository for the {@link Estreno} entity.
 */
public interface EstrenoSearchRepository extends ElasticsearchRepository<Estreno, Long>, EstrenoSearchRepositoryInternal {}

interface EstrenoSearchRepositoryInternal {
    Page<Estreno> search(String query, Pageable pageable);
}

class EstrenoSearchRepositoryInternalImpl implements EstrenoSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    EstrenoSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Estreno> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Estreno> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Estreno.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
