package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.PlanPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlanPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanPriceRepository extends JpaRepository<PlanPrice, Long> {}
