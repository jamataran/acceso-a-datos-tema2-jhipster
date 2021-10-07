package com.cev.ad.tema2.service;

import com.cev.ad.tema2.domain.*; // for static metamodels
import com.cev.ad.tema2.domain.Estreno;
import com.cev.ad.tema2.repository.EstrenoRepository;
import com.cev.ad.tema2.repository.search.EstrenoSearchRepository;
import com.cev.ad.tema2.service.criteria.EstrenoCriteria;
import com.cev.ad.tema2.service.dto.EstrenoDTO;
import com.cev.ad.tema2.service.mapper.EstrenoMapper;
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
 * Service for executing complex queries for {@link Estreno} entities in the database.
 * The main input is a {@link EstrenoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EstrenoDTO} or a {@link Page} of {@link EstrenoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstrenoQueryService extends QueryService<Estreno> {

    private final Logger log = LoggerFactory.getLogger(EstrenoQueryService.class);

    private final EstrenoRepository estrenoRepository;

    private final EstrenoMapper estrenoMapper;

    private final EstrenoSearchRepository estrenoSearchRepository;

    public EstrenoQueryService(
        EstrenoRepository estrenoRepository,
        EstrenoMapper estrenoMapper,
        EstrenoSearchRepository estrenoSearchRepository
    ) {
        this.estrenoRepository = estrenoRepository;
        this.estrenoMapper = estrenoMapper;
        this.estrenoSearchRepository = estrenoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EstrenoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EstrenoDTO> findByCriteria(EstrenoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Estreno> specification = createSpecification(criteria);
        return estrenoMapper.toDto(estrenoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EstrenoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstrenoDTO> findByCriteria(EstrenoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Estreno> specification = createSpecification(criteria);
        return estrenoRepository.findAll(specification, page).map(estrenoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstrenoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Estreno> specification = createSpecification(criteria);
        return estrenoRepository.count(specification);
    }

    /**
     * Function to convert {@link EstrenoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Estreno> createSpecification(EstrenoCriteria criteria) {
        Specification<Estreno> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Estreno_.id));
            }
            if (criteria.getEstreno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstreno(), Estreno_.estreno));
            }
            if (criteria.getFechaEstreno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaEstreno(), Estreno_.fechaEstreno));
            }
        }
        return specification;
    }
}
