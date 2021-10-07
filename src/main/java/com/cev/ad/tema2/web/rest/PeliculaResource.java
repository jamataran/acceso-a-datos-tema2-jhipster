package com.cev.ad.tema2.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.cev.ad.tema2.repository.PeliculaRepository;
import com.cev.ad.tema2.service.PeliculaQueryService;
import com.cev.ad.tema2.service.PeliculaService;
import com.cev.ad.tema2.service.criteria.PeliculaCriteria;
import com.cev.ad.tema2.service.dto.PeliculaDTO;
import com.cev.ad.tema2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.cev.ad.tema2.domain.Pelicula}.
 */
@RestController
@RequestMapping("/api")
public class PeliculaResource {

    private final Logger log = LoggerFactory.getLogger(PeliculaResource.class);

    private static final String ENTITY_NAME = "pelicula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeliculaService peliculaService;

    private final PeliculaRepository peliculaRepository;

    private final PeliculaQueryService peliculaQueryService;

    public PeliculaResource(
        PeliculaService peliculaService,
        PeliculaRepository peliculaRepository,
        PeliculaQueryService peliculaQueryService
    ) {
        this.peliculaService = peliculaService;
        this.peliculaRepository = peliculaRepository;
        this.peliculaQueryService = peliculaQueryService;
    }

    /**
     * {@code POST  /peliculas} : Create a new pelicula.
     *
     * @param peliculaDTO the peliculaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peliculaDTO, or with status {@code 400 (Bad Request)} if the pelicula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/peliculas")
    public ResponseEntity<PeliculaDTO> createPelicula(@Valid @RequestBody PeliculaDTO peliculaDTO) throws URISyntaxException {
        log.debug("REST request to save Pelicula : {}", peliculaDTO);
        if (peliculaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pelicula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(peliculaDTO.getEstreno())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        PeliculaDTO result = peliculaService.save(peliculaDTO);
        return ResponseEntity
            .created(new URI("/api/peliculas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /peliculas/:id} : Updates an existing pelicula.
     *
     * @param id the id of the peliculaDTO to save.
     * @param peliculaDTO the peliculaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peliculaDTO,
     * or with status {@code 400 (Bad Request)} if the peliculaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peliculaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/peliculas/{id}")
    public ResponseEntity<PeliculaDTO> updatePelicula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeliculaDTO peliculaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pelicula : {}, {}", id, peliculaDTO);
        if (peliculaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peliculaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peliculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PeliculaDTO result = peliculaService.save(peliculaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, peliculaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /peliculas/:id} : Partial updates given fields of an existing pelicula, field will ignore if it is null
     *
     * @param id the id of the peliculaDTO to save.
     * @param peliculaDTO the peliculaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peliculaDTO,
     * or with status {@code 400 (Bad Request)} if the peliculaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the peliculaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the peliculaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/peliculas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeliculaDTO> partialUpdatePelicula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeliculaDTO peliculaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pelicula partially : {}, {}", id, peliculaDTO);
        if (peliculaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peliculaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peliculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeliculaDTO> result = peliculaService.partialUpdate(peliculaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, peliculaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /peliculas} : get all the peliculas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peliculas in body.
     */
    @GetMapping("/peliculas")
    public ResponseEntity<List<PeliculaDTO>> getAllPeliculas(PeliculaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Peliculas by criteria: {}", criteria);
        Page<PeliculaDTO> page = peliculaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /peliculas/count} : count all the peliculas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/peliculas/count")
    public ResponseEntity<Long> countPeliculas(PeliculaCriteria criteria) {
        log.debug("REST request to count Peliculas by criteria: {}", criteria);
        return ResponseEntity.ok().body(peliculaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /peliculas/:id} : get the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peliculaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/peliculas/{id}")
    public ResponseEntity<PeliculaDTO> getPelicula(@PathVariable Long id) {
        log.debug("REST request to get Pelicula : {}", id);
        Optional<PeliculaDTO> peliculaDTO = peliculaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peliculaDTO);
    }

    /**
     * {@code DELETE  /peliculas/:id} : delete the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/peliculas/{id}")
    public ResponseEntity<Void> deletePelicula(@PathVariable Long id) {
        log.debug("REST request to delete Pelicula : {}", id);
        peliculaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/peliculas?query=:query} : search for the pelicula corresponding
     * to the query.
     *
     * @param query the query of the pelicula search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/peliculas")
    public ResponseEntity<List<PeliculaDTO>> searchPeliculas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Peliculas for query {}", query);
        Page<PeliculaDTO> page = peliculaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
