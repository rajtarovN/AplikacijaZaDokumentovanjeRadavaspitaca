package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Roditelj;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Roditelj}.
 */
public interface RoditeljService {
    /**
     * Save a roditelj.
     *
     * @param roditelj the entity to save.
     * @return the persisted entity.
     */
    Roditelj save(Roditelj roditelj);

    /**
     * Updates a roditelj.
     *
     * @param roditelj the entity to update.
     * @return the persisted entity.
     */
    Roditelj update(Roditelj roditelj);

    /**
     * Partially updates a roditelj.
     *
     * @param roditelj the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Roditelj> partialUpdate(Roditelj roditelj);

    /**
     * Get all the roditeljs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Roditelj> findAll(Pageable pageable);

    /**
     * Get the "id" roditelj.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Roditelj> findOne(Long id);

    /**
     * Delete the "id" roditelj.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
