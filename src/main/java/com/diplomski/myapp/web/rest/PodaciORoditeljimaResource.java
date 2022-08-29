package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.PodaciORoditeljima;
import com.diplomski.myapp.repository.PodaciORoditeljimaRepository;
import com.diplomski.myapp.service.PodaciORoditeljimaService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diplomski.myapp.domain.PodaciORoditeljima}.
 */
@RestController
@RequestMapping("/api")
public class PodaciORoditeljimaResource {

    private final Logger log = LoggerFactory.getLogger(PodaciORoditeljimaResource.class);

    private static final String ENTITY_NAME = "podaciORoditeljima";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PodaciORoditeljimaService podaciORoditeljimaService;

    private final PodaciORoditeljimaRepository podaciORoditeljimaRepository;

    public PodaciORoditeljimaResource(
        PodaciORoditeljimaService podaciORoditeljimaService,
        PodaciORoditeljimaRepository podaciORoditeljimaRepository
    ) {
        this.podaciORoditeljimaService = podaciORoditeljimaService;
        this.podaciORoditeljimaRepository = podaciORoditeljimaRepository;
    }

    /**
     * {@code POST  /podaci-o-roditeljimas} : Create a new podaciORoditeljima.
     *
     * @param podaciORoditeljima the podaciORoditeljima to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new podaciORoditeljima, or with status {@code 400 (Bad Request)} if the podaciORoditeljima has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RODITELJ') ")
    @PostMapping("/podaci-o-roditeljimas")
    public ResponseEntity<PodaciORoditeljima> createPodaciORoditeljima(@RequestBody PodaciORoditeljima podaciORoditeljima)
        throws URISyntaxException {
        log.debug("REST request to save PodaciORoditeljima : {}", podaciORoditeljima);
        if (podaciORoditeljima.getId() != null) {
            throw new BadRequestAlertException("A new podaciORoditeljima cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PodaciORoditeljima result = podaciORoditeljimaService.save(podaciORoditeljima);
        return ResponseEntity
            .created(new URI("/api/podaci-o-roditeljimas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /podaci-o-roditeljimas/:id} : Updates an existing podaciORoditeljima.
     *
     * @param id the id of the podaciORoditeljima to save.
     * @param podaciORoditeljima the podaciORoditeljima to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated podaciORoditeljima,
     * or with status {@code 400 (Bad Request)} if the podaciORoditeljima is not valid,
     * or with status {@code 500 (Internal Server Error)} if the podaciORoditeljima couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RODITELJ') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG')")
    @PutMapping("/podaci-o-roditeljimas/{id}")
    public ResponseEntity<PodaciORoditeljima> updatePodaciORoditeljima(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PodaciORoditeljima podaciORoditeljima
    ) throws URISyntaxException {
        log.debug("REST request to update PodaciORoditeljima : {}, {}", id, podaciORoditeljima);
        if (podaciORoditeljima.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, podaciORoditeljima.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!podaciORoditeljimaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PodaciORoditeljima result = podaciORoditeljimaService.update(podaciORoditeljima);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, podaciORoditeljima.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /podaci-o-roditeljimas/:id} : Partial updates given fields of an existing podaciORoditeljima, field will ignore if it is null
     *
     * @param id the id of the podaciORoditeljima to save.
     * @param podaciORoditeljima the podaciORoditeljima to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated podaciORoditeljima,
     * or with status {@code 400 (Bad Request)} if the podaciORoditeljima is not valid,
     * or with status {@code 404 (Not Found)} if the podaciORoditeljima is not found,
     * or with status {@code 500 (Internal Server Error)} if the podaciORoditeljima couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RODITELJ') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG')")
    @PatchMapping(value = "/podaci-o-roditeljimas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PodaciORoditeljima> partialUpdatePodaciORoditeljima(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PodaciORoditeljima podaciORoditeljima
    ) throws URISyntaxException {
        log.debug("REST request to partial update PodaciORoditeljima partially : {}, {}", id, podaciORoditeljima);
        if (podaciORoditeljima.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, podaciORoditeljima.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!podaciORoditeljimaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PodaciORoditeljima> result = podaciORoditeljimaService.partialUpdate(podaciORoditeljima);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, podaciORoditeljima.getId().toString())
        );
    }

    /**
     * {@code GET  /podaci-o-roditeljimas} : get all the podaciORoditeljimas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of podaciORoditeljimas in body.
     */
    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_RODITELJ') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG') or hasRole('ROLE_VASPITAC')"
    )
    @GetMapping("/podaci-o-roditeljimas")
    public ResponseEntity<List<PodaciORoditeljima>> getAllPodaciORoditeljimas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PodaciORoditeljimas");
        Page<PodaciORoditeljima> page = podaciORoditeljimaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /podaci-o-roditeljimas/:id} : get the "id" podaciORoditeljima.
     *
     * @param id the id of the podaciORoditeljima to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the podaciORoditeljima, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_RODITELJ') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG') or hasRole('ROLE_VASPITAC')"
    )
    @GetMapping("/podaci-o-roditeljimas/{id}")
    public ResponseEntity<PodaciORoditeljima> getPodaciORoditeljima(@PathVariable Long id) {
        log.debug("REST request to get PodaciORoditeljima : {}", id);
        Optional<PodaciORoditeljima> podaciORoditeljima = podaciORoditeljimaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(podaciORoditeljima);
    }

    /**
     * {@code DELETE  /podaci-o-roditeljimas/:id} : delete the "id" podaciORoditeljima.
     *
     * @param id the id of the podaciORoditeljima to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RODITELJ') ")
    @DeleteMapping("/podaci-o-roditeljimas/{id}")
    public ResponseEntity<Void> deletePodaciORoditeljima(@PathVariable Long id) {
        log.debug("REST request to delete PodaciORoditeljima : {}", id);
        podaciORoditeljimaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
