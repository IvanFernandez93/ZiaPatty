package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FraseAudio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FraseAudio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraseAudioRepository extends JpaRepository<FraseAudio, Long> {}
