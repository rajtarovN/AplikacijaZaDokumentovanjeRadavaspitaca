package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Pedagog;
import com.diplomski.myapp.repository.PedagogRepository;
import com.diplomski.myapp.service.PedagogService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Pedagog}.
 */
@RestController
@RequestMapping("/api")
public class PedagogResource {

    private final Logger log = LoggerFactory.getLogger(PedagogResource.class);

    private static final String ENTITY_NAME = "pedagog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PedagogService pedagogService;

    private final PedagogRepository pedagogRepository;

    public PedagogResource(PedagogService pedagogService, PedagogRepository pedagogRepository) {
        this.pedagogService = pedagogService;
        this.pedagogRepository = pedagogRepository;
    }

    /**
     * {@code POST  /pedagogs} : Create a new pedagog.
     *
     * @param pedagog the pedagog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pedagog, or with status {@code 400 (Bad Request)} if the pedagog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pedagogs")
    public ResponseEntity<Pedagog> createPedagog(@RequestBody Pedagog pedagog) throws URISyntaxException {
        log.debug("REST request to save Pedagog : {}", pedagog);
        if (pedagog.getId() != null) {
            throw new BadRequestAlertException("A new pedagog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pedagog result = pedagogService.save(pedagog);
        return ResponseEntity
            .created(new URI("/api/pedagogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pedagogs/:id} : Updates an existing pedagog.
     *
     * @param id the id of the pedagog to save.
     * @param pedagog the pedagog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedagog,
     * or with status {@code 400 (Bad Request)} if the pedagog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pedagog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pedagogs/{id}")
    public ResponseEntity<Pedagog> updatePedagog(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pedagog pedagog)
        throws URISyntaxException {
        log.debug("REST request to update Pedagog : {}, {}", id, pedagog);
        if (pedagog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedagog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedagogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pedagog result = pedagogService.update(pedagog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedagog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pedagogs/:id} : Partial updates given fields of an existing pedagog, field will ignore if it is null
     *
     * @param id the id of the pedagog to save.
     * @param pedagog the pedagog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedagog,
     * or with status {@code 400 (Bad Request)} if the pedagog is not valid,
     * or with status {@code 404 (Not Found)} if the pedagog is not found,
     * or with status {@code 500 (Internal Server Error)} if the pedagog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pedagogs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pedagog> partialUpdatePedagog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pedagog pedagog
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pedagog partially : {}, {}", id, pedagog);
        if (pedagog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedagog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedagogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pedagog> result = pedagogService.partialUpdate(pedagog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedagog.getId().toString())
        );
    }

    /**
     * {@code GET  /pedagogs} : get all the pedagogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pedagogs in body.
     */
    @GetMapping("/pedagogs")
    public ResponseEntity<List<Pedagog>> getAllPedagogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pedagogs");
        Page<Pedagog> page = pedagogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pedagogs/:id} : get the "id" pedagog.
     *
     * @param id the id of the pedagog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pedagog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pedagogs/{id}")
    public ResponseEntity<Pedagog> getPedagog(@PathVariable Long id) {
        log.debug("REST request to get Pedagog : {}", id);
        Optional<Pedagog> pedagog = pedagogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pedagog);
    }

    /**
     * {@code DELETE  /pedagogs/:id} : delete the "id" pedagog.
     *
     * @param id the id of the pedagog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pedagogs/{id}")
    public ResponseEntity<Void> deletePedagog(@PathVariable Long id) {
        log.debug("REST request to delete Pedagog : {}", id);
        pedagogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
