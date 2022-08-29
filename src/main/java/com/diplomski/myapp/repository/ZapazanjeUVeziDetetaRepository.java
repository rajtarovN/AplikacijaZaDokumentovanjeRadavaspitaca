package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.ZapazanjeUVeziDeteta;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ZapazanjeUVeziDeteta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZapazanjeUVeziDetetaRepository extends JpaRepository<ZapazanjeUVeziDeteta, Long> {
    @Query("SELECT f from ZapazanjeUVeziDeteta f where f.dete.id = ?1")
    List<ZapazanjeUVeziDeteta> findByDete(Long idDeteta);
}
