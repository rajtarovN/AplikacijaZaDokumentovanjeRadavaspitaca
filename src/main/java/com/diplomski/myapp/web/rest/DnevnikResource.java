package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.repository.DnevnikRepository;
import com.diplomski.myapp.service.DnevnikService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Dnevnik}.
 */
@RestController
@RequestMapping("/api")
public class DnevnikResource {

    private final Logger log = LoggerFactory.getLogger(DnevnikResource.class);

    private static final String ENTITY_NAME = "dnevnik";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DnevnikService dnevnikService;

    private final DnevnikRepository dnevnikRepository;

    public DnevnikResource(DnevnikService dnevnikService, DnevnikRepository dnevnikRepository) {
        this.dnevnikService = dnevnikService;
        this.dnevnikRepository = dnevnikRepository;
    }

    /**
     * {@code POST  /dnevniks} : Create a new dnevnik.
     *
     * @param dnevnik the dnevnik to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dnevnik, or with status {@code 400 (Bad Request)} if the dnevnik has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dnevniks")
    public ResponseEntity<Dnevnik> createDnevnik(@RequestBody Dnevnik dnevnik) throws URISyntaxException {
        log.debug("REST request to save Dnevnik : {}", dnevnik);
        if (dnevnik.getId() != null) {
            throw new BadRequestAlertException("A new dnevnik cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dnevnik result = dnevnikService.save(dnevnik);
        return ResponseEntity
            .created(new URI("/api/dnevniks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dnevniks/:id} : Updates an existing dnevnik.
     *
     * @param id the id of the dnevnik to save.
     * @param dnevnik the dnevnik to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dnevnik,
     * or with status {@code 400 (Bad Request)} if the dnevnik is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dnevnik couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dnevniks/{id}")
    public ResponseEntity<Dnevnik> updateDnevnik(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dnevnik dnevnik)
        throws URISyntaxException {
        log.debug("REST request to update Dnevnik : {}, {}", id, dnevnik);
        if (dnevnik.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dnevnik.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dnevnikRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dnevnik result = dnevnikService.update(dnevnik);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dnevnik.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dnevniks/:id} : Partial updates given fields of an existing dnevnik, field will ignore if it is null
     *
     * @param id the id of the dnevnik to save.
     * @param dnevnik the dnevnik to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dnevnik,
     * or with status {@code 400 (Bad Request)} if the dnevnik is not valid,
     * or with status {@code 404 (Not Found)} if the dnevnik is not found,
     * or with status {@code 500 (Internal Server Error)} if the dnevnik couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dnevniks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dnevnik> partialUpdateDnevnik(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dnevnik dnevnik
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dnevnik partially : {}, {}", id, dnevnik);
        if (dnevnik.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dnevnik.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dnevnikRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dnevnik> result = dnevnikService.partialUpdate(dnevnik);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dnevnik.getId().toString())
        );
    }

    /**
     * {@code GET  /dnevniks} : get all the dnevniks.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dnevniks in body.
     */
    @GetMapping("/dnevniks")
    public ResponseEntity<List<Dnevnik>> getAllDnevniks(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Dnevniks");
        Page<Dnevnik> page;
        if (eagerload) {
            page = dnevnikService.findAllWithEagerRelationships(pageable);
        } else {
            page = dnevnikService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dnevniks/:id} : get the "id" dnevnik.
     *
     * @param id the id of the dnevnik to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dnevnik, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dnevniks/{id}")
    public ResponseEntity<Dnevnik> getDnevnik(@PathVariable Long id) {
        log.debug("REST request to get Dnevnik : {}", id);
        Optional<Dnevnik> dnevnik = dnevnikService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dnevnik);
    }

    /**
     * {@code DELETE  /dnevniks/:id} : delete the "id" dnevnik.
     *
     * @param id the id of the dnevnik to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dnevniks/{id}")
    public ResponseEntity<Void> deleteDnevnik(@PathVariable Long id) {
        log.debug("REST request to delete Dnevnik : {}", id);
        dnevnikService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
