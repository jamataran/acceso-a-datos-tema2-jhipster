package com.cev.ad.tema2.service;

import com.cev.ad.tema2.service.dto.EstrenoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cev.ad.tema2.domain.Estreno}.
 */
public interface EstrenoService {
    /**
     * Save a estreno.
     *
     * @param estrenoDTO the entity to save.
     * @return the persisted entity.
     */
    EstrenoDTO save(EstrenoDTO estrenoDTO);

    /**
     * Partially updates a estreno.
     *
     * @param estrenoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EstrenoDTO> partialUpdate(EstrenoDTO estrenoDTO);

    /**
     * Get all the estrenos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstrenoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" estreno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EstrenoDTO> findOne(Long id);

    /**
     * Delete the "id" estreno.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the estreno corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstrenoDTO> search(String query, Pageable pageable);
}
