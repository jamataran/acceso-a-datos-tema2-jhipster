package com.cev.ad.tema2.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cev.ad.tema2.domain.Pelicula;
import com.cev.ad.tema2.repository.EstrenoRepository;
import com.cev.ad.tema2.repository.PeliculaRepository;
import com.cev.ad.tema2.repository.search.PeliculaSearchRepository;
import com.cev.ad.tema2.service.PeliculaService;

/**
 * Service Implementation for managing {@link Pelicula}.
 */
@Service
@Transactional
public class PeliculaServiceImpl implements PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaServiceImpl.class);

    private final PeliculaRepository peliculaRepository;

    private final PeliculaSearchRepository peliculaSearchRepository;

    private final EstrenoRepository estrenoRepository;

    public PeliculaServiceImpl(
        PeliculaRepository peliculaRepository,
        PeliculaSearchRepository peliculaSearchRepository,
        EstrenoRepository estrenoRepository
    ) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaSearchRepository = peliculaSearchRepository;
        this.estrenoRepository = estrenoRepository;
    }

    @Override
    public Pelicula save(Pelicula pelicula) {
        log.debug("Request to save Pelicula : {}", pelicula);
        Long estrenoId = pelicula.getEstreno().getId();
        estrenoRepository.findById(estrenoId).ifPresent(pelicula::estreno);
        Pelicula result = peliculaRepository.save(pelicula);
        peliculaSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Pelicula> partialUpdate(Pelicula pelicula) {
        log.debug("Request to partially update Pelicula : {}", pelicula);

        return peliculaRepository
            .findById(pelicula.getId())
            .map(existingPelicula -> {
                if (pelicula.getTitulo() != null) {
                    existingPelicula.setTitulo(pelicula.getTitulo());
                }
                if (pelicula.getDescripcion() != null) {
                    existingPelicula.setDescripcion(pelicula.getDescripcion());
                }
                if (pelicula.getEnCines() != null) {
                    existingPelicula.setEnCines(pelicula.getEnCines());
                }

                return existingPelicula;
            })
            .map(peliculaRepository::save)
            .map(savedPelicula -> {
                peliculaSearchRepository.save(savedPelicula);

                return savedPelicula;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pelicula> findAll(Pageable pageable) {
        log.debug("Request to get all Peliculas");
        return peliculaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pelicula> findOne(Long id) {
        log.debug("Request to get Pelicula : {}", id);
        return peliculaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pelicula : {}", id);
        peliculaRepository.deleteById(id);
        peliculaSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pelicula> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Peliculas for query {}", query);
        return peliculaSearchRepository.search(query, pageable);
    }
}
