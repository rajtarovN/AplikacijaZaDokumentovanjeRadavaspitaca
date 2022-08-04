package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.NeDolasci;
import com.diplomski.myapp.web.rest.dto.NeDolasciDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link NeDolasci}.
 */
public interface NeDolasciService {
    /**
     * Save a neDolasci.
     *
     * @param neDolasci the entity to save.
     * @return the persisted entity.
     */
    NeDolasci save(NeDolasci neDolasci);

    /**
     * Updates a neDolasci.
     *
     * @param neDolasci the entity to update.
     * @return the persisted entity.
     */
    NeDolasci update(NeDolasci neDolasci);

    /**
     * Partially updates a neDolasci.
     *
     * @param neDolasci the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NeDolasci> partialUpdate(NeDolasci neDolasci);

    /**
     * Get all the neDolascis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NeDolasci> findAll(Pageable pageable);

    /**
     * Get the "id" neDolasci.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NeDolasci> findOne(Long id);

    /**
     * Delete the "id" neDolasci.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    String saveList(List<NeDolasciDTO> neDolasci);

    Page<NeDolasci> findAllByGrupa(Pageable pageable, Long id);
}
