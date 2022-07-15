package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Grupa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Grupa}.
 */
public interface GrupaService {
    /**
     * Save a grupa.
     *
     * @param grupa the entity to save.
     * @return the persisted entity.
     */
    Grupa save(Grupa grupa);

    /**
     * Updates a grupa.
     *
     * @param grupa the entity to update.
     * @return the persisted entity.
     */
    Grupa update(Grupa grupa);

    /**
     * Partially updates a grupa.
     *
     * @param grupa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Grupa> partialUpdate(Grupa grupa);

    /**
     * Get all the grupas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Grupa> findAll(Pageable pageable);
    /**
     * Get all the Grupa where Dnevnik is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Grupa> findAllWhereDnevnikIsNull();

    /**
     * Get the "id" grupa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Grupa> findOne(Long id);

    /**
     * Delete the "id" grupa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
