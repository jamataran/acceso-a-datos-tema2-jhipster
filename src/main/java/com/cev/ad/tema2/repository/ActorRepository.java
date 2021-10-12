package com.cev.ad.tema2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cev.ad.tema2.domain.Actor;

/**
 * Spring Data SQL repository for the Actor entity.
 */
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>, JpaSpecificationExecutor<Actor> {
    @Query(
        value = "select distinct actor from Actor actor left join fetch actor.peliculas",
        countQuery = "select count(distinct actor) from Actor actor"
    )
    Page<Actor> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct actor from Actor actor left join fetch actor.peliculas")
    List<Actor> findAllWithEagerRelationships();

    @Query("select actor from Actor actor left join fetch actor.peliculas where actor.id =:id")
    Optional<Actor> findOneWithEagerRelationships(@Param("id") Long id);
}
