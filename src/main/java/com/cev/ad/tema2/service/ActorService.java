package com.cev.ad.tema2.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cev.ad.tema2.service.dto.ActorDTO;

/**
 * Service Interface for managing {@link com.cev.ad.tema2.domain.Actor}.
 */
public interface ActorService {
    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save.
     * @return the persisted entity.
     */
    ActorDTO save(ActorDTO actorDTO);

    /**
     * Partially updates a actor.
     *
     * @param actorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActorDTO> partialUpdate(ActorDTO actorDTO);

    /**
     * Get all the actors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActorDTO> findAll(Pageable pageable);

    /**
     * Get all the actors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" actor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActorDTO> findOne(Long id);

    /**
     * Delete the "id" actor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the actor corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActorDTO> search(String query, Pageable pageable);
}
