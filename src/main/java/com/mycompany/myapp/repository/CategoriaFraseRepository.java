package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CategoriaFrase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoriaFrase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaFraseRepository extends JpaRepository<CategoriaFrase, Long> {}
