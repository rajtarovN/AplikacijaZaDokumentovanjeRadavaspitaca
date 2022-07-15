package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.PlanPrice;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PlanPrice}.
 */
public interface PlanPriceService {
    /**
     * Save a planPrice.
     *
     * @param planPrice the entity to save.
     * @return the persisted entity.
     */
    PlanPrice save(PlanPrice planPrice);

    /**
     * Updates a planPrice.
     *
     * @param planPrice the entity to update.
     * @return the persisted entity.
     */
    PlanPrice update(PlanPrice planPrice);

    /**
     * Partially updates a planPrice.
     *
     * @param planPrice the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlanPrice> partialUpdate(PlanPrice planPrice);

    /**
     * Get all the planPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanPrice> findAll(Pageable pageable);

    /**
     * Get the "id" planPrice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanPrice> findOne(Long id);

    /**
     * Delete the "id" planPrice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
