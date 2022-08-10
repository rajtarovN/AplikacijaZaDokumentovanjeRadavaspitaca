package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.NeDolasci;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NeDolasci entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NeDolasciRepository extends JpaRepository<NeDolasci, Long> {
    @Query("SELECT f from NeDolasci f where f.dnevnik.grupa.id = ?1 ")
    List<NeDolasci> findByGrupaId(Long id);

    @Query("SELECT f from NeDolasci f where f.dnevnik.grupa.id = ?1 and f.dete.id = ?2")
    List<Object> findByDeteAndGrupa(Long idGrupe, Long idDeteta);
}
