package com.cev.accesoadatos.tema2.jhipster.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cev.accesoadatos.tema2.jhipster.domain.Pelicula;

/**
 * Spring Data SQL repository for the Pelicula entity.
 */
@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long>, JpaSpecificationExecutor<Pelicula> {
    @Query(
        value = "select distinct pelicula from Pelicula pelicula left join fetch pelicula.actors",
        countQuery = "select count(distinct pelicula) from Pelicula pelicula"
    )
    Page<Pelicula> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct pelicula from Pelicula pelicula left join fetch pelicula.actors")
    List<Pelicula> findAllWithEagerRelationships();

    @Query("select pelicula from Pelicula pelicula left join fetch pelicula.actors where pelicula.id =:id")
    Optional<Pelicula> findOneWithEagerRelationships(@Param("id") Long id);
}
