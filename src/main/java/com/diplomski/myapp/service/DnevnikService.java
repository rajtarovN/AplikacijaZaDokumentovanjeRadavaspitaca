package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Dnevnik;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dnevnik}.
 */
public interface DnevnikService {
    /**
     * Save a dnevnik.
     *
     * @param dnevnik the entity to save.
     * @return the persisted entity.
     */
    Dnevnik save(Dnevnik dnevnik);

    /**
     * Updates a dnevnik.
     *
     * @param dnevnik the entity to update.
     * @return the persisted entity.
     */
    Dnevnik update(Dnevnik dnevnik);

    /**
     * Partially updates a dnevnik.
     *
     * @param dnevnik the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dnevnik> partialUpdate(Dnevnik dnevnik);

    /**
     * Get all the dnevniks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dnevnik> findAll(Pageable pageable);

    /**
     * Get all the dnevniks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dnevnik> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dnevnik.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dnevnik> findOne(Long id);

    /**
     * Delete the "id" dnevnik.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
