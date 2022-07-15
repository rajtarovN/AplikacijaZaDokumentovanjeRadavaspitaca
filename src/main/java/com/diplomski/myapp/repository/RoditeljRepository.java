package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Roditelj;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Roditelj entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoditeljRepository extends JpaRepository<Roditelj, Long> {}
