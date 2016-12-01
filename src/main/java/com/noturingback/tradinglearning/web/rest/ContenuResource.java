package com.noturingback.tradinglearning.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noturingback.tradinglearning.domain.Contenu;
import com.noturingback.tradinglearning.service.ContenuService;
import com.noturingback.tradinglearning.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Contenu.
 */
@RestController
@RequestMapping("/api")
public class ContenuResource {

    private final Logger log = LoggerFactory.getLogger(ContenuResource.class);
        
    @Inject
    private ContenuService contenuService;

    /**
     * POST  /contenus : Create a new contenu.
     *
     * @param contenu the contenu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contenu, or with status 400 (Bad Request) if the contenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contenus")
    @Timed
    public ResponseEntity<Contenu> createContenu(@RequestBody Contenu contenu) throws URISyntaxException {
        log.debug("REST request to save Contenu : {}", contenu);
        if (contenu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contenu", "idexists", "A new contenu cannot already have an ID")).body(null);
        }
        Contenu result = contenuService.save(contenu);
        return ResponseEntity.created(new URI("/api/contenus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contenu", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contenus : Updates an existing contenu.
     *
     * @param contenu the contenu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contenu,
     * or with status 400 (Bad Request) if the contenu is not valid,
     * or with status 500 (Internal Server Error) if the contenu couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contenus")
    @Timed
    public ResponseEntity<Contenu> updateContenu(@RequestBody Contenu contenu) throws URISyntaxException {
        log.debug("REST request to update Contenu : {}", contenu);
        if (contenu.getId() == null) {
            return createContenu(contenu);
        }
        Contenu result = contenuService.save(contenu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contenu", contenu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contenus : get all the contenus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contenus in body
     */
    @GetMapping("/contenus")
    @Timed
    public List<Contenu> getAllContenus() {
        log.debug("REST request to get all Contenus");
        return contenuService.findAll();
    }

    /**
     * GET  /contenus/:id : get the "id" contenu.
     *
     * @param id the id of the contenu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contenu, or with status 404 (Not Found)
     */
    @GetMapping("/contenus/{id}")
    @Timed
    public ResponseEntity<Contenu> getContenu(@PathVariable Long id) {
        log.debug("REST request to get Contenu : {}", id);
        Contenu contenu = contenuService.findOne(id);
        return Optional.ofNullable(contenu)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contenus/:id : delete the "id" contenu.
     *
     * @param id the id of the contenu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contenus/{id}")
    @Timed
    public ResponseEntity<Void> deleteContenu(@PathVariable Long id) {
        log.debug("REST request to delete Contenu : {}", id);
        contenuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contenu", id.toString())).build();
    }

    /**
     * SEARCH  /_search/contenus?query=:query : search for the contenu corresponding
     * to the query.
     *
     * @param query the query of the contenu search 
     * @return the result of the search
     */
    @GetMapping("/_search/contenus")
    @Timed
    public List<Contenu> searchContenus(@RequestParam String query) {
        log.debug("REST request to search Contenus for query {}", query);
        return contenuService.search(query);
    }


}
