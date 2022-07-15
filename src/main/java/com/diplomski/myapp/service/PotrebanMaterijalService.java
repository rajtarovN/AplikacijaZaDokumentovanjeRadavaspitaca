package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.PotrebanMaterijal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PotrebanMaterijal}.
 */
public interface PotrebanMaterijalService {
    /**
     * Save a potrebanMaterijal.
     *
     * @param potrebanMaterijal the entity to save.
     * @return the persisted entity.
     */
    PotrebanMaterijal save(PotrebanMaterijal potrebanMaterijal);

    /**
     * Updates a potrebanMaterijal.
     *
     * @param potrebanMaterijal the entity to update.
     * @return the persisted entity.
     */
    PotrebanMaterijal update(PotrebanMaterijal potrebanMaterijal);

    /**
     * Partially updates a potrebanMaterijal.
     *
     * @param potrebanMaterijal the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PotrebanMaterijal> partialUpdate(PotrebanMaterijal potrebanMaterijal);

    /**
     * Get all the potrebanMaterijals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PotrebanMaterijal> findAll(Pageable pageable);

    /**
     * Get the "id" potrebanMaterijal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PotrebanMaterijal> findOne(Long id);

    /**
     * Delete the "id" potrebanMaterijal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
