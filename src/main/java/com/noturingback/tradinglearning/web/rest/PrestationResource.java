package com.noturingback.tradinglearning.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noturingback.tradinglearning.domain.Prestation;
import com.noturingback.tradinglearning.service.PrestationService;
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
 * REST controller for managing Prestation.
 */
@RestController
@RequestMapping("/api")
public class PrestationResource {

    private final Logger log = LoggerFactory.getLogger(PrestationResource.class);
        
    @Inject
    private PrestationService prestationService;

    /**
     * POST  /prestations : Create a new prestation.
     *
     * @param prestation the prestation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prestation, or with status 400 (Bad Request) if the prestation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prestations")
    @Timed
    public ResponseEntity<Prestation> createPrestation(@RequestBody Prestation prestation) throws URISyntaxException {
        log.debug("REST request to save Prestation : {}", prestation);
        if (prestation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("prestation", "idexists", "A new prestation cannot already have an ID")).body(null);
        }
        Prestation result = prestationService.save(prestation);
        return ResponseEntity.created(new URI("/api/prestations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("prestation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prestations : Updates an existing prestation.
     *
     * @param prestation the prestation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prestation,
     * or with status 400 (Bad Request) if the prestation is not valid,
     * or with status 500 (Internal Server Error) if the prestation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prestations")
    @Timed
    public ResponseEntity<Prestation> updatePrestation(@RequestBody Prestation prestation) throws URISyntaxException {
        log.debug("REST request to update Prestation : {}", prestation);
        if (prestation.getId() == null) {
            return createPrestation(prestation);
        }
        Prestation result = prestationService.save(prestation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("prestation", prestation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prestations : get all the prestations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of prestations in body
     */
    @GetMapping("/prestations")
    @Timed
    public List<Prestation> getAllPrestations() {
        log.debug("REST request to get all Prestations");
        return prestationService.findAll();
    }

    /**
     * GET  /prestations/:id : get the "id" prestation.
     *
     * @param id the id of the prestation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prestation, or with status 404 (Not Found)
     */
    @GetMapping("/prestations/{id}")
    @Timed
    public ResponseEntity<Prestation> getPrestation(@PathVariable Long id) {
        log.debug("REST request to get Prestation : {}", id);
        Prestation prestation = prestationService.findOne(id);
        return Optional.ofNullable(prestation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prestations/:id : delete the "id" prestation.
     *
     * @param id the id of the prestation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prestations/{id}")
    @Timed
    public ResponseEntity<Void> deletePrestation(@PathVariable Long id) {
        log.debug("REST request to delete Prestation : {}", id);
        prestationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prestation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/prestations?query=:query : search for the prestation corresponding
     * to the query.
     *
     * @param query the query of the prestation search 
     * @return the result of the search
     */
    @GetMapping("/_search/prestations")
    @Timed
    public List<Prestation> searchPrestations(@RequestParam String query) {
        log.debug("REST request to search Prestations for query {}", query);
        return prestationService.search(query);
    }


}
