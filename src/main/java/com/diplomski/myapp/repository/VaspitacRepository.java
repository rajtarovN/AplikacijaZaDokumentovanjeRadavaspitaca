package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Vaspitac;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vaspitac entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaspitacRepository extends JpaRepository<Vaspitac, Long> {
    @Query("SELECT d from Vaspitac d where  d.objekat.id = ?1 ")
    List<Vaspitac> getByObjekat(Long id);
}
