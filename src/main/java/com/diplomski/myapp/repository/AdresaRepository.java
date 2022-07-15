package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Adresa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Adresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdresaRepository extends JpaRepository<Adresa, Long> {}
