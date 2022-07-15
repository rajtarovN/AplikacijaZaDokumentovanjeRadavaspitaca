package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.ZapazanjeUVeziDeteta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ZapazanjeUVeziDeteta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZapazanjeUVeziDetetaRepository extends JpaRepository<ZapazanjeUVeziDeteta, Long> {}
