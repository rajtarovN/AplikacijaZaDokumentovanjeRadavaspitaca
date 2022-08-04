package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Dete;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeteRepository extends JpaRepository<Dete, Long> {
    @Query("SELECT f from Dete f where f.grupa.id = ?1 ")
    List<Dete> findAllByGrupa(Long id);

    @Query("SELECT f from Dete f where f.formular.roditelj.user.login = ?1 ")
    List<Dete> findAllByRoditelj(String username);
}
