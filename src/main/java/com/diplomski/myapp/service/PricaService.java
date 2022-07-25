package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.KonacnaPrica;
import com.diplomski.myapp.domain.Prica;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Prica}.
 */
public interface PricaService {
    /**
     * Save a prica.
     *
     * @param prica the entity to save.
     * @return the persisted entity.
     */
    Prica save(Prica prica);

    /**
     * Updates a prica.
     *
     * @param prica the entity to update.
     * @return the persisted entity.
     */
    Prica update(Prica prica);

    /**
     * Partially updates a prica.
     *
     * @param prica the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Prica> partialUpdate(Prica prica);

    /**
     * Get all the pricas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Prica> findAll(Pageable pageable);

    /**
     * Get the "id" prica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Prica> findOne(Long id);

    /**
     * Delete the "id" prica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    KonacnaPrica startWriting(Long id);
}
