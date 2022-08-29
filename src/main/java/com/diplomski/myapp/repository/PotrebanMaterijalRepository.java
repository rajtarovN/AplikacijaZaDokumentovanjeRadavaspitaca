package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.PotrebanMaterijal;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PotrebanMaterijal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotrebanMaterijalRepository extends JpaRepository<PotrebanMaterijal, Long> {
    @Query("SELECT d from PotrebanMaterijal d where  d.objekat.id = ?1 ")
    List<PotrebanMaterijal> findByObjekatId(Long objekatId);
}
