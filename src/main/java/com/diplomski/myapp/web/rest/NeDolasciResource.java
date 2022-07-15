package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.NeDolasci;
import com.diplomski.myapp.repository.NeDolasciRepository;
import com.diplomski.myapp.service.NeDolasciService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.NeDolasci}.
 */
@RestController
@RequestMapping("/api")
public class NeDolasciResource {

    private final Logger log = LoggerFactory.getLogger(NeDolasciResource.class);

    private static final String ENTITY_NAME = "neDolasci";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NeDolasciService neDolasciService;

    private final NeDolasciRepository neDolasciRepository;

    public NeDolasciResource(NeDolasciService neDolasciService, NeDolasciRepository neDolasciRepository) {
        this.neDolasciService = neDolasciService;
        this.neDolasciRepository = neDolasciRepository;
    }

    /**
     * {@code POST  /ne-dolascis} : Create a new neDolasci.
     *
     * @param neDolasci the neDolasci to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new neDolasci, or with status {@code 400 (Bad Request)} if the neDolasci has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ne-dolascis")
    public ResponseEntity<NeDolasci> createNeDolasci(@RequestBody NeDolasci neDolasci) throws URISyntaxException {
        log.debug("REST request to save NeDolasci : {}", neDolasci);
        if (neDolasci.getId() != null) {
            throw new BadRequestAlertException("A new neDolasci cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NeDolasci result = neDolasciService.save(neDolasci);
        return ResponseEntity
            .created(new URI("/api/ne-dolascis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ne-dolascis/:id} : Updates an existing neDolasci.
     *
     * @param id the id of the neDolasci to save.
     * @param neDolasci the neDolasci to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated neDolasci,
     * or with status {@code 400 (Bad Request)} if the neDolasci is not valid,
     * or with status {@code 500 (Internal Server Error)} if the neDolasci couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ne-dolascis/{id}")
    public ResponseEntity<NeDolasci> updateNeDolasci(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NeDolasci neDolasci
    ) throws URISyntaxException {
        log.debug("REST request to update NeDolasci : {}, {}", id, neDolasci);
        if (neDolasci.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, neDolasci.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!neDolasciRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NeDolasci result = neDolasciService.update(neDolasci);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, neDolasci.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ne-dolascis/:id} : Partial updates given fields of an existing neDolasci, field will ignore if it is null
     *
     * @param id the id of the neDolasci to save.
     * @param neDolasci the neDolasci to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated neDolasci,
     * or with status {@code 400 (Bad Request)} if the neDolasci is not valid,
     * or with status {@code 404 (Not Found)} if the neDolasci is not found,
     * or with status {@code 500 (Internal Server Error)} if the neDolasci couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ne-dolascis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NeDolasci> partialUpdateNeDolasci(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NeDolasci neDolasci
    ) throws URISyntaxException {
        log.debug("REST request to partial update NeDolasci partially : {}, {}", id, neDolasci);
        if (neDolasci.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, neDolasci.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!neDolasciRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NeDolasci> result = neDolasciService.partialUpdate(neDolasci);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, neDolasci.getId().toString())
        );
    }

    /**
     * {@code GET  /ne-dolascis} : get all the neDolascis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of neDolascis in body.
     */
    @GetMapping("/ne-dolascis")
    public ResponseEntity<List<NeDolasci>> getAllNeDolascis(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of NeDolascis");
        Page<NeDolasci> page = neDolasciService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ne-dolascis/:id} : get the "id" neDolasci.
     *
     * @param id the id of the neDolasci to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the neDolasci, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ne-dolascis/{id}")
    public ResponseEntity<NeDolasci> getNeDolasci(@PathVariable Long id) {
        log.debug("REST request to get NeDolasci : {}", id);
        Optional<NeDolasci> neDolasci = neDolasciService.findOne(id);
        return ResponseUtil.wrapOrNotFound(neDolasci);
    }

    /**
     * {@code DELETE  /ne-dolascis/:id} : delete the "id" neDolasci.
     *
     * @param id the id of the neDolasci to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ne-dolascis/{id}")
    public ResponseEntity<Void> deleteNeDolasci(@PathVariable Long id) {
        log.debug("REST request to delete NeDolasci : {}", id);
        neDolasciService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
