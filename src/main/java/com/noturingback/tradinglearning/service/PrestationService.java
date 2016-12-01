package com.noturingback.tradinglearning.service;

import com.noturingback.tradinglearning.domain.Prestation;
import com.noturingback.tradinglearning.repository.PrestationRepository;
import com.noturingback.tradinglearning.repository.search.PrestationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Prestation.
 */
@Service
@Transactional
public class PrestationService {

    private final Logger log = LoggerFactory.getLogger(PrestationService.class);
    
    @Inject
    private PrestationRepository prestationRepository;

    @Inject
    private PrestationSearchRepository prestationSearchRepository;

    /**
     * Save a prestation.
     *
     * @param prestation the entity to save
     * @return the persisted entity
     */
    public Prestation save(Prestation prestation) {
        log.debug("Request to save Prestation : {}", prestation);
        Prestation result = prestationRepository.save(prestation);
        prestationSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the prestations.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Prestation> findAll() {
        log.debug("Request to get all Prestations");
        List<Prestation> result = prestationRepository.findAll();

        return result;
    }

    /**
     *  Get one prestation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Prestation findOne(Long id) {
        log.debug("Request to get Prestation : {}", id);
        Prestation prestation = prestationRepository.findOne(id);
        return prestation;
    }

    /**
     *  Delete the  prestation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Prestation : {}", id);
        prestationRepository.delete(id);
        prestationSearchRepository.delete(id);
    }

    /**
     * Search for the prestation corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Prestation> search(String query) {
        log.debug("Request to search Prestations for query {}", query);
        return StreamSupport
            .stream(prestationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
