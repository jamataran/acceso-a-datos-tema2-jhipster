package com.cev.ad.tema2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cev.ad.tema2.IntegrationTest;
import com.cev.ad.tema2.domain.Estreno;
import com.cev.ad.tema2.domain.Pelicula;
import com.cev.ad.tema2.domain.Review;
import com.cev.ad.tema2.repository.PeliculaRepository;
import com.cev.ad.tema2.repository.search.PeliculaSearchRepository;
import com.cev.ad.tema2.service.criteria.PeliculaCriteria;
import com.cev.ad.tema2.service.dto.PeliculaDTO;
import com.cev.ad.tema2.service.mapper.PeliculaMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeliculaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PeliculaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EN_CINES = false;
    private static final Boolean UPDATED_EN_CINES = true;

    private static final String ENTITY_API_URL = "/api/peliculas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/peliculas";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private PeliculaMapper peliculaMapper;

    /**
     * This repository is mocked in the com.cev.ad.tema2.repository.search test package.
     *
     * @see com.cev.ad.tema2.repository.search.PeliculaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PeliculaSearchRepository mockPeliculaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeliculaMockMvc;

    private Pelicula pelicula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula().titulo(DEFAULT_TITULO).descripcion(DEFAULT_DESCRIPCION).enCines(DEFAULT_EN_CINES);
        // Add required entity
        Estreno estreno;
        if (TestUtil.findAll(em, Estreno.class).isEmpty()) {
            estreno = EstrenoResourceIT.createEntity(em);
            em.persist(estreno);
            em.flush();
        } else {
            estreno = TestUtil.findAll(em, Estreno.class).get(0);
        }
        pelicula.setEstreno(estreno);
        return pelicula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createUpdatedEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula().titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION).enCines(UPDATED_EN_CINES);
        // Add required entity
        Estreno estreno;
        if (TestUtil.findAll(em, Estreno.class).isEmpty()) {
            estreno = EstrenoResourceIT.createUpdatedEntity(em);
            em.persist(estreno);
            em.flush();
        } else {
            estreno = TestUtil.findAll(em, Estreno.class).get(0);
        }
        pelicula.setEstreno(estreno);
        return pelicula;
    }

    @BeforeEach
    public void initTest() {
        pelicula = createEntity(em);
    }

    @Test
    @Transactional
    void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();
        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);
        restPeliculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peliculaDTO)))
            .andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate + 1);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPelicula.getEnCines()).isEqualTo(DEFAULT_EN_CINES);

        // Validate the id for MapsId, the ids must be same
        assertThat(testPelicula.getId()).isEqualTo(testPelicula.getEstreno().getId());

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(1)).save(testPelicula);
    }

    @Test
    @Transactional
    void createPeliculaWithExistingId() throws Exception {
        // Create the Pelicula with an existing ID
        pelicula.setId(1L);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeliculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peliculaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void updatePeliculaMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Add a new parent entity
        Estreno estreno = EstrenoResourceIT.createUpdatedEntity(em);
        em.persist(estreno);
        em.flush();

        // Load the pelicula
        Pelicula updatedPelicula = peliculaRepository.findById(pelicula.getId()).get();
        assertThat(updatedPelicula).isNotNull();
        // Disconnect from session so that the updates on updatedPelicula are not directly saved in db
        em.detach(updatedPelicula);

        // Update the Estreno with new association value
        updatedPelicula.setEstreno(estreno);
        PeliculaDTO updatedPeliculaDTO = peliculaMapper.toDto(updatedPelicula);
        assertThat(updatedPeliculaDTO).isNotNull();

        // Update the entity
        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeliculaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeliculaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testPelicula.getId()).isEqualTo(testPelicula.getEstreno().getId());

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository).save(pelicula);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setTitulo(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        restPeliculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peliculaDTO)))
            .andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setDescripcion(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        restPeliculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peliculaDTO)))
            .andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeliculas() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].enCines").value(hasItem(DEFAULT_EN_CINES.booleanValue())));
    }

    @Test
    @Transactional
    void getPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get the pelicula
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL_ID, pelicula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pelicula.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.enCines").value(DEFAULT_EN_CINES.booleanValue()));
    }

    @Test
    @Transactional
    void getPeliculasByIdFiltering() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        Long id = pelicula.getId();

        defaultPeliculaShouldBeFound("id.equals=" + id);
        defaultPeliculaShouldNotBeFound("id.notEquals=" + id);

        defaultPeliculaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPeliculaShouldNotBeFound("id.greaterThan=" + id);

        defaultPeliculaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPeliculaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeliculasByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where titulo equals to DEFAULT_TITULO
        defaultPeliculaShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the peliculaList where titulo equals to UPDATED_TITULO
        defaultPeliculaShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllPeliculasByTituloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where titulo not equals to DEFAULT_TITULO
        defaultPeliculaShouldNotBeFound("titulo.notEquals=" + DEFAULT_TITULO);

        // Get all the peliculaList where titulo not equals to UPDATED_TITULO
        defaultPeliculaShouldBeFound("titulo.notEquals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllPeliculasByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultPeliculaShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the peliculaList where titulo equals to UPDATED_TITULO
        defaultPeliculaShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllPeliculasByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where titulo is not null
        defaultPeliculaShouldBeFound("titulo.specified=true");

        // Get all the peliculaList where titulo is null
        defaultPeliculaShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllPeliculasByTituloContainsSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where titulo contains DEFAULT_TITULO
        defaultPeliculaShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the peliculaList where titulo contains UPDATED_TITULO
        defaultPeliculaShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllPeliculasByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where titulo does not contain DEFAULT_TITULO
        defaultPeliculaShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the peliculaList where titulo does not contain UPDATED_TITULO
        defaultPeliculaShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllPeliculasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultPeliculaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the peliculaList where descripcion equals to UPDATED_DESCRIPCION
        defaultPeliculaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPeliculasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultPeliculaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the peliculaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultPeliculaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPeliculasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultPeliculaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the peliculaList where descripcion equals to UPDATED_DESCRIPCION
        defaultPeliculaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPeliculasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where descripcion is not null
        defaultPeliculaShouldBeFound("descripcion.specified=true");

        // Get all the peliculaList where descripcion is null
        defaultPeliculaShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllPeliculasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where descripcion contains DEFAULT_DESCRIPCION
        defaultPeliculaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the peliculaList where descripcion contains UPDATED_DESCRIPCION
        defaultPeliculaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPeliculasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultPeliculaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the peliculaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultPeliculaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPeliculasByEnCinesIsEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where enCines equals to DEFAULT_EN_CINES
        defaultPeliculaShouldBeFound("enCines.equals=" + DEFAULT_EN_CINES);

        // Get all the peliculaList where enCines equals to UPDATED_EN_CINES
        defaultPeliculaShouldNotBeFound("enCines.equals=" + UPDATED_EN_CINES);
    }

    @Test
    @Transactional
    void getAllPeliculasByEnCinesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where enCines not equals to DEFAULT_EN_CINES
        defaultPeliculaShouldNotBeFound("enCines.notEquals=" + DEFAULT_EN_CINES);

        // Get all the peliculaList where enCines not equals to UPDATED_EN_CINES
        defaultPeliculaShouldBeFound("enCines.notEquals=" + UPDATED_EN_CINES);
    }

    @Test
    @Transactional
    void getAllPeliculasByEnCinesIsInShouldWork() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where enCines in DEFAULT_EN_CINES or UPDATED_EN_CINES
        defaultPeliculaShouldBeFound("enCines.in=" + DEFAULT_EN_CINES + "," + UPDATED_EN_CINES);

        // Get all the peliculaList where enCines equals to UPDATED_EN_CINES
        defaultPeliculaShouldNotBeFound("enCines.in=" + UPDATED_EN_CINES);
    }

    @Test
    @Transactional
    void getAllPeliculasByEnCinesIsNullOrNotNull() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList where enCines is not null
        defaultPeliculaShouldBeFound("enCines.specified=true");

        // Get all the peliculaList where enCines is null
        defaultPeliculaShouldNotBeFound("enCines.specified=false");
    }

    @Test
    @Transactional
    void getAllPeliculasByEstrenoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Estreno estreno = pelicula.getEstreno();
        peliculaRepository.saveAndFlush(pelicula);
        Long estrenoId = estreno.getId();

        // Get all the peliculaList where estreno equals to estrenoId
        defaultPeliculaShouldBeFound("estrenoId.equals=" + estrenoId);

        // Get all the peliculaList where estreno equals to (estrenoId + 1)
        defaultPeliculaShouldNotBeFound("estrenoId.equals=" + (estrenoId + 1));
    }

    @Test
    @Transactional
    void getAllPeliculasByReviewIsEqualToSomething() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        Review review;
        if (TestUtil.findAll(em, Review.class).isEmpty()) {
            review = ReviewResourceIT.createEntity(em);
            em.persist(review);
            em.flush();
        } else {
            review = TestUtil.findAll(em, Review.class).get(0);
        }
        em.persist(review);
        em.flush();
        pelicula.addReview(review);
        peliculaRepository.saveAndFlush(pelicula);
        Long reviewId = review.getId();

        // Get all the peliculaList where review equals to reviewId
        defaultPeliculaShouldBeFound("reviewId.equals=" + reviewId);

        // Get all the peliculaList where review equals to (reviewId + 1)
        defaultPeliculaShouldNotBeFound("reviewId.equals=" + (reviewId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeliculaShouldBeFound(String filter) throws Exception {
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].enCines").value(hasItem(DEFAULT_EN_CINES.booleanValue())));

        // Check, that the count call also returns 1
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeliculaShouldNotBeFound(String filter) throws Exception {
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeliculaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPelicula() throws Exception {
        // Get the pelicula
        restPeliculaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findById(pelicula.getId()).get();
        // Disconnect from session so that the updates on updatedPelicula are not directly saved in db
        em.detach(updatedPelicula);
        updatedPelicula.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION).enCines(UPDATED_EN_CINES);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(updatedPelicula);

        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peliculaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPelicula.getEnCines()).isEqualTo(UPDATED_EN_CINES);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository).save(testPelicula);
    }

    @Test
    @Transactional
    void putNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peliculaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void putWithIdMismatchPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peliculaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void partialUpdatePeliculaWithPatch() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula using partial update
        Pelicula partialUpdatedPelicula = new Pelicula();
        partialUpdatedPelicula.setId(pelicula.getId());

        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPelicula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPelicula))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPelicula.getEnCines()).isEqualTo(DEFAULT_EN_CINES);
    }

    @Test
    @Transactional
    void fullUpdatePeliculaWithPatch() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula using partial update
        Pelicula partialUpdatedPelicula = new Pelicula();
        partialUpdatedPelicula.setId(pelicula.getId());

        partialUpdatedPelicula.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION).enCines(UPDATED_EN_CINES);

        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPelicula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPelicula))
            )
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPelicula.getEnCines()).isEqualTo(UPDATED_EN_CINES);
    }

    @Test
    @Transactional
    void patchNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peliculaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeliculaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(0)).save(pelicula);
    }

    @Test
    @Transactional
    void deletePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        int databaseSizeBeforeDelete = peliculaRepository.findAll().size();

        // Delete the pelicula
        restPeliculaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pelicula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Pelicula in Elasticsearch
        verify(mockPeliculaSearchRepository, times(1)).deleteById(pelicula.getId());
    }

    @Test
    @Transactional
    void searchPelicula() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        when(mockPeliculaSearchRepository.search("id:" + pelicula.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pelicula), PageRequest.of(0, 1), 1));

        // Search the pelicula
        restPeliculaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + pelicula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].enCines").value(hasItem(DEFAULT_EN_CINES.booleanValue())));
    }
}
