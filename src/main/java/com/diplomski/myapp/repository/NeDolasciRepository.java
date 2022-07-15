package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.NeDolasci;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NeDolasci entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NeDolasciRepository extends JpaRepository<NeDolasci, Long> {}
