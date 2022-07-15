package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Vaspitac;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Vaspitac}.
 */
public interface VaspitacService {
    /**
     * Save a vaspitac.
     *
     * @param vaspitac the entity to save.
     * @return the persisted entity.
     */
    Vaspitac save(Vaspitac vaspitac);

    /**
     * Updates a vaspitac.
     *
     * @param vaspitac the entity to update.
     * @return the persisted entity.
     */
    Vaspitac update(Vaspitac vaspitac);

    /**
     * Partially updates a vaspitac.
     *
     * @param vaspitac the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vaspitac> partialUpdate(Vaspitac vaspitac);

    /**
     * Get all the vaspitacs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vaspitac> findAll(Pageable pageable);
    /**
     * Get all the Vaspitac where ZapazanjeUVeziDeteta is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Vaspitac> findAllWhereZapazanjeUVeziDetetaIsNull();

    /**
     * Get the "id" vaspitac.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vaspitac> findOne(Long id);

    /**
     * Delete the "id" vaspitac.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
