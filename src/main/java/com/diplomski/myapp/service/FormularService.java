package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.web.rest.dto.DeteZaGrupuDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Formular}.
 */
public interface FormularService {
    /**
     * Save a formular.
     *
     * @param formular the entity to save.
     * @return the persisted entity.
     */
    Formular save(Formular formular, String username);

    /**
     * Updates a formular.
     *
     * @param formular the entity to update.
     * @return the persisted entity.
     */
    Formular update(Formular formular);

    /**
     * Partially updates a formular.
     *
     * @param formular the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Formular> partialUpdate(Formular formular);

    /**
     * Get all the formulars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Formular> findAll(Pageable pageable);
    /**
     * Get all the Formular where Dete is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Formular> findAllWhereDeteIsNull();

    /**
     * Get the "id" formular.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Formular> findOne(Long id);

    /**
     * Delete the "id" formular.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<DeteZaGrupuDTO> findAllDecaZaGrupu();

    Page<Formular> findAllByRoditelj(Pageable pageable, String username);

    Formular approve(Long id);

    Formular reject(Long id);
}
