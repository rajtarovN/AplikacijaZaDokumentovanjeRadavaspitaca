package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Adresa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Adresa}.
 */
public interface AdresaService {
    /**
     * Save a adresa.
     *
     * @param adresa the entity to save.
     * @return the persisted entity.
     */
    Adresa save(Adresa adresa);

    /**
     * Updates a adresa.
     *
     * @param adresa the entity to update.
     * @return the persisted entity.
     */
    Adresa update(Adresa adresa);

    /**
     * Partially updates a adresa.
     *
     * @param adresa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Adresa> partialUpdate(Adresa adresa);

    /**
     * Get all the adresas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Adresa> findAll(Pageable pageable);
    /**
     * Get all the Adresa where Formular is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Adresa> findAllWhereFormularIsNull();

    /**
     * Get the "id" adresa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Adresa> findOne(Long id);

    /**
     * Delete the "id" adresa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
