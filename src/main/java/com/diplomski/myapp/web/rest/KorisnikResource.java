package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Korisnik;
import com.diplomski.myapp.repository.KorisnikRepository;
import com.diplomski.myapp.service.KorisnikService;
import com.diplomski.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diplomski.myapp.domain.Korisnik}.
 */
@RestController
@RequestMapping("/api")
public class KorisnikResource {

    private final Logger log = LoggerFactory.getLogger(KorisnikResource.class);

    private static final String ENTITY_NAME = "korisnik";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KorisnikService korisnikService;

    private final KorisnikRepository korisnikRepository;

    public KorisnikResource(KorisnikService korisnikService, KorisnikRepository korisnikRepository) {
        this.korisnikService = korisnikService;
        this.korisnikRepository = korisnikRepository;
    }

    /**
     * {@code POST  /korisniks} : Create a new korisnik.
     *
     * @param korisnik the korisnik to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new korisnik, or with status {@code 400 (Bad Request)} if the korisnik has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/korisniks")
    public ResponseEntity<Korisnik> createKorisnik(@RequestBody Korisnik korisnik) throws URISyntaxException {
        log.debug("REST request to save Korisnik : {}", korisnik);
        if (korisnik.getId() != null) {
            throw new BadRequestAlertException("A new korisnik cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Korisnik result = korisnikService.save(korisnik);
        return ResponseEntity
            .created(new URI("/api/korisniks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /korisniks/:id} : Updates an existing korisnik.
     *
     * @param id the id of the korisnik to save.
     * @param korisnik the korisnik to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated korisnik,
     * or with status {@code 400 (Bad Request)} if the korisnik is not valid,
     * or with status {@code 500 (Internal Server Error)} if the korisnik couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/korisniks/{id}")
    public ResponseEntity<Korisnik> updateKorisnik(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Korisnik korisnik
    ) throws URISyntaxException {
        log.debug("REST request to update Korisnik : {}, {}", id, korisnik);
        if (korisnik.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, korisnik.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!korisnikRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Korisnik result = korisnikService.update(korisnik);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, korisnik.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /korisniks/:id} : Partial updates given fields of an existing korisnik, field will ignore if it is null
     *
     * @param id the id of the korisnik to save.
     * @param korisnik the korisnik to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated korisnik,
     * or with status {@code 400 (Bad Request)} if the korisnik is not valid,
     * or with status {@code 404 (Not Found)} if the korisnik is not found,
     * or with status {@code 500 (Internal Server Error)} if the korisnik couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/korisniks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Korisnik> partialUpdateKorisnik(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Korisnik korisnik
    ) throws URISyntaxException {
        log.debug("REST request to partial update Korisnik partially : {}, {}", id, korisnik);
        if (korisnik.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, korisnik.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!korisnikRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Korisnik> result = korisnikService.partialUpdate(korisnik);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, korisnik.getId().toString())
        );
    }

    /**
     * {@code GET  /korisniks} : get all the korisniks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of korisniks in body.
     */
    @GetMapping("/korisniks")
    public ResponseEntity<List<Korisnik>> getAllKorisniks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Korisniks");
        Page<Korisnik> page = korisnikService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /korisniks/:id} : get the "id" korisnik.
     *
     * @param id the id of the korisnik to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the korisnik, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/korisniks/{id}")
    public ResponseEntity<Korisnik> getKorisnik(@PathVariable Long id) {
        log.debug("REST request to get Korisnik : {}", id);
        Optional<Korisnik> korisnik = korisnikService.findOne(id);
        return ResponseUtil.wrapOrNotFound(korisnik);
    }

    /**
     * {@code DELETE  /korisniks/:id} : delete the "id" korisnik.
     *
     * @param id the id of the korisnik to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/korisniks/{id}")
    public ResponseEntity<Void> deleteKorisnik(@PathVariable Long id) {
        log.debug("REST request to delete Korisnik : {}", id);
        korisnikService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
