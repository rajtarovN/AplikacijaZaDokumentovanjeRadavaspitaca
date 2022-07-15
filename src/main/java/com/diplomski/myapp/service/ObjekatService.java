package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Objekat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Objekat}.
 */
public interface ObjekatService {
    /**
     * Save a objekat.
     *
     * @param objekat the entity to save.
     * @return the persisted entity.
     */
    Objekat save(Objekat objekat);

    /**
     * Updates a objekat.
     *
     * @param objekat the entity to update.
     * @return the persisted entity.
     */
    Objekat update(Objekat objekat);

    /**
     * Partially updates a objekat.
     *
     * @param objekat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Objekat> partialUpdate(Objekat objekat);

    /**
     * Get all the objekats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Objekat> findAll(Pageable pageable);

    /**
     * Get the "id" objekat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Objekat> findOne(Long id);

    /**
     * Delete the "id" objekat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
