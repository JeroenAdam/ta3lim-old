package com.adambahri.app.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.adambahri.app.domain.Skill;
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
 * Spring Data Elasticsearch repository for the {@link Skill} entity.
 */
public interface SkillSearchRepository extends ElasticsearchRepository<Skill, Long>, SkillSearchRepositoryInternal {}

interface SkillSearchRepositoryInternal {
    Page<Skill> search(String query, Pageable pageable);
}

class SkillSearchRepositoryInternalImpl implements SkillSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    SkillSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Skill> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Skill> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Skill.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
