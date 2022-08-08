package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Roditelj;
import com.diplomski.myapp.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Roditelj entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoditeljRepository extends JpaRepository<Roditelj, Long> {
    @Query("SELECT u.user from Roditelj u where u.id = ?1 ")
    User findByUser(Long id);

    @Query("SELECT u from Roditelj u where u.user.login = ?1 ")
    Roditelj findByUsername(String username);
}
