package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.KomentarNaPricu;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link KomentarNaPricu}.
 */
public interface KomentarNaPricuService {
    /**
     * Save a komentarNaPricu.
     *
     * @param komentarNaPricu the entity to save.
     * @return the persisted entity.
     */
    KomentarNaPricu save(KomentarNaPricu komentarNaPricu);

    /**
     * Updates a komentarNaPricu.
     *
     * @param komentarNaPricu the entity to update.
     * @return the persisted entity.
     */
    KomentarNaPricu update(KomentarNaPricu komentarNaPricu);

    /**
     * Partially updates a komentarNaPricu.
     *
     * @param komentarNaPricu the entity to update partially.
     * @return the persisted entity.
     */
    Optional<KomentarNaPricu> partialUpdate(KomentarNaPricu komentarNaPricu);

    /**
     * Get all the komentarNaPricus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KomentarNaPricu> findAll(Pageable pageable);

    /**
     * Get the "id" komentarNaPricu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KomentarNaPricu> findOne(Long id);

    /**
     * Delete the "id" komentarNaPricu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
