package com.cev.ad.tema2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.cev.ad.tema2.IntegrationTest;
import com.cev.ad.tema2.domain.Estreno;
import com.cev.ad.tema2.repository.EstrenoRepository;
import com.cev.ad.tema2.repository.search.EstrenoSearchRepository;
import com.cev.ad.tema2.service.dto.EstrenoDTO;
import com.cev.ad.tema2.service.mapper.EstrenoMapper;

/**
 * Integration tests for the {@link EstrenoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EstrenoResourceIT {

    private static final String DEFAULT_ESTRENO = "AAAAAAAAAA";
    private static final String UPDATED_ESTRENO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ESTRENO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ESTRENO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ESTRENO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/estrenos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/estrenos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstrenoRepository estrenoRepository;

    @Autowired
    private EstrenoMapper estrenoMapper;

    /**
     * This repository is mocked in the com.cev.ad.tema2.repository.search test package.
     *
     * @see com.cev.ad.tema2.repository.search.EstrenoSearchRepositoryMockConfiguration
     */
    @Autowired
    private EstrenoSearchRepository mockEstrenoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstrenoMockMvc;

    private Estreno estreno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estreno createEntity(EntityManager em) {
        Estreno estreno = new Estreno().estreno(DEFAULT_ESTRENO).fechaEstreno(DEFAULT_FECHA_ESTRENO);
        return estreno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estreno createUpdatedEntity(EntityManager em) {
        Estreno estreno = new Estreno().estreno(UPDATED_ESTRENO).fechaEstreno(UPDATED_FECHA_ESTRENO);
        return estreno;
    }

    @BeforeEach
    public void initTest() {
        estreno = createEntity(em);
    }

    @Test
    @Transactional
    void createEstreno() throws Exception {
        int databaseSizeBeforeCreate = estrenoRepository.findAll().size();
        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);
        restEstrenoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estrenoDTO)))
            .andExpect(status().isCreated());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeCreate + 1);
        Estreno testEstreno = estrenoList.get(estrenoList.size() - 1);
        assertThat(testEstreno.getEstreno()).isEqualTo(DEFAULT_ESTRENO);
        assertThat(testEstreno.getFechaEstreno()).isEqualTo(DEFAULT_FECHA_ESTRENO);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(1)).save(testEstreno);
    }

    @Test
    @Transactional
    void createEstrenoWithExistingId() throws Exception {
        // Create the Estreno with an existing ID
        estreno.setId(1L);
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        int databaseSizeBeforeCreate = estrenoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstrenoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estrenoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void getAllEstrenos() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList
        restEstrenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estreno.getId().intValue())))
            .andExpect(jsonPath("$.[*].estreno").value(hasItem(DEFAULT_ESTRENO)))
            .andExpect(jsonPath("$.[*].fechaEstreno").value(hasItem(DEFAULT_FECHA_ESTRENO.toString())));
    }

    @Test
    @Transactional
    void getEstreno() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get the estreno
        restEstrenoMockMvc
            .perform(get(ENTITY_API_URL_ID, estreno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estreno.getId().intValue()))
            .andExpect(jsonPath("$.estreno").value(DEFAULT_ESTRENO))
            .andExpect(jsonPath("$.fechaEstreno").value(DEFAULT_FECHA_ESTRENO.toString()));
    }

    @Test
    @Transactional
    void getEstrenosByIdFiltering() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        Long id = estreno.getId();

        defaultEstrenoShouldBeFound("id.equals=" + id);
        defaultEstrenoShouldNotBeFound("id.notEquals=" + id);

        defaultEstrenoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEstrenoShouldNotBeFound("id.greaterThan=" + id);

        defaultEstrenoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEstrenoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstrenosByEstrenoIsEqualToSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where estreno equals to DEFAULT_ESTRENO
        defaultEstrenoShouldBeFound("estreno.equals=" + DEFAULT_ESTRENO);

        // Get all the estrenoList where estreno equals to UPDATED_ESTRENO
        defaultEstrenoShouldNotBeFound("estreno.equals=" + UPDATED_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByEstrenoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where estreno not equals to DEFAULT_ESTRENO
        defaultEstrenoShouldNotBeFound("estreno.notEquals=" + DEFAULT_ESTRENO);

        // Get all the estrenoList where estreno not equals to UPDATED_ESTRENO
        defaultEstrenoShouldBeFound("estreno.notEquals=" + UPDATED_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByEstrenoIsInShouldWork() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where estreno in DEFAULT_ESTRENO or UPDATED_ESTRENO
        defaultEstrenoShouldBeFound("estreno.in=" + DEFAULT_ESTRENO + "," + UPDATED_ESTRENO);

        // Get all the estrenoList where estreno equals to UPDATED_ESTRENO
        defaultEstrenoShouldNotBeFound("estreno.in=" + UPDATED_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByEstrenoIsNullOrNotNull() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where estreno is not null
        defaultEstrenoShouldBeFound("estreno.specified=true");

        // Get all the estrenoList where estreno is null
        defaultEstrenoShouldNotBeFound("estreno.specified=false");
    }

    @Test
    @Transactional
    void getAllEstrenosByEstrenoContainsSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where estreno contains DEFAULT_ESTRENO
        defaultEstrenoShouldBeFound("estreno.contains=" + DEFAULT_ESTRENO);

        // Get all the estrenoList where estreno contains UPDATED_ESTRENO
        defaultEstrenoShouldNotBeFound("estreno.contains=" + UPDATED_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByEstrenoNotContainsSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where estreno does not contain DEFAULT_ESTRENO
        defaultEstrenoShouldNotBeFound("estreno.doesNotContain=" + DEFAULT_ESTRENO);

        // Get all the estrenoList where estreno does not contain UPDATED_ESTRENO
        defaultEstrenoShouldBeFound("estreno.doesNotContain=" + UPDATED_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsEqualToSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno equals to DEFAULT_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.equals=" + DEFAULT_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno equals to UPDATED_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.equals=" + UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno not equals to DEFAULT_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.notEquals=" + DEFAULT_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno not equals to UPDATED_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.notEquals=" + UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsInShouldWork() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno in DEFAULT_FECHA_ESTRENO or UPDATED_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.in=" + DEFAULT_FECHA_ESTRENO + "," + UPDATED_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno equals to UPDATED_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.in=" + UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsNullOrNotNull() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno is not null
        defaultEstrenoShouldBeFound("fechaEstreno.specified=true");

        // Get all the estrenoList where fechaEstreno is null
        defaultEstrenoShouldNotBeFound("fechaEstreno.specified=false");
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno is greater than or equal to DEFAULT_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.greaterThanOrEqual=" + DEFAULT_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno is greater than or equal to UPDATED_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.greaterThanOrEqual=" + UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno is less than or equal to DEFAULT_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.lessThanOrEqual=" + DEFAULT_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno is less than or equal to SMALLER_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.lessThanOrEqual=" + SMALLER_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsLessThanSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno is less than DEFAULT_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.lessThan=" + DEFAULT_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno is less than UPDATED_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.lessThan=" + UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void getAllEstrenosByFechaEstrenoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        // Get all the estrenoList where fechaEstreno is greater than DEFAULT_FECHA_ESTRENO
        defaultEstrenoShouldNotBeFound("fechaEstreno.greaterThan=" + DEFAULT_FECHA_ESTRENO);

        // Get all the estrenoList where fechaEstreno is greater than SMALLER_FECHA_ESTRENO
        defaultEstrenoShouldBeFound("fechaEstreno.greaterThan=" + SMALLER_FECHA_ESTRENO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstrenoShouldBeFound(String filter) throws Exception {
        restEstrenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estreno.getId().intValue())))
            .andExpect(jsonPath("$.[*].estreno").value(hasItem(DEFAULT_ESTRENO)))
            .andExpect(jsonPath("$.[*].fechaEstreno").value(hasItem(DEFAULT_FECHA_ESTRENO.toString())));

        // Check, that the count call also returns 1
        restEstrenoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstrenoShouldNotBeFound(String filter) throws Exception {
        restEstrenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstrenoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstreno() throws Exception {
        // Get the estreno
        restEstrenoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEstreno() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();

        // Update the estreno
        Estreno updatedEstreno = estrenoRepository.findById(estreno.getId()).get();
        // Disconnect from session so that the updates on updatedEstreno are not directly saved in db
        em.detach(updatedEstreno);
        updatedEstreno.estreno(UPDATED_ESTRENO).fechaEstreno(UPDATED_FECHA_ESTRENO);
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(updatedEstreno);

        restEstrenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estrenoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estrenoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);
        Estreno testEstreno = estrenoList.get(estrenoList.size() - 1);
        assertThat(testEstreno.getEstreno()).isEqualTo(UPDATED_ESTRENO);
        assertThat(testEstreno.getFechaEstreno()).isEqualTo(UPDATED_FECHA_ESTRENO);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository).save(testEstreno);
    }

    @Test
    @Transactional
    void putNonExistingEstreno() throws Exception {
        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();
        estreno.setId(count.incrementAndGet());

        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstrenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estrenoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estrenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstreno() throws Exception {
        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();
        estreno.setId(count.incrementAndGet());

        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstrenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(estrenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstreno() throws Exception {
        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();
        estreno.setId(count.incrementAndGet());

        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstrenoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(estrenoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void partialUpdateEstrenoWithPatch() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();

        // Update the estreno using partial update
        Estreno partialUpdatedEstreno = new Estreno();
        partialUpdatedEstreno.setId(estreno.getId());

        restEstrenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstreno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstreno))
            )
            .andExpect(status().isOk());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);
        Estreno testEstreno = estrenoList.get(estrenoList.size() - 1);
        assertThat(testEstreno.getEstreno()).isEqualTo(DEFAULT_ESTRENO);
        assertThat(testEstreno.getFechaEstreno()).isEqualTo(DEFAULT_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void fullUpdateEstrenoWithPatch() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();

        // Update the estreno using partial update
        Estreno partialUpdatedEstreno = new Estreno();
        partialUpdatedEstreno.setId(estreno.getId());

        partialUpdatedEstreno.estreno(UPDATED_ESTRENO).fechaEstreno(UPDATED_FECHA_ESTRENO);

        restEstrenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstreno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstreno))
            )
            .andExpect(status().isOk());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);
        Estreno testEstreno = estrenoList.get(estrenoList.size() - 1);
        assertThat(testEstreno.getEstreno()).isEqualTo(UPDATED_ESTRENO);
        assertThat(testEstreno.getFechaEstreno()).isEqualTo(UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    void patchNonExistingEstreno() throws Exception {
        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();
        estreno.setId(count.incrementAndGet());

        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstrenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estrenoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estrenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstreno() throws Exception {
        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();
        estreno.setId(count.incrementAndGet());

        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstrenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(estrenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstreno() throws Exception {
        int databaseSizeBeforeUpdate = estrenoRepository.findAll().size();
        estreno.setId(count.incrementAndGet());

        // Create the Estreno
        EstrenoDTO estrenoDTO = estrenoMapper.toDto(estreno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstrenoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(estrenoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estreno in the database
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(0)).save(estreno);
    }

    @Test
    @Transactional
    void deleteEstreno() throws Exception {
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);

        int databaseSizeBeforeDelete = estrenoRepository.findAll().size();

        // Delete the estreno
        restEstrenoMockMvc
            .perform(delete(ENTITY_API_URL_ID, estreno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Estreno> estrenoList = estrenoRepository.findAll();
        assertThat(estrenoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Estreno in Elasticsearch
        verify(mockEstrenoSearchRepository, times(1)).deleteById(estreno.getId());
    }

    @Test
    @Transactional
    void searchEstreno() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        estrenoRepository.saveAndFlush(estreno);
        when(mockEstrenoSearchRepository.search("id:" + estreno.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(estreno), PageRequest.of(0, 1), 1));

        // Search the estreno
        restEstrenoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + estreno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estreno.getId().intValue())))
            .andExpect(jsonPath("$.[*].estreno").value(hasItem(DEFAULT_ESTRENO)))
            .andExpect(jsonPath("$.[*].fechaEstreno").value(hasItem(DEFAULT_FECHA_ESTRENO.toString())));
    }
}
