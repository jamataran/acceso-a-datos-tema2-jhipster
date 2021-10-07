package com.cev.ad.tema2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cev.ad.tema2.IntegrationTest;
import com.cev.ad.tema2.domain.Pelicula;
import com.cev.ad.tema2.domain.Review;
import com.cev.ad.tema2.repository.ReviewRepository;
import com.cev.ad.tema2.repository.search.ReviewSearchRepository;
import com.cev.ad.tema2.service.criteria.ReviewCriteria;
import com.cev.ad.tema2.service.dto.ReviewDTO;
import com.cev.ad.tema2.service.mapper.ReviewMapper;
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
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewResourceIT {

    private static final Integer DEFAULT_PUNTUACION = 0;
    private static final Integer UPDATED_PUNTUACION = 1;
    private static final Integer SMALLER_PUNTUACION = 0 - 1;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/reviews";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * This repository is mocked in the com.cev.ad.tema2.repository.search test package.
     *
     * @see com.cev.ad.tema2.repository.search.ReviewSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReviewSearchRepository mockReviewSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMockMvc;

    private Review review;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity(EntityManager em) {
        Review review = new Review().puntuacion(DEFAULT_PUNTUACION).descripcion(DEFAULT_DESCRIPCION);
        return review;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity(EntityManager em) {
        Review review = new Review().puntuacion(UPDATED_PUNTUACION).descripcion(UPDATED_DESCRIPCION);
        return review;
    }

    @BeforeEach
    public void initTest() {
        review = createEntity(em);
    }

    @Test
    @Transactional
    void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();
        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getPuntuacion()).isEqualTo(DEFAULT_PUNTUACION);
        assertThat(testReview.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).save(testReview);
    }

    @Test
    @Transactional
    void createReviewWithExistingId() throws Exception {
        // Create the Review with an existing ID
        review.setId(1L);
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void checkPuntuacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setPuntuacion(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setDescripcion(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].puntuacion").value(hasItem(DEFAULT_PUNTUACION)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.puntuacion").value(DEFAULT_PUNTUACION))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getReviewsByIdFiltering() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        Long id = review.getId();

        defaultReviewShouldBeFound("id.equals=" + id);
        defaultReviewShouldNotBeFound("id.notEquals=" + id);

        defaultReviewShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReviewShouldNotBeFound("id.greaterThan=" + id);

        defaultReviewShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReviewShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion equals to DEFAULT_PUNTUACION
        defaultReviewShouldBeFound("puntuacion.equals=" + DEFAULT_PUNTUACION);

        // Get all the reviewList where puntuacion equals to UPDATED_PUNTUACION
        defaultReviewShouldNotBeFound("puntuacion.equals=" + UPDATED_PUNTUACION);
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion not equals to DEFAULT_PUNTUACION
        defaultReviewShouldNotBeFound("puntuacion.notEquals=" + DEFAULT_PUNTUACION);

        // Get all the reviewList where puntuacion not equals to UPDATED_PUNTUACION
        defaultReviewShouldBeFound("puntuacion.notEquals=" + UPDATED_PUNTUACION);
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion in DEFAULT_PUNTUACION or UPDATED_PUNTUACION
        defaultReviewShouldBeFound("puntuacion.in=" + DEFAULT_PUNTUACION + "," + UPDATED_PUNTUACION);

        // Get all the reviewList where puntuacion equals to UPDATED_PUNTUACION
        defaultReviewShouldNotBeFound("puntuacion.in=" + UPDATED_PUNTUACION);
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion is not null
        defaultReviewShouldBeFound("puntuacion.specified=true");

        // Get all the reviewList where puntuacion is null
        defaultReviewShouldNotBeFound("puntuacion.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion is greater than or equal to DEFAULT_PUNTUACION
        defaultReviewShouldBeFound("puntuacion.greaterThanOrEqual=" + DEFAULT_PUNTUACION);

        // Get all the reviewList where puntuacion is greater than or equal to (DEFAULT_PUNTUACION + 1)
        defaultReviewShouldNotBeFound("puntuacion.greaterThanOrEqual=" + (DEFAULT_PUNTUACION + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion is less than or equal to DEFAULT_PUNTUACION
        defaultReviewShouldBeFound("puntuacion.lessThanOrEqual=" + DEFAULT_PUNTUACION);

        // Get all the reviewList where puntuacion is less than or equal to SMALLER_PUNTUACION
        defaultReviewShouldNotBeFound("puntuacion.lessThanOrEqual=" + SMALLER_PUNTUACION);
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion is less than DEFAULT_PUNTUACION
        defaultReviewShouldNotBeFound("puntuacion.lessThan=" + DEFAULT_PUNTUACION);

        // Get all the reviewList where puntuacion is less than (DEFAULT_PUNTUACION + 1)
        defaultReviewShouldBeFound("puntuacion.lessThan=" + (DEFAULT_PUNTUACION + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByPuntuacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where puntuacion is greater than DEFAULT_PUNTUACION
        defaultReviewShouldNotBeFound("puntuacion.greaterThan=" + DEFAULT_PUNTUACION);

        // Get all the reviewList where puntuacion is greater than SMALLER_PUNTUACION
        defaultReviewShouldBeFound("puntuacion.greaterThan=" + SMALLER_PUNTUACION);
    }

    @Test
    @Transactional
    void getAllReviewsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where descripcion equals to DEFAULT_DESCRIPCION
        defaultReviewShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the reviewList where descripcion equals to UPDATED_DESCRIPCION
        defaultReviewShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllReviewsByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultReviewShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the reviewList where descripcion not equals to UPDATED_DESCRIPCION
        defaultReviewShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllReviewsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultReviewShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the reviewList where descripcion equals to UPDATED_DESCRIPCION
        defaultReviewShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllReviewsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where descripcion is not null
        defaultReviewShouldBeFound("descripcion.specified=true");

        // Get all the reviewList where descripcion is null
        defaultReviewShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where descripcion contains DEFAULT_DESCRIPCION
        defaultReviewShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the reviewList where descripcion contains UPDATED_DESCRIPCION
        defaultReviewShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllReviewsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultReviewShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the reviewList where descripcion does not contain UPDATED_DESCRIPCION
        defaultReviewShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllReviewsByPeliculaIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        Pelicula pelicula;
        if (TestUtil.findAll(em, Pelicula.class).isEmpty()) {
            pelicula = PeliculaResourceIT.createEntity(em);
            em.persist(pelicula);
            em.flush();
        } else {
            pelicula = TestUtil.findAll(em, Pelicula.class).get(0);
        }
        em.persist(pelicula);
        em.flush();
        review.setPelicula(pelicula);
        reviewRepository.saveAndFlush(review);
        Long peliculaId = pelicula.getId();

        // Get all the reviewList where pelicula equals to peliculaId
        defaultReviewShouldBeFound("peliculaId.equals=" + peliculaId);

        // Get all the reviewList where pelicula equals to (peliculaId + 1)
        defaultReviewShouldNotBeFound("peliculaId.equals=" + (peliculaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewShouldBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].puntuacion").value(hasItem(DEFAULT_PUNTUACION)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewShouldNotBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).get();
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview);
        updatedReview.puntuacion(UPDATED_PUNTUACION).descripcion(UPDATED_DESCRIPCION);
        ReviewDTO reviewDTO = reviewMapper.toDto(updatedReview);

        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getPuntuacion()).isEqualTo(UPDATED_PUNTUACION);
        assertThat(testReview.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository).save(testReview);
    }

    @Test
    @Transactional
    void putNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(count.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void putWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(count.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(count.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void partialUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getPuntuacion()).isEqualTo(DEFAULT_PUNTUACION);
        assertThat(testReview.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview.puntuacion(UPDATED_PUNTUACION).descripcion(UPDATED_DESCRIPCION);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getPuntuacion()).isEqualTo(UPDATED_PUNTUACION);
        assertThat(testReview.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(count.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(count.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(count.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(0)).save(review);
    }

    @Test
    @Transactional
    void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Delete the review
        restReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, review.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Review in Elasticsearch
        verify(mockReviewSearchRepository, times(1)).deleteById(review.getId());
    }

    @Test
    @Transactional
    void searchReview() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        when(mockReviewSearchRepository.search("id:" + review.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(review), PageRequest.of(0, 1), 1));

        // Search the review
        restReviewMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].puntuacion").value(hasItem(DEFAULT_PUNTUACION)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
}
