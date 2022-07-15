package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.PodaciORoditeljima;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PodaciORoditeljima}.
 */
public interface PodaciORoditeljimaService {
    /**
     * Save a podaciORoditeljima.
     *
     * @param podaciORoditeljima the entity to save.
     * @return the persisted entity.
     */
    PodaciORoditeljima save(PodaciORoditeljima podaciORoditeljima);

    /**
     * Updates a podaciORoditeljima.
     *
     * @param podaciORoditeljima the entity to update.
     * @return the persisted entity.
     */
    PodaciORoditeljima update(PodaciORoditeljima podaciORoditeljima);

    /**
     * Partially updates a podaciORoditeljima.
     *
     * @param podaciORoditeljima the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PodaciORoditeljima> partialUpdate(PodaciORoditeljima podaciORoditeljima);

    /**
     * Get all the podaciORoditeljimas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PodaciORoditeljima> findAll(Pageable pageable);

    /**
     * Get the "id" podaciORoditeljima.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PodaciORoditeljima> findOne(Long id);

    /**
     * Delete the "id" podaciORoditeljima.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
