package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.KonacnaPrica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the KonacnaPrica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KonacnaPricaRepository extends JpaRepository<KonacnaPrica, Long> {}
