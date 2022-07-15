package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Prica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricaRepository extends JpaRepository<Prica, Long> {}
