package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Direktor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Direktor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirektorRepository extends JpaRepository<Direktor, Long> {}
