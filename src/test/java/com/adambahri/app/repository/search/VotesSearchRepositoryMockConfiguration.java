package com.adambahri.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link VotesSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class VotesSearchRepositoryMockConfiguration {

    @MockBean
    private VotesSearchRepository mockVotesSearchRepository;
}
