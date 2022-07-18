package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Objekat;
import com.diplomski.myapp.repository.ObjekatRepository;
import com.diplomski.myapp.service.ObjekatService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Objekat}.
 */
@RestController
@RequestMapping("/api")
public class ObjekatResource {

    private final Logger log = LoggerFactory.getLogger(ObjekatResource.class);

    private static final String ENTITY_NAME = "objekat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjekatService objekatService;

    private final ObjekatRepository objekatRepository;

    public ObjekatResource(ObjekatService objekatService, ObjekatRepository objekatRepository) {
        this.objekatService = objekatService;
        this.objekatRepository = objekatRepository;
    }

    /**
     * {@code POST  /objekats} : Create a new objekat.
     *
     * @param objekat the objekat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objekat, or with status {@code 400 (Bad Request)} if the objekat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/objekats")
    public ResponseEntity<Objekat> createObjekat(@RequestBody Objekat objekat) throws URISyntaxException {
        log.debug("REST request to save Objekat : {}", objekat);
        if (objekat.getId() != null) {
            throw new BadRequestAlertException("A new objekat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Objekat result = objekatService.save(objekat);
        return ResponseEntity
            .created(new URI("/api/objekats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /objekats/:id} : Updates an existing objekat.
     *
     * @param id the id of the objekat to save.
     * @param objekat the objekat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objekat,
     * or with status {@code 400 (Bad Request)} if the objekat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objekat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/objekats/{id}")
    public ResponseEntity<Objekat> updateObjekat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Objekat objekat)
        throws URISyntaxException {
        log.debug("REST request to update Objekat : {}, {}", id, objekat);
        if (objekat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objekat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objekatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Objekat result = objekatService.update(objekat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, objekat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /objekats/:id} : Partial updates given fields of an existing objekat, field will ignore if it is null
     *
     * @param id the id of the objekat to save.
     * @param objekat the objekat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objekat,
     * or with status {@code 400 (Bad Request)} if the objekat is not valid,
     * or with status {@code 404 (Not Found)} if the objekat is not found,
     * or with status {@code 500 (Internal Server Error)} if the objekat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/objekats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Objekat> partialUpdateObjekat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Objekat objekat
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objekat partially : {}, {}", id, objekat);
        if (objekat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objekat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objekatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Objekat> result = objekatService.partialUpdate(objekat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, objekat.getId().toString())
        );
    }

    /**
     * {@code GET  /objekats} : get all the objekats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objekats in body.
     */
    @GetMapping("/objekats")
    public ResponseEntity<List<Objekat>> getAllObjekats(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Objekats");
        Page<Objekat> page = objekatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /objekats/:id} : get the "id" objekat.
     *
     * @param id the id of the objekat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objekat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/objekats/{id}")
    public ResponseEntity<Objekat> getObjekat(@PathVariable Long id) {
        log.debug("REST request to get Objekat : {}", id);
        Optional<Objekat> objekat = objekatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(objekat);
    }

    /**
     * {@code DELETE  /objekats/:id} : delete the "id" objekat.
     *
     * @param id the id of the objekat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/objekats/{id}")
    public ResponseEntity<Void> deleteObjekat(@PathVariable Long id) {
        log.debug("REST request to delete Objekat : {}", id);
        objekatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/objekats/all")
    public ResponseEntity<List<Objekat>> getAllObjekat() {
        log.debug("REST request to get all Objekat :");
        List<Objekat> objekat = objekatService.findAll();
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), objekat);
        return ResponseEntity.ok().body(objekat);
    }
}
