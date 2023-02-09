package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lingua;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Lingua entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LinguaRepository extends JpaRepository<Lingua, Long> {}
