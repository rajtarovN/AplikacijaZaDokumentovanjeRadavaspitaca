package com.diplomski.myapp.service;

import com.diplomski.myapp.domain.Korisnik;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Korisnik}.
 */
public interface KorisnikService {
    /**
     * Save a korisnik.
     *
     * @param korisnik the entity to save.
     * @return the persisted entity.
     */
    Korisnik save(Korisnik korisnik);

    /**
     * Updates a korisnik.
     *
     * @param korisnik the entity to update.
     * @return the persisted entity.
     */
    Korisnik update(Korisnik korisnik);

    /**
     * Partially updates a korisnik.
     *
     * @param korisnik the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Korisnik> partialUpdate(Korisnik korisnik);

    /**
     * Get all the korisniks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Korisnik> findAll(Pageable pageable);

    /**
     * Get the "id" korisnik.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Korisnik> findOne(Long id);

    /**
     * Delete the "id" korisnik.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
