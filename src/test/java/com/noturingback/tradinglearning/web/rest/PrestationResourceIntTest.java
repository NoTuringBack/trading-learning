package com.noturingback.tradinglearning.web.rest;

import com.noturingback.tradinglearning.TradinglearningApp;

import com.noturingback.tradinglearning.domain.Prestation;
import com.noturingback.tradinglearning.repository.PrestationRepository;
import com.noturingback.tradinglearning.service.PrestationService;
import com.noturingback.tradinglearning.repository.search.PrestationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.noturingback.tradinglearning.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noturingback.tradinglearning.domain.enumeration.Categorie;
/**
 * Test class for the PrestationResource REST controller.
 *
 * @see PrestationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradinglearningApp.class)
public class PrestationResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_DEBUT_LIVRAISON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT_LIVRAISON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN_LIVRAISON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN_LIVRAISON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Categorie DEFAULT_CATEGORIE = Categorie.AGRICULTURE;
    private static final Categorie UPDATED_CATEGORIE = Categorie.ANIMALIER;

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_BENEFICIAIRES_MAX = 1;
    private static final Integer UPDATED_NB_BENEFICIAIRES_MAX = 2;

    private static final Integer DEFAULT_PRIX = 1;
    private static final Integer UPDATED_PRIX = 2;

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEMANDE = false;
    private static final Boolean UPDATED_DEMANDE = true;

    private static final Boolean DEFAULT_EFFECTUE = false;
    private static final Boolean UPDATED_EFFECTUE = true;

    @Inject
    private PrestationRepository prestationRepository;

    @Inject
    private PrestationService prestationService;

    @Inject
    private PrestationSearchRepository prestationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPrestationMockMvc;

    private Prestation prestation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrestationResource prestationResource = new PrestationResource();
        ReflectionTestUtils.setField(prestationResource, "prestationService", prestationService);
        this.restPrestationMockMvc = MockMvcBuilders.standaloneSetup(prestationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestation createEntity(EntityManager em) {
        Prestation prestation = new Prestation()
                .dateDebutLivraison(DEFAULT_DATE_DEBUT_LIVRAISON)
                .dateFinLivraison(DEFAULT_DATE_FIN_LIVRAISON)
                .categorie(DEFAULT_CATEGORIE)
                .titre(DEFAULT_TITRE)
                .description(DEFAULT_DESCRIPTION)
                .nbBeneficiairesMax(DEFAULT_NB_BENEFICIAIRES_MAX)
                .prix(DEFAULT_PRIX)
                .lieu(DEFAULT_LIEU)
                .demande(DEFAULT_DEMANDE)
                .effectue(DEFAULT_EFFECTUE);
        return prestation;
    }

    @Before
    public void initTest() {
        prestationSearchRepository.deleteAll();
        prestation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrestation() throws Exception {
        int databaseSizeBeforeCreate = prestationRepository.findAll().size();

        // Create the Prestation

        restPrestationMockMvc.perform(post("/api/prestations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prestation)))
            .andExpect(status().isCreated());

        // Validate the Prestation in the database
        List<Prestation> prestations = prestationRepository.findAll();
        assertThat(prestations).hasSize(databaseSizeBeforeCreate + 1);
        Prestation testPrestation = prestations.get(prestations.size() - 1);
        assertThat(testPrestation.getDateDebutLivraison()).isEqualTo(DEFAULT_DATE_DEBUT_LIVRAISON);
        assertThat(testPrestation.getDateFinLivraison()).isEqualTo(DEFAULT_DATE_FIN_LIVRAISON);
        assertThat(testPrestation.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testPrestation.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testPrestation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrestation.getNbBeneficiairesMax()).isEqualTo(DEFAULT_NB_BENEFICIAIRES_MAX);
        assertThat(testPrestation.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testPrestation.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testPrestation.isDemande()).isEqualTo(DEFAULT_DEMANDE);
        assertThat(testPrestation.isEffectue()).isEqualTo(DEFAULT_EFFECTUE);

        // Validate the Prestation in ElasticSearch
        Prestation prestationEs = prestationSearchRepository.findOne(testPrestation.getId());
        assertThat(prestationEs).isEqualToComparingFieldByField(testPrestation);
    }

    @Test
    @Transactional
    public void getAllPrestations() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        // Get all the prestations
        restPrestationMockMvc.perform(get("/api/prestations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestation.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebutLivraison").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT_LIVRAISON))))
            .andExpect(jsonPath("$.[*].dateFinLivraison").value(hasItem(sameInstant(DEFAULT_DATE_FIN_LIVRAISON))))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nbBeneficiairesMax").value(hasItem(DEFAULT_NB_BENEFICIAIRES_MAX)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX)))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].demande").value(hasItem(DEFAULT_DEMANDE.booleanValue())))
            .andExpect(jsonPath("$.[*].effectue").value(hasItem(DEFAULT_EFFECTUE.booleanValue())));
    }

    @Test
    @Transactional
    public void getPrestation() throws Exception {
        // Initialize the database
        prestationRepository.saveAndFlush(prestation);

        // Get the prestation
        restPrestationMockMvc.perform(get("/api/prestations/{id}", prestation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prestation.getId().intValue()))
            .andExpect(jsonPath("$.dateDebutLivraison").value(sameInstant(DEFAULT_DATE_DEBUT_LIVRAISON)))
            .andExpect(jsonPath("$.dateFinLivraison").value(sameInstant(DEFAULT_DATE_FIN_LIVRAISON)))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.toString()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nbBeneficiairesMax").value(DEFAULT_NB_BENEFICIAIRES_MAX))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU.toString()))
            .andExpect(jsonPath("$.demande").value(DEFAULT_DEMANDE.booleanValue()))
            .andExpect(jsonPath("$.effectue").value(DEFAULT_EFFECTUE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrestation() throws Exception {
        // Get the prestation
        restPrestationMockMvc.perform(get("/api/prestations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrestation() throws Exception {
        // Initialize the database
        prestationService.save(prestation);

        int databaseSizeBeforeUpdate = prestationRepository.findAll().size();

        // Update the prestation
        Prestation updatedPrestation = prestationRepository.findOne(prestation.getId());
        updatedPrestation
                .dateDebutLivraison(UPDATED_DATE_DEBUT_LIVRAISON)
                .dateFinLivraison(UPDATED_DATE_FIN_LIVRAISON)
                .categorie(UPDATED_CATEGORIE)
                .titre(UPDATED_TITRE)
                .description(UPDATED_DESCRIPTION)
                .nbBeneficiairesMax(UPDATED_NB_BENEFICIAIRES_MAX)
                .prix(UPDATED_PRIX)
                .lieu(UPDATED_LIEU)
                .demande(UPDATED_DEMANDE)
                .effectue(UPDATED_EFFECTUE);

        restPrestationMockMvc.perform(put("/api/prestations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrestation)))
            .andExpect(status().isOk());

        // Validate the Prestation in the database
        List<Prestation> prestations = prestationRepository.findAll();
        assertThat(prestations).hasSize(databaseSizeBeforeUpdate);
        Prestation testPrestation = prestations.get(prestations.size() - 1);
        assertThat(testPrestation.getDateDebutLivraison()).isEqualTo(UPDATED_DATE_DEBUT_LIVRAISON);
        assertThat(testPrestation.getDateFinLivraison()).isEqualTo(UPDATED_DATE_FIN_LIVRAISON);
        assertThat(testPrestation.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testPrestation.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testPrestation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrestation.getNbBeneficiairesMax()).isEqualTo(UPDATED_NB_BENEFICIAIRES_MAX);
        assertThat(testPrestation.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPrestation.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testPrestation.isDemande()).isEqualTo(UPDATED_DEMANDE);
        assertThat(testPrestation.isEffectue()).isEqualTo(UPDATED_EFFECTUE);

        // Validate the Prestation in ElasticSearch
        Prestation prestationEs = prestationSearchRepository.findOne(testPrestation.getId());
        assertThat(prestationEs).isEqualToComparingFieldByField(testPrestation);
    }

    @Test
    @Transactional
    public void deletePrestation() throws Exception {
        // Initialize the database
        prestationService.save(prestation);

        int databaseSizeBeforeDelete = prestationRepository.findAll().size();

        // Get the prestation
        restPrestationMockMvc.perform(delete("/api/prestations/{id}", prestation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean prestationExistsInEs = prestationSearchRepository.exists(prestation.getId());
        assertThat(prestationExistsInEs).isFalse();

        // Validate the database is empty
        List<Prestation> prestations = prestationRepository.findAll();
        assertThat(prestations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrestation() throws Exception {
        // Initialize the database
        prestationService.save(prestation);

        // Search the prestation
        restPrestationMockMvc.perform(get("/api/_search/prestations?query=id:" + prestation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestation.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebutLivraison").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT_LIVRAISON))))
            .andExpect(jsonPath("$.[*].dateFinLivraison").value(hasItem(sameInstant(DEFAULT_DATE_FIN_LIVRAISON))))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nbBeneficiairesMax").value(hasItem(DEFAULT_NB_BENEFICIAIRES_MAX)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX)))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].demande").value(hasItem(DEFAULT_DEMANDE.booleanValue())))
            .andExpect(jsonPath("$.[*].effectue").value(hasItem(DEFAULT_EFFECTUE.booleanValue())));
    }
}
