package com.cev.ad.tema2.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.cev.ad.tema2.domain.Pelicula;
import com.cev.ad.tema2.repository.EstrenoRepository;
import com.cev.ad.tema2.repository.PeliculaRepository;
import com.cev.ad.tema2.repository.search.PeliculaSearchRepository;
import com.cev.ad.tema2.service.PeliculaService;
import com.cev.ad.tema2.service.dto.PeliculaDTO;
import com.cev.ad.tema2.service.mapper.PeliculaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pelicula}.
 */
@Service
@Transactional
public class PeliculaServiceImpl implements PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaServiceImpl.class);

    private final PeliculaRepository peliculaRepository;

    private final PeliculaMapper peliculaMapper;

    private final PeliculaSearchRepository peliculaSearchRepository;

    private final EstrenoRepository estrenoRepository;

    public PeliculaServiceImpl(
        PeliculaRepository peliculaRepository,
        PeliculaMapper peliculaMapper,
        PeliculaSearchRepository peliculaSearchRepository,
        EstrenoRepository estrenoRepository
    ) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
        this.peliculaSearchRepository = peliculaSearchRepository;
        this.estrenoRepository = estrenoRepository;
    }

    @Override
    public PeliculaDTO save(PeliculaDTO peliculaDTO) {
        log.debug("Request to save Pelicula : {}", peliculaDTO);
        Pelicula pelicula = peliculaMapper.toEntity(peliculaDTO);
        Long estrenoId = peliculaDTO.getEstreno().getId();
        estrenoRepository.findById(estrenoId).ifPresent(pelicula::estreno);
        pelicula = peliculaRepository.save(pelicula);
        PeliculaDTO result = peliculaMapper.toDto(pelicula);
        peliculaSearchRepository.save(pelicula);
        return result;
    }

    @Override
    public Optional<PeliculaDTO> partialUpdate(PeliculaDTO peliculaDTO) {
        log.debug("Request to partially update Pelicula : {}", peliculaDTO);

        return peliculaRepository
            .findById(peliculaDTO.getId())
            .map(existingPelicula -> {
                peliculaMapper.partialUpdate(existingPelicula, peliculaDTO);

                return existingPelicula;
            })
            .map(peliculaRepository::save)
            .map(savedPelicula -> {
                peliculaSearchRepository.save(savedPelicula);

                return savedPelicula;
            })
            .map(peliculaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PeliculaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Peliculas");
        return peliculaRepository.findAll(pageable).map(peliculaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeliculaDTO> findOne(Long id) {
        log.debug("Request to get Pelicula : {}", id);
        return peliculaRepository.findById(id).map(peliculaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pelicula : {}", id);
        peliculaRepository.deleteById(id);
        peliculaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PeliculaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Peliculas for query {}", query);
        return peliculaSearchRepository.search(query, pageable).map(peliculaMapper::toDto);
    }
}
