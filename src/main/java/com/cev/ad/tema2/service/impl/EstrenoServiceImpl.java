package com.cev.ad.tema2.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cev.ad.tema2.domain.Estreno;
import com.cev.ad.tema2.repository.EstrenoRepository;
import com.cev.ad.tema2.repository.search.EstrenoSearchRepository;
import com.cev.ad.tema2.service.EstrenoService;
import com.cev.ad.tema2.service.dto.EstrenoDTO;
import com.cev.ad.tema2.service.mapper.EstrenoMapper;

/**
 * Service Implementation for managing {@link Estreno}.
 */
@Service
@Transactional
public class EstrenoServiceImpl implements EstrenoService {

    private final Logger log = LoggerFactory.getLogger(EstrenoServiceImpl.class);

    private final EstrenoRepository estrenoRepository;

    private final EstrenoMapper estrenoMapper;

    private final EstrenoSearchRepository estrenoSearchRepository;

    public EstrenoServiceImpl(
        EstrenoRepository estrenoRepository,
        EstrenoMapper estrenoMapper,
        EstrenoSearchRepository estrenoSearchRepository
    ) {
        this.estrenoRepository = estrenoRepository;
        this.estrenoMapper = estrenoMapper;
        this.estrenoSearchRepository = estrenoSearchRepository;
    }

    @Override
    public EstrenoDTO save(EstrenoDTO estrenoDTO) {
        log.debug("Request to save Estreno : {}", estrenoDTO);
        Estreno estreno = estrenoMapper.toEntity(estrenoDTO);
        estreno = estrenoRepository.save(estreno);
        EstrenoDTO result = estrenoMapper.toDto(estreno);
        estrenoSearchRepository.save(estreno);
        return result;
    }

    @Override
    public Optional<EstrenoDTO> partialUpdate(EstrenoDTO estrenoDTO) {
        log.debug("Request to partially update Estreno : {}", estrenoDTO);

        return estrenoRepository
            .findById(estrenoDTO.getId())
            .map(existingEstreno -> {
                estrenoMapper.partialUpdate(existingEstreno, estrenoDTO);

                return existingEstreno;
            })
            .map(estrenoRepository::save)
            .map(savedEstreno -> {
                estrenoSearchRepository.save(savedEstreno);

                return savedEstreno;
            })
            .map(estrenoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstrenoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estrenos");
        return estrenoRepository.findAll(pageable).map(estrenoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstrenoDTO> findOne(Long id) {
        log.debug("Request to get Estreno : {}", id);
        return estrenoRepository.findById(id).map(estrenoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estreno : {}", id);
        estrenoRepository.deleteById(id);
        estrenoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstrenoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Estrenos for query {}", query);
        return estrenoSearchRepository.search(query, pageable).map(estrenoMapper::toDto);
    }
}
