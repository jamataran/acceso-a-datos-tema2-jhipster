package com.cev.ad.tema2.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.cev.ad.tema2.domain.Review;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Review} entity.
 */
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long>, ReviewSearchRepositoryInternal {}

interface ReviewSearchRepositoryInternal {
    Page<Review> search(String query, Pageable pageable);
}

class ReviewSearchRepositoryInternalImpl implements ReviewSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ReviewSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Review> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Review> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Review.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
