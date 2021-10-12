package com.cev.accesoadatos.tema2.jhipster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.accesoadatos.tema2.jhipster.domain.Categoria;

/**
 * Spring Data SQL repository for the Categoria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
