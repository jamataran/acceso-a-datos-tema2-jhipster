package com.cev.accesoadatos.tema2.jhipster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.accesoadatos.tema2.jhipster.domain.Estreno;

/**
 * Spring Data SQL repository for the Estreno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstrenoRepository extends JpaRepository<Estreno, Long> {}
