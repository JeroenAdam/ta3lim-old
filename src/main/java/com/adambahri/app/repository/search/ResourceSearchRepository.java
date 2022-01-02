package com.adambahri.app.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.adambahri.app.domain.Resource;
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
 * Spring Data Elasticsearch repository for the {@link Resource} entity.
 */
public interface ResourceSearchRepository extends ElasticsearchRepository<Resource, Long>, ResourceSearchRepositoryInternal {}

interface ResourceSearchRepositoryInternal {
    Page<Resource> search(String query, Pageable pageable);
}

class ResourceSearchRepositoryInternalImpl implements ResourceSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ResourceSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Resource> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Resource> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Resource.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
