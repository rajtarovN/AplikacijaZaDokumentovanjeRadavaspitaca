package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.domain.enumeration.StatusFormulara;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Formular entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormularRepository extends JpaRepository<Formular, Long> {
    @Query("SELECT f from Formular f where f.statusFormulara = 'ODOBREN' ")
    List<Formular> findAllOdobren();

    @Query("SELECT f from Formular f where f.roditelj.user.login = ?1")
    List<Formular> findAllByRoditelj(String username);
}
