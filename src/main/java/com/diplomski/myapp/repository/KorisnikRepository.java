package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Korisnik;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Korisnik entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {}
