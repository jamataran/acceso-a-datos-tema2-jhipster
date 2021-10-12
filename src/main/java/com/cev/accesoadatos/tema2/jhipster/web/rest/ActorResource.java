package com.cev.accesoadatos.tema2.jhipster.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cev.accesoadatos.tema2.jhipster.domain.Actor;
import com.cev.accesoadatos.tema2.jhipster.repository.ActorRepository;
import com.cev.accesoadatos.tema2.jhipster.service.ActorQueryService;
import com.cev.accesoadatos.tema2.jhipster.service.ActorService;
import com.cev.accesoadatos.tema2.jhipster.service.criteria.ActorCriteria;
import com.cev.accesoadatos.tema2.jhipster.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cev.accesoadatos.tema2.jhipster.domain.Actor}.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "actor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActorService actorService;

    private final ActorRepository actorRepository;

    private final ActorQueryService actorQueryService;

    public ActorResource(ActorService actorService, ActorRepository actorRepository, ActorQueryService actorQueryService) {
        this.actorService = actorService;
        this.actorRepository = actorRepository;
        this.actorQueryService = actorQueryService;
    }

    /**
     * {@code POST  /actors} : Create a new actor.
     *
     * @param actor the actor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actor, or with status {@code 400 (Bad Request)} if the actor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actors")
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actor);
        if (actor.getId() != null) {
            throw new BadRequestAlertException("A new actor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Actor result = actorService.save(actor);
        return ResponseEntity
            .created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actors/:id} : Updates an existing actor.
     *
     * @param id the id of the actor to save.
     * @param actor the actor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actor,
     * or with status {@code 400 (Bad Request)} if the actor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actors/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Actor actor)
        throws URISyntaxException {
        log.debug("REST request to update Actor : {}, {}", id, actor);
        if (actor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Actor result = actorService.save(actor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, actor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /actors/:id} : Partial updates given fields of an existing actor, field will ignore if it is null
     *
     * @param id the id of the actor to save.
     * @param actor the actor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actor,
     * or with status {@code 400 (Bad Request)} if the actor is not valid,
     * or with status {@code 404 (Not Found)} if the actor is not found,
     * or with status {@code 500 (Internal Server Error)} if the actor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/actors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Actor> partialUpdateActor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Actor actor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Actor partially : {}, {}", id, actor);
        if (actor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Actor> result = actorService.partialUpdate(actor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, actor.getId().toString())
        );
    }

    /**
     * {@code GET  /actors} : get all the actors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actors in body.
     */
    @GetMapping("/actors")
    public ResponseEntity<List<Actor>> getAllActors(ActorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Actors by criteria: {}", criteria);
        Page<Actor> page = actorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /actors/count} : count all the actors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/actors/count")
    public ResponseEntity<Long> countActors(ActorCriteria criteria) {
        log.debug("REST request to count Actors by criteria: {}", criteria);
        return ResponseEntity.ok().body(actorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /actors/:id} : get the "id" actor.
     *
     * @param id the id of the actor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actors/{id}")
    public ResponseEntity<Actor> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        Optional<Actor> actor = actorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actor);
    }

    /**
     * {@code DELETE  /actors/:id} : delete the "id" actor.
     *
     * @param id the id of the actor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actors/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/actors?query=:query} : search for the actor corresponding
     * to the query.
     *
     * @param query the query of the actor search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/actors")
    public ResponseEntity<List<Actor>> searchActors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Actors for query {}", query);
        Page<Actor> page = actorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
