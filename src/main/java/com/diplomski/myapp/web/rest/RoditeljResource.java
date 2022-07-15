package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Roditelj;
import com.diplomski.myapp.repository.RoditeljRepository;
import com.diplomski.myapp.service.RoditeljService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Roditelj}.
 */
@RestController
@RequestMapping("/api")
public class RoditeljResource {

    private final Logger log = LoggerFactory.getLogger(RoditeljResource.class);

    private static final String ENTITY_NAME = "roditelj";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoditeljService roditeljService;

    private final RoditeljRepository roditeljRepository;

    public RoditeljResource(RoditeljService roditeljService, RoditeljRepository roditeljRepository) {
        this.roditeljService = roditeljService;
        this.roditeljRepository = roditeljRepository;
    }

    /**
     * {@code POST  /roditeljs} : Create a new roditelj.
     *
     * @param roditelj the roditelj to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roditelj, or with status {@code 400 (Bad Request)} if the roditelj has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roditeljs")
    public ResponseEntity<Roditelj> createRoditelj(@RequestBody Roditelj roditelj) throws URISyntaxException {
        log.debug("REST request to save Roditelj : {}", roditelj);
        if (roditelj.getId() != null) {
            throw new BadRequestAlertException("A new roditelj cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Roditelj result = roditeljService.save(roditelj);
        return ResponseEntity
            .created(new URI("/api/roditeljs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roditeljs/:id} : Updates an existing roditelj.
     *
     * @param id the id of the roditelj to save.
     * @param roditelj the roditelj to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roditelj,
     * or with status {@code 400 (Bad Request)} if the roditelj is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roditelj couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roditeljs/{id}")
    public ResponseEntity<Roditelj> updateRoditelj(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Roditelj roditelj
    ) throws URISyntaxException {
        log.debug("REST request to update Roditelj : {}, {}", id, roditelj);
        if (roditelj.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roditelj.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roditeljRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Roditelj result = roditeljService.update(roditelj);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roditelj.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roditeljs/:id} : Partial updates given fields of an existing roditelj, field will ignore if it is null
     *
     * @param id the id of the roditelj to save.
     * @param roditelj the roditelj to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roditelj,
     * or with status {@code 400 (Bad Request)} if the roditelj is not valid,
     * or with status {@code 404 (Not Found)} if the roditelj is not found,
     * or with status {@code 500 (Internal Server Error)} if the roditelj couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/roditeljs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Roditelj> partialUpdateRoditelj(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Roditelj roditelj
    ) throws URISyntaxException {
        log.debug("REST request to partial update Roditelj partially : {}, {}", id, roditelj);
        if (roditelj.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roditelj.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roditeljRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Roditelj> result = roditeljService.partialUpdate(roditelj);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roditelj.getId().toString())
        );
    }

    /**
     * {@code GET  /roditeljs} : get all the roditeljs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roditeljs in body.
     */
    @GetMapping("/roditeljs")
    public ResponseEntity<List<Roditelj>> getAllRoditeljs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Roditeljs");
        Page<Roditelj> page = roditeljService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roditeljs/:id} : get the "id" roditelj.
     *
     * @param id the id of the roditelj to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roditelj, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roditeljs/{id}")
    public ResponseEntity<Roditelj> getRoditelj(@PathVariable Long id) {
        log.debug("REST request to get Roditelj : {}", id);
        Optional<Roditelj> roditelj = roditeljService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roditelj);
    }

    /**
     * {@code DELETE  /roditeljs/:id} : delete the "id" roditelj.
     *
     * @param id the id of the roditelj to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roditeljs/{id}")
    public ResponseEntity<Void> deleteRoditelj(@PathVariable Long id) {
        log.debug("REST request to delete Roditelj : {}", id);
        roditeljService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
