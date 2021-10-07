package com.cev.ad.tema2.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cev.ad.tema2.domain.Pelicula;

/**
 * Service Interface for managing {@link Pelicula}.
 */
public interface PeliculaService {
    /**
     * Save a pelicula.
     *
     * @param pelicula the entity to save.
     * @return the persisted entity.
     */
    Pelicula save(Pelicula pelicula);

    /**
     * Partially updates a pelicula.
     *
     * @param pelicula the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pelicula> partialUpdate(Pelicula pelicula);

    /**
     * Get all the peliculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pelicula> findAll(Pageable pageable);

    /**
     * Get the "id" pelicula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pelicula> findOne(Long id);

    /**
     * Delete the "id" pelicula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the pelicula corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pelicula> search(String query, Pageable pageable);
}
