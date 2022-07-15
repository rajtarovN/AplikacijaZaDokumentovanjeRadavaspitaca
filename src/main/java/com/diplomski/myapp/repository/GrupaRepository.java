package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Grupa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Grupa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrupaRepository extends JpaRepository<Grupa, Long> {}
