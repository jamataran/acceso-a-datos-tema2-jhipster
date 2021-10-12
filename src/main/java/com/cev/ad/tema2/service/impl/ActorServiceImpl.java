package com.cev.ad.tema2.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cev.ad.tema2.domain.Actor;
import com.cev.ad.tema2.repository.ActorRepository;
import com.cev.ad.tema2.repository.search.ActorSearchRepository;
import com.cev.ad.tema2.service.ActorService;
import com.cev.ad.tema2.service.dto.ActorDTO;
import com.cev.ad.tema2.service.mapper.ActorMapper;

/**
 * Service Implementation for managing {@link Actor}.
 */
@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    private final ActorSearchRepository actorSearchRepository;

    public ActorServiceImpl(ActorRepository actorRepository, ActorMapper actorMapper, ActorSearchRepository actorSearchRepository) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.actorSearchRepository = actorSearchRepository;
    }

    @Override
    public ActorDTO save(ActorDTO actorDTO) {
        log.debug("Request to save Actor : {}", actorDTO);
        Actor actor = actorMapper.toEntity(actorDTO);
        actor = actorRepository.save(actor);
        ActorDTO result = actorMapper.toDto(actor);
        actorSearchRepository.save(actor);
        return result;
    }

    @Override
    public Optional<ActorDTO> partialUpdate(ActorDTO actorDTO) {
        log.debug("Request to partially update Actor : {}", actorDTO);

        return actorRepository
            .findById(actorDTO.getId())
            .map(existingActor -> {
                actorMapper.partialUpdate(existingActor, actorDTO);

                return existingActor;
            })
            .map(actorRepository::save)
            .map(savedActor -> {
                actorSearchRepository.save(savedActor);

                return savedActor;
            })
            .map(actorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Actors");
        return actorRepository.findAll(pageable).map(actorMapper::toDto);
    }

    public Page<ActorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return actorRepository.findAllWithEagerRelationships(pageable).map(actorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActorDTO> findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        return actorRepository.findOneWithEagerRelationships(id).map(actorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        actorRepository.deleteById(id);
        actorSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Actors for query {}", query);
        return actorSearchRepository.search(query, pageable).map(actorMapper::toDto);
    }
}
