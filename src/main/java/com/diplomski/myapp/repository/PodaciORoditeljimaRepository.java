package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.PodaciORoditeljima;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PodaciORoditeljima entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PodaciORoditeljimaRepository extends JpaRepository<PodaciORoditeljima, Long> {}
