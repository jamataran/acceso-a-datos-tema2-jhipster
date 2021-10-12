package com.cev.ad.tema2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cev.ad.tema2.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
