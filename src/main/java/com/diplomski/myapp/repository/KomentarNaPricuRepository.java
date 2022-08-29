package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.KomentarNaPricu;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the KomentarNaPricu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KomentarNaPricuRepository extends JpaRepository<KomentarNaPricu, Long> {
    @Query("SELECT k from KomentarNaPricu k where k.prica.id = ?1 ")
    List<KomentarNaPricu> findByPrica(Long idPrice);
}
