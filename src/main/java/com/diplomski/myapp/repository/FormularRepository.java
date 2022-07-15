package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Formular;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Formular entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormularRepository extends JpaRepository<Formular, Long> {}
