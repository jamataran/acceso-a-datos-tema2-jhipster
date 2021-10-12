package com.cev.ad.tema2.service;

import com.cev.ad.tema2.domain.*; // for static metamodels
import com.cev.ad.tema2.domain.Pelicula;
import com.cev.ad.tema2.repository.PeliculaRepository;
import com.cev.ad.tema2.repository.search.PeliculaSearchRepository;
import com.cev.ad.tema2.service.criteria.PeliculaCriteria;
import com.cev.ad.tema2.service.dto.PeliculaDTO;
import com.cev.ad.tema2.service.mapper.PeliculaMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pelicula} entities in the database.
 * The main input is a {@link PeliculaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeliculaDTO} or a {@link Page} of {@link PeliculaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeliculaQueryService extends QueryService<Pelicula> {

    private final Logger log = LoggerFactory.getLogger(PeliculaQueryService.class);

    private final PeliculaRepository peliculaRepository;

    private final PeliculaMapper peliculaMapper;

    private final PeliculaSearchRepository peliculaSearchRepository;

    public PeliculaQueryService(
        PeliculaRepository peliculaRepository,
        PeliculaMapper peliculaMapper,
        PeliculaSearchRepository peliculaSearchRepository
    ) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
        this.peliculaSearchRepository = peliculaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PeliculaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeliculaDTO> findByCriteria(PeliculaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pelicula> specification = createSpecification(criteria);
        return peliculaMapper.toDto(peliculaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PeliculaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeliculaDTO> findByCriteria(PeliculaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pelicula> specification = createSpecification(criteria);
        return peliculaRepository.findAll(specification, page).map(peliculaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeliculaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pelicula> specification = createSpecification(criteria);
        return peliculaRepository.count(specification);
    }

    /**
     * Function to convert {@link PeliculaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pelicula> createSpecification(PeliculaCriteria criteria) {
        Specification<Pelicula> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pelicula_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Pelicula_.titulo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Pelicula_.descripcion));
            }
            if (criteria.getEnCines() != null) {
                specification = specification.and(buildSpecification(criteria.getEnCines(), Pelicula_.enCines));
            }
            if (criteria.getEstrenoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEstrenoId(), root -> root.join(Pelicula_.estreno, JoinType.LEFT).get(Estreno_.id))
                    );
            }
            if (criteria.getReviewId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReviewId(), root -> root.join(Pelicula_.reviews, JoinType.LEFT).get(Review_.id))
                    );
            }
        }
        return specification;
    }
}
