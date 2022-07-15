package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.ZapazanjeUVeziDeteta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ZapazanjeUVeziDeteta}.
 */
public interface ZapazanjeUVeziDetetaService {
    /**
     * Save a zapazanjeUVeziDeteta.
     *
     * @param zapazanjeUVeziDeteta the entity to save.
     * @return the persisted entity.
     */
    ZapazanjeUVeziDeteta save(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta);

    /**
     * Updates a zapazanjeUVeziDeteta.
     *
     * @param zapazanjeUVeziDeteta the entity to update.
     * @return the persisted entity.
     */
    ZapazanjeUVeziDeteta update(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta);

    /**
     * Partially updates a zapazanjeUVeziDeteta.
     *
     * @param zapazanjeUVeziDeteta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ZapazanjeUVeziDeteta> partialUpdate(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta);

    /**
     * Get all the zapazanjeUVeziDetetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ZapazanjeUVeziDeteta> findAll(Pageable pageable);

    /**
     * Get the "id" zapazanjeUVeziDeteta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ZapazanjeUVeziDeteta> findOne(Long id);

    /**
     * Delete the "id" zapazanjeUVeziDeteta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
