package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.PodaciORoditeljima;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PodaciORoditeljima entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PodaciORoditeljimaRepository extends JpaRepository<PodaciORoditeljima, Long> {
    @Query("SELECT u from PodaciORoditeljima u where u.formular.id = ?1 ")
    Set<PodaciORoditeljima> findByFormular(Long id);
}
