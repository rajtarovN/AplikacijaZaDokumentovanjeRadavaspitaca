package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Prica;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricaRepository extends JpaRepository<Prica, Long> {
    @Query("SELECT p from Prica p where  p.dnevnik.id = ?1 ")
    List<Prica> findCurrentPricas(Long id);
}
