package com.cev.ad.tema2.service;

import com.cev.ad.tema2.service.dto.PeliculaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cev.ad.tema2.domain.Pelicula}.
 */
public interface PeliculaService {
    /**
     * Save a pelicula.
     *
     * @param peliculaDTO the entity to save.
     * @return the persisted entity.
     */
    PeliculaDTO save(PeliculaDTO peliculaDTO);

    /**
     * Partially updates a pelicula.
     *
     * @param peliculaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PeliculaDTO> partialUpdate(PeliculaDTO peliculaDTO);

    /**
     * Get all the peliculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PeliculaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pelicula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PeliculaDTO> findOne(Long id);

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
    Page<PeliculaDTO> search(String query, Pageable pageable);
}
