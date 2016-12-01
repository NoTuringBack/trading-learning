package com.noturingback.tradinglearning.repository.search;

import com.noturingback.tradinglearning.domain.Utilisateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Utilisateur entity.
 */
public interface UtilisateurSearchRepository extends ElasticsearchRepository<Utilisateur, Long> {
}
