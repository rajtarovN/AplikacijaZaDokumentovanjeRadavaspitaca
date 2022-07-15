package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Pedagog;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pedagog}.
 */
public interface PedagogService {
    /**
     * Save a pedagog.
     *
     * @param pedagog the entity to save.
     * @return the persisted entity.
     */
    Pedagog save(Pedagog pedagog);

    /**
     * Updates a pedagog.
     *
     * @param pedagog the entity to update.
     * @return the persisted entity.
     */
    Pedagog update(Pedagog pedagog);

    /**
     * Partially updates a pedagog.
     *
     * @param pedagog the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pedagog> partialUpdate(Pedagog pedagog);

    /**
     * Get all the pedagogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pedagog> findAll(Pageable pageable);

    /**
     * Get the "id" pedagog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pedagog> findOne(Long id);

    /**
     * Delete the "id" pedagog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
