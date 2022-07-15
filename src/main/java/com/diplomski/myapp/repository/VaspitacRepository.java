package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Vaspitac;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vaspitac entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaspitacRepository extends JpaRepository<Vaspitac, Long> {}
