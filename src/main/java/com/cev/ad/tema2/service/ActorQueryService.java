package com.cev.ad.tema2.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cev.ad.tema2.domain.Actor;
import com.cev.ad.tema2.repository.ActorRepository;
import com.cev.ad.tema2.repository.search.ActorSearchRepository;
import com.cev.ad.tema2.service.criteria.ActorCriteria;
import com.cev.ad.tema2.service.dto.ActorDTO;
import com.cev.ad.tema2.service.mapper.ActorMapper;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Actor} entities in the database.
 * The main input is a {@link ActorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActorDTO} or a {@link Page} of {@link ActorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActorQueryService extends QueryService<Actor> {

    private final Logger log = LoggerFactory.getLogger(ActorQueryService.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    private final ActorSearchRepository actorSearchRepository;

    public ActorQueryService(ActorRepository actorRepository, ActorMapper actorMapper, ActorSearchRepository actorSearchRepository) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.actorSearchRepository = actorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ActorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActorDTO> findByCriteria(ActorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorMapper.toDto(actorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActorDTO> findByCriteria(ActorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.findAll(specification, page).map(actorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.count(specification);
    }

    /**
     * Function to convert {@link ActorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Actor> createSpecification(ActorCriteria criteria) {
        Specification<Actor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Actor_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Actor_.nombre));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBio(), Actor_.bio));
            }
            if (criteria.getPeliculaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPeliculaId(), root -> root.join(Actor_.peliculas, JoinType.LEFT).get(Pelicula_.id))
                    );
            }
        }
        return specification;
    }
}
