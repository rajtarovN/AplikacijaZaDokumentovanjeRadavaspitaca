package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Pedagog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pedagog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PedagogRepository extends JpaRepository<Pedagog, Long> {}
