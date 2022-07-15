package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Objekat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Objekat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjekatRepository extends JpaRepository<Objekat, Long> {}
