package com.noturingback.tradinglearning.service;

import com.noturingback.tradinglearning.domain.Utilisateur;
import com.noturingback.tradinglearning.repository.UtilisateurRepository;
import com.noturingback.tradinglearning.repository.search.UtilisateurSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Utilisateur.
 */
@Service
@Transactional
public class UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurService.class);

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;

    /**
     * Save a utilisateur.
     *
     * @param utilisateur the entity to save
     * @return the persisted entity
     */
    public Utilisateur save(Utilisateur utilisateur) {
        log.debug("Request to save Utilisateur : {}", utilisateur);
        Utilisateur result = utilisateurRepository.save(utilisateur);
        utilisateurSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the utilisateurs.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        log.debug("Request to get all Utilisateurs");
        List<Utilisateur> result = utilisateurRepository.findAllWithEagerRelationships();

        return result;
    }

    /**
     *  Get one utilisateur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Utilisateur findOne(Long id) {
        log.debug("Request to get Utilisateur : {}", id);
        Utilisateur utilisateur = utilisateurRepository.findOneWithEagerRelationships(id);
        return utilisateur;
    }

    /**
     *  Get current utilisateur.
     *
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Utilisateur findCurrent() {
        log.debug("Request to get current Utilisateur");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userLogin = user.getUsername(); //get logged in username
        log.debug("userLogin : " + userLogin);

        Utilisateur utilisateur = utilisateurRepository.findByUser_Login(userLogin);
        log.debug("utilisateur : " + utilisateur.getCredits());
        return utilisateur;
    }

    /**
     *  Delete the  utilisateur by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.delete(id);
        utilisateurSearchRepository.delete(id);
    }

    /**
     * Search for the utilisateur corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Utilisateur> search(String query) {
        log.debug("Request to search Utilisateurs for query {}", query);
        return StreamSupport
            .stream(utilisateurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
