package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Direktor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Direktor}.
 */
public interface DirektorService {
    /**
     * Save a direktor.
     *
     * @param direktor the entity to save.
     * @return the persisted entity.
     */
    Direktor save(Direktor direktor);

    /**
     * Updates a direktor.
     *
     * @param direktor the entity to update.
     * @return the persisted entity.
     */
    Direktor update(Direktor direktor);

    /**
     * Partially updates a direktor.
     *
     * @param direktor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Direktor> partialUpdate(Direktor direktor);

    /**
     * Get all the direktors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Direktor> findAll(Pageable pageable);

    /**
     * Get the "id" direktor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Direktor> findOne(Long id);

    /**
     * Delete the "id" direktor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
