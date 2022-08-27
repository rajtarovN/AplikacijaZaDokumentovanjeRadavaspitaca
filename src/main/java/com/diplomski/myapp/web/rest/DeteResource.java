package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.repository.DeteRepository;
import com.diplomski.myapp.security.AuthoritiesConstants;
import com.diplomski.myapp.service.DeteService;
import com.diplomski.myapp.web.rest.dto.ProfilDetetaDTO;
import com.diplomski.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Dete}.
 */
@RestController
@RequestMapping("/api")
public class DeteResource {

    private final Logger log = LoggerFactory.getLogger(DeteResource.class);

    private static final String ENTITY_NAME = "dete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeteService deteService;

    private final DeteRepository deteRepository;

    public DeteResource(DeteService deteService, DeteRepository deteRepository) {
        this.deteService = deteService;
        this.deteRepository = deteRepository;
    }

    /**
     * {@code POST  /detes} : Create a new dete.
     *
     * @param dete the dete to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dete, or with status {@code 400 (Bad Request)} if the dete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detes")
    public ResponseEntity<Dete> createDete(@RequestBody Dete dete) throws URISyntaxException {
        log.debug("REST request to save Dete : {}", dete);
        if (dete.getId() != null) {
            throw new BadRequestAlertException("A new dete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dete result = deteService.save(dete);
        return ResponseEntity
            .created(new URI("/api/detes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detes/:id} : Updates an existing dete.
     *
     * @param id the id of the dete to save.
     * @param dete the dete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dete,
     * or with status {@code 400 (Bad Request)} if the dete is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detes/{id}")
    public ResponseEntity<Dete> updateDete(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dete dete)
        throws URISyntaxException {
        log.debug("REST request to update Dete : {}, {}", id, dete);
        if (dete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dete result = deteService.update(dete);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dete.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detes/:id} : Partial updates given fields of an existing dete, field will ignore if it is null
     *
     * @param id the id of the dete to save.
     * @param dete the dete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dete,
     * or with status {@code 400 (Bad Request)} if the dete is not valid,
     * or with status {@code 404 (Not Found)} if the dete is not found,
     * or with status {@code 500 (Internal Server Error)} if the dete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dete> partialUpdateDete(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dete dete)
        throws URISyntaxException {
        log.debug("REST request to partial update Dete partially : {}, {}", id, dete);
        if (dete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dete> result = deteService.partialUpdate(dete);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dete.getId().toString())
        );
    }

    /**
     * {@code GET  /detes} : get all the detes.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detes in body.
     */
    @GetMapping("/detes")
    public ResponseEntity<List<Dete>> getAllDetes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("nedolasci-is-null".equals(filter)) {
            log.debug("REST request to get all Detes where neDolasci is null");
            return new ResponseEntity<>(deteService.findAllWhereNeDolasciIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Detes");
        Page<Dete> page = deteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detes/:id} : get the "id" dete.
     *
     * @param id the id of the dete to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dete, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detes/{id}")
    public ResponseEntity<Dete> getDete(@PathVariable Long id) {
        log.debug("REST request to get Dete : {}", id);
        Optional<Dete> dete = deteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dete);
    }

    /**
     * {@code DELETE  /detes/:id} : delete the "id" dete.
     *
     * @param id the id of the dete to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detes/{id}")
    public ResponseEntity<Void> deleteDete(@PathVariable Long id) {
        log.debug("REST request to delete Dete : {}", id);
        deteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG') or hasRole('ROLE_VASPITAC') or hasRole('ROLE_RODITELJ')"
    )
    @GetMapping("/detes/profil/{id}")
    public ResponseEntity<ProfilDetetaDTO> getProfil(@PathVariable Long id) {
        log.debug("REST request to get Dete profil : {}", id);
        ProfilDetetaDTO dete = deteService.findProfil(id);
        return ResponseEntity.ok().body(dete);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG') or hasRole('ROLE_VASPITAC')")
    @GetMapping("/detes/findByGrupa/{id}")
    public ResponseEntity<List<Dete>> getAllDetesByGrupa(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @PathVariable Long id
    ) {
        if ("nedolasci-is-null".equals(filter)) {
            log.debug("REST request to get all Detes where neDolasci is null");
            return new ResponseEntity<>(deteService.findAllWhereNeDolasciIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Detes");
        Page<Dete> page = deteService.findAllByGrupa(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PreAuthorize("hasRole('ROLE_RODITELJ')")
    @GetMapping("/detes/findByRoditelj/{username}")
    public ResponseEntity<List<Dete>> getAllDetesByGrupa(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @PathVariable String username
    ) {
        if ("nedolasci-is-null".equals(filter)) {
            log.debug("REST request to get all Detes where neDolasci is null");
            return new ResponseEntity<>(deteService.findAllWhereNeDolasciIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Detes");
        Page<Dete> page = deteService.findAllByRoditelj(pageable, username);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PreAuthorize("hasRole('ROLE_VASPITAC')")
    @GetMapping("/detes/findForVaspitac/{username}")
    public ResponseEntity<List<Dete>> getAllDetesForVaspitac(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @PathVariable String username
    ) {
        if ("nedolasci-is-null".equals(filter)) {
            log.debug("REST request to get all Detes where neDolasci is null");
            return new ResponseEntity<>(deteService.findAllWhereNeDolasciIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Detes");
        Page<Dete> page = deteService.findAllForVaspitac(pageable, username);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
