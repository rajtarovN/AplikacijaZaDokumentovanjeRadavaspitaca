package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Dete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeteRepository extends JpaRepository<Dete, Long> {}
