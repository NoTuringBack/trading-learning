package com.noturingback.tradinglearning.web.rest;

import com.noturingback.tradinglearning.TradinglearningApp;

import com.noturingback.tradinglearning.domain.Contenu;
import com.noturingback.tradinglearning.repository.ContenuRepository;
import com.noturingback.tradinglearning.service.ContenuService;
import com.noturingback.tradinglearning.repository.search.ContenuSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noturingback.tradinglearning.domain.enumeration.Type;
import com.noturingback.tradinglearning.domain.enumeration.Categorie;
/**
 * Test class for the ContenuResource REST controller.
 *
 * @see ContenuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradinglearningApp.class)
public class ContenuResourceIntTest {

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final Type DEFAULT_TYPE = Type.ARTICLE;
    private static final Type UPDATED_TYPE = Type.VIDEO;

    private static final Categorie DEFAULT_CATEGORIE = Categorie.AGRICULTURE;
    private static final Categorie UPDATED_CATEGORIE = Categorie.ANIMALIER;

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DONNEES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DONNEES = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DONNEES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DONNEES_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_PRIX = 1;
    private static final Integer UPDATED_PRIX = 2;

    @Inject
    private ContenuRepository contenuRepository;

    @Inject
    private ContenuService contenuService;

    @Inject
    private ContenuSearchRepository contenuSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContenuMockMvc;

    private Contenu contenu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContenuResource contenuResource = new ContenuResource();
        ReflectionTestUtils.setField(contenuResource, "contenuService", contenuService);
        this.restContenuMockMvc = MockMvcBuilders.standaloneSetup(contenuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contenu createEntity(EntityManager em) {
        Contenu contenu = new Contenu()
                .dateCreation(DEFAULT_DATE_CREATION)
                .type(DEFAULT_TYPE)
                .categorie(DEFAULT_CATEGORIE)
                .titre(DEFAULT_TITRE)
                .description(DEFAULT_DESCRIPTION)
                .donnees(DEFAULT_DONNEES)
                .donneesContentType(DEFAULT_DONNEES_CONTENT_TYPE)
                .prix(DEFAULT_PRIX);
        return contenu;
    }

    @Before
    public void initTest() {
        contenuSearchRepository.deleteAll();
        contenu = createEntity(em);
    }

    @Test
    @Transactional
    public void createContenu() throws Exception {
        int databaseSizeBeforeCreate = contenuRepository.findAll().size();

        // Create the Contenu

        restContenuMockMvc.perform(post("/api/contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contenu)))
            .andExpect(status().isCreated());

        // Validate the Contenu in the database
        List<Contenu> contenus = contenuRepository.findAll();
        assertThat(contenus).hasSize(databaseSizeBeforeCreate + 1);
        Contenu testContenu = contenus.get(contenus.size() - 1);
        assertThat(testContenu.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testContenu.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContenu.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testContenu.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testContenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContenu.getDonnees()).isEqualTo(DEFAULT_DONNEES);
        assertThat(testContenu.getDonneesContentType()).isEqualTo(DEFAULT_DONNEES_CONTENT_TYPE);
        assertThat(testContenu.getPrix()).isEqualTo(DEFAULT_PRIX);

        // Validate the Contenu in ElasticSearch
        Contenu contenuEs = contenuSearchRepository.findOne(testContenu.getId());
        assertThat(contenuEs).isEqualToComparingFieldByField(testContenu);
    }

    @Test
    @Transactional
    public void getAllContenus() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        // Get all the contenus
        restContenuMockMvc.perform(get("/api/contenus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].donneesContentType").value(hasItem(DEFAULT_DONNEES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].donnees").value(hasItem(Base64Utils.encodeToString(DEFAULT_DONNEES))))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX)));
    }

    @Test
    @Transactional
    public void getContenu() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        // Get the contenu
        restContenuMockMvc.perform(get("/api/contenus/{id}", contenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contenu.getId().intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.toString()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.donneesContentType").value(DEFAULT_DONNEES_CONTENT_TYPE))
            .andExpect(jsonPath("$.donnees").value(Base64Utils.encodeToString(DEFAULT_DONNEES)))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX));
    }

    @Test
    @Transactional
    public void getNonExistingContenu() throws Exception {
        // Get the contenu
        restContenuMockMvc.perform(get("/api/contenus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContenu() throws Exception {
        // Initialize the database
        contenuService.save(contenu);

        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();

        // Update the contenu
        Contenu updatedContenu = contenuRepository.findOne(contenu.getId());
        updatedContenu
                .dateCreation(UPDATED_DATE_CREATION)
                .type(UPDATED_TYPE)
                .categorie(UPDATED_CATEGORIE)
                .titre(UPDATED_TITRE)
                .description(UPDATED_DESCRIPTION)
                .donnees(UPDATED_DONNEES)
                .donneesContentType(UPDATED_DONNEES_CONTENT_TYPE)
                .prix(UPDATED_PRIX);

        restContenuMockMvc.perform(put("/api/contenus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContenu)))
            .andExpect(status().isOk());

        // Validate the Contenu in the database
        List<Contenu> contenus = contenuRepository.findAll();
        assertThat(contenus).hasSize(databaseSizeBeforeUpdate);
        Contenu testContenu = contenus.get(contenus.size() - 1);
        assertThat(testContenu.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testContenu.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContenu.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testContenu.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testContenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContenu.getDonnees()).isEqualTo(UPDATED_DONNEES);
        assertThat(testContenu.getDonneesContentType()).isEqualTo(UPDATED_DONNEES_CONTENT_TYPE);
        assertThat(testContenu.getPrix()).isEqualTo(UPDATED_PRIX);

        // Validate the Contenu in ElasticSearch
        Contenu contenuEs = contenuSearchRepository.findOne(testContenu.getId());
        assertThat(contenuEs).isEqualToComparingFieldByField(testContenu);
    }

    @Test
    @Transactional
    public void deleteContenu() throws Exception {
        // Initialize the database
        contenuService.save(contenu);

        int databaseSizeBeforeDelete = contenuRepository.findAll().size();

        // Get the contenu
        restContenuMockMvc.perform(delete("/api/contenus/{id}", contenu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contenuExistsInEs = contenuSearchRepository.exists(contenu.getId());
        assertThat(contenuExistsInEs).isFalse();

        // Validate the database is empty
        List<Contenu> contenus = contenuRepository.findAll();
        assertThat(contenus).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContenu() throws Exception {
        // Initialize the database
        contenuService.save(contenu);

        // Search the contenu
        restContenuMockMvc.perform(get("/api/_search/contenus?query=id:" + contenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].donneesContentType").value(hasItem(DEFAULT_DONNEES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].donnees").value(hasItem(Base64Utils.encodeToString(DEFAULT_DONNEES))))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX)));
    }
}
