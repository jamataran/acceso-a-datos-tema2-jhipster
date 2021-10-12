package com.cev.ad.tema2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cev.ad.tema2.domain.Estreno;

/**
 * Spring Data SQL repository for the Estreno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstrenoRepository extends JpaRepository<Estreno, Long>, JpaSpecificationExecutor<Estreno> {}
