package com.noturingback.tradinglearning.repository.search;

import com.noturingback.tradinglearning.domain.Contenu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Contenu entity.
 */
public interface ContenuSearchRepository extends ElasticsearchRepository<Contenu, Long> {
}
