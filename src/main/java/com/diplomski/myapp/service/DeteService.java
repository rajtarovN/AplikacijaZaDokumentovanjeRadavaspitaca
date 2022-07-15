package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Dete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dete}.
 */
public interface DeteService {
    /**
     * Save a dete.
     *
     * @param dete the entity to save.
     * @return the persisted entity.
     */
    Dete save(Dete dete);

    /**
     * Updates a dete.
     *
     * @param dete the entity to update.
     * @return the persisted entity.
     */
    Dete update(Dete dete);

    /**
     * Partially updates a dete.
     *
     * @param dete the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dete> partialUpdate(Dete dete);

    /**
     * Get all the detes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dete> findAll(Pageable pageable);
    /**
     * Get all the Dete where NeDolasci is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Dete> findAllWhereNeDolasciIsNull();

    /**
     * Get the "id" dete.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dete> findOne(Long id);

    /**
     * Delete the "id" dete.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
