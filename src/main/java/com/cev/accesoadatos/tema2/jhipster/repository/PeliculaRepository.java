package com.cev.accesoadatos.tema2.jhipster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cev.accesoadatos.tema2.jhipster.domain.Pelicula;

/**
 * Spring Data SQL repository for the Pelicula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long>, JpaSpecificationExecutor<Pelicula> {}
