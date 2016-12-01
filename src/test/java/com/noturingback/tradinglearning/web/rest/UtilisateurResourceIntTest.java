package com.noturingback.tradinglearning.web.rest;

import com.noturingback.tradinglearning.TradinglearningApp;

import com.noturingback.tradinglearning.domain.Utilisateur;
import com.noturingback.tradinglearning.repository.UtilisateurRepository;
import com.noturingback.tradinglearning.service.UtilisateurService;
import com.noturingback.tradinglearning.repository.search.UtilisateurSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UtilisateurResource REST controller.
 *
 * @see UtilisateurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradinglearningApp.class)
public class UtilisateurResourceIntTest {

    private static final Integer DEFAULT_CREDITS = 1;
    private static final Integer UPDATED_CREDITS = 2;

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurService utilisateurService;

    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UtilisateurResource utilisateurResource = new UtilisateurResource();
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurService", utilisateurService);
        this.restUtilisateurMockMvc = MockMvcBuilders.standaloneSetup(utilisateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
                .credits(DEFAULT_CREDITS);
        return utilisateur;
    }

    @Before
    public void initTest() {
        utilisateurSearchRepository.deleteAll();
        utilisateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // Create the Utilisateur

        restUtilisateurMockMvc.perform(post("/api/utilisateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(utilisateur)))
            .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getCredits()).isEqualTo(DEFAULT_CREDITS);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurs
        restUtilisateurMockMvc.perform(get("/api/utilisateurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)));
    }

    @Test
    @Transactional
    public void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.credits").value(DEFAULT_CREDITS));
    }

    @Test
    @Transactional
    public void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUtilisateur() throws Exception {
        // Initialize the database
        utilisateurService.save(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findOne(utilisateur.getId());
        updatedUtilisateur
                .credits(UPDATED_CREDITS);

        restUtilisateurMockMvc.perform(put("/api/utilisateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUtilisateur)))
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getCredits()).isEqualTo(UPDATED_CREDITS);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurService.save(utilisateur);

        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Get the utilisateur
        restUtilisateurMockMvc.perform(delete("/api/utilisateurs/{id}", utilisateur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean utilisateurExistsInEs = utilisateurSearchRepository.exists(utilisateur.getId());
        assertThat(utilisateurExistsInEs).isFalse();

        // Validate the database is empty
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUtilisateur() throws Exception {
        // Initialize the database
        utilisateurService.save(utilisateur);

        // Search the utilisateur
        restUtilisateurMockMvc.perform(get("/api/_search/utilisateurs?query=id:" + utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)));
    }
}
