package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Frase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Frase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraseRepository extends JpaRepository<Frase, Long> {}
