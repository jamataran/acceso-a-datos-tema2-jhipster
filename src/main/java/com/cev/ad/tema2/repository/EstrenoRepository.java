package com.cev.ad.tema2.repository;

import com.cev.ad.tema2.domain.Estreno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Estreno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstrenoRepository extends JpaRepository<Estreno, Long>, JpaSpecificationExecutor<Estreno> {}
