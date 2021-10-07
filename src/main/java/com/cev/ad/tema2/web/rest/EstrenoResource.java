package com.cev.ad.tema2.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.cev.ad.tema2.repository.EstrenoRepository;
import com.cev.ad.tema2.service.EstrenoQueryService;
import com.cev.ad.tema2.service.EstrenoService;
import com.cev.ad.tema2.service.criteria.EstrenoCriteria;
import com.cev.ad.tema2.service.dto.EstrenoDTO;
import com.cev.ad.tema2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cev.ad.tema2.domain.Estreno}.
 */
@RestController
@RequestMapping("/api")
public class EstrenoResource {

    private final Logger log = LoggerFactory.getLogger(EstrenoResource.class);

    private static final String ENTITY_NAME = "estreno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstrenoService estrenoService;

    private final EstrenoRepository estrenoRepository;

    private final EstrenoQueryService estrenoQueryService;

    public EstrenoResource(EstrenoService estrenoService, EstrenoRepository estrenoRepository, EstrenoQueryService estrenoQueryService) {
        this.estrenoService = estrenoService;
        this.estrenoRepository = estrenoRepository;
        this.estrenoQueryService = estrenoQueryService;
    }

    /**
     * {@code POST  /estrenos} : Create a new estreno.
     *
     * @param estrenoDTO the estrenoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estrenoDTO, or with status {@code 400 (Bad Request)} if the estreno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estrenos")
    public ResponseEntity<EstrenoDTO> createEstreno(@RequestBody EstrenoDTO estrenoDTO) throws URISyntaxException {
        log.debug("REST request to save Estreno : {}", estrenoDTO);
        if (estrenoDTO.getId() != null) {
            throw new BadRequestAlertException("A new estreno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstrenoDTO result = estrenoService.save(estrenoDTO);
        return ResponseEntity
            .created(new URI("/api/estrenos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /estrenos/:id} : Updates an existing estreno.
     *
     * @param id the id of the estrenoDTO to save.
     * @param estrenoDTO the estrenoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estrenoDTO,
     * or with status {@code 400 (Bad Request)} if the estrenoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estrenoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estrenos/{id}")
    public ResponseEntity<EstrenoDTO> updateEstreno(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstrenoDTO estrenoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Estreno : {}, {}", id, estrenoDTO);
        if (estrenoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estrenoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estrenoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EstrenoDTO result = estrenoService.save(estrenoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estrenoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /estrenos/:id} : Partial updates given fields of an existing estreno, field will ignore if it is null
     *
     * @param id the id of the estrenoDTO to save.
     * @param estrenoDTO the estrenoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estrenoDTO,
     * or with status {@code 400 (Bad Request)} if the estrenoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estrenoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estrenoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/estrenos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstrenoDTO> partialUpdateEstreno(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstrenoDTO estrenoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estreno partially : {}, {}", id, estrenoDTO);
        if (estrenoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estrenoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estrenoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstrenoDTO> result = estrenoService.partialUpdate(estrenoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estrenoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estrenos} : get all the estrenos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estrenos in body.
     */
    @GetMapping("/estrenos")
    public ResponseEntity<List<EstrenoDTO>> getAllEstrenos(EstrenoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Estrenos by criteria: {}", criteria);
        Page<EstrenoDTO> page = estrenoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estrenos/count} : count all the estrenos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/estrenos/count")
    public ResponseEntity<Long> countEstrenos(EstrenoCriteria criteria) {
        log.debug("REST request to count Estrenos by criteria: {}", criteria);
        return ResponseEntity.ok().body(estrenoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estrenos/:id} : get the "id" estreno.
     *
     * @param id the id of the estrenoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estrenoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estrenos/{id}")
    public ResponseEntity<EstrenoDTO> getEstreno(@PathVariable Long id) {
        log.debug("REST request to get Estreno : {}", id);
        Optional<EstrenoDTO> estrenoDTO = estrenoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estrenoDTO);
    }

    /**
     * {@code DELETE  /estrenos/:id} : delete the "id" estreno.
     *
     * @param id the id of the estrenoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/estrenos/{id}")
    public ResponseEntity<Void> deleteEstreno(@PathVariable Long id) {
        log.debug("REST request to delete Estreno : {}", id);
        estrenoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/estrenos?query=:query} : search for the estreno corresponding
     * to the query.
     *
     * @param query the query of the estreno search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/estrenos")
    public ResponseEntity<List<EstrenoDTO>> searchEstrenos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Estrenos for query {}", query);
        Page<EstrenoDTO> page = estrenoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
