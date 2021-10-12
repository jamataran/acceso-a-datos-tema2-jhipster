package com.cev.accesoadatos.tema2.jhipster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cev.accesoadatos.tema2.jhipster.domain.Actor;

/**
 * Spring Data SQL repository for the Actor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>, JpaSpecificationExecutor<Actor> {}
