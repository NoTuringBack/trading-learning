package com.noturingback.tradinglearning.repository.search;

import com.noturingback.tradinglearning.domain.Prestation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Prestation entity.
 */
public interface PrestationSearchRepository extends ElasticsearchRepository<Prestation, Long> {
}
