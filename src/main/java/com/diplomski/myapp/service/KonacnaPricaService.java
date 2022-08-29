package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.KonacnaPrica;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link KonacnaPrica}.
 */
public interface KonacnaPricaService {
    /**
     * Save a konacnaPrica.
     *
     * @param konacnaPrica the entity to save.
     * @return the persisted entity.
     */
    KonacnaPrica save(KonacnaPrica konacnaPrica, Long idPrice);

    /**
     * Updates a konacnaPrica.
     *
     * @param konacnaPrica the entity to update.
     * @return the persisted entity.
     */
    KonacnaPrica update(KonacnaPrica konacnaPrica);

    /**
     * Partially updates a konacnaPrica.
     *
     * @param konacnaPrica the entity to update partially.
     * @return the persisted entity.
     */
    Optional<KonacnaPrica> partialUpdate(KonacnaPrica konacnaPrica);

    /**
     * Get all the konacnaPricas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KonacnaPrica> findAll(Pageable pageable);

    /**
     * Get the "id" konacnaPrica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KonacnaPrica> findOne(Long id);

    /**
     * Delete the "id" konacnaPrica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
