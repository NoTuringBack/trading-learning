package com.noturingback.tradinglearning.service;

import com.noturingback.tradinglearning.domain.Contenu;
import com.noturingback.tradinglearning.repository.ContenuRepository;
import com.noturingback.tradinglearning.repository.search.ContenuSearchRepository;
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
 * Service Implementation for managing Contenu.
 */
@Service
@Transactional
public class ContenuService {

    private final Logger log = LoggerFactory.getLogger(ContenuService.class);
    
    @Inject
    private ContenuRepository contenuRepository;

    @Inject
    private ContenuSearchRepository contenuSearchRepository;

    /**
     * Save a contenu.
     *
     * @param contenu the entity to save
     * @return the persisted entity
     */
    public Contenu save(Contenu contenu) {
        log.debug("Request to save Contenu : {}", contenu);
        Contenu result = contenuRepository.save(contenu);
        contenuSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the contenus.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Contenu> findAll() {
        log.debug("Request to get all Contenus");
        List<Contenu> result = contenuRepository.findAll();

        return result;
    }

    /**
     *  Get one contenu by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Contenu findOne(Long id) {
        log.debug("Request to get Contenu : {}", id);
        Contenu contenu = contenuRepository.findOne(id);
        return contenu;
    }

    /**
     *  Delete the  contenu by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contenu : {}", id);
        contenuRepository.delete(id);
        contenuSearchRepository.delete(id);
    }

    /**
     * Search for the contenu corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Contenu> search(String query) {
        log.debug("Request to search Contenus for query {}", query);
        return StreamSupport
            .stream(contenuSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
