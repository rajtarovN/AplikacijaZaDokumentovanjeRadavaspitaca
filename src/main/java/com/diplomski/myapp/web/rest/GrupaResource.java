package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Grupa;
import com.diplomski.myapp.repository.GrupaRepository;
import com.diplomski.myapp.service.GrupaService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diplomski.myapp.domain.Grupa}.
 */
@RestController
@RequestMapping("/api")
public class GrupaResource {

    private final Logger log = LoggerFactory.getLogger(GrupaResource.class);

    private static final String ENTITY_NAME = "grupa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrupaService grupaService;

    private final GrupaRepository grupaRepository;

    public GrupaResource(GrupaService grupaService, GrupaRepository grupaRepository) {
        this.grupaService = grupaService;
        this.grupaRepository = grupaRepository;
    }

    /**
     * {@code POST  /grupas} : Create a new grupa.
     *
     * @param grupa the grupa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupa, or with status {@code 400 (Bad Request)} if the grupa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grupas")
    public ResponseEntity<Grupa> createGrupa(@RequestBody Grupa grupa) throws URISyntaxException {
        log.debug("REST request to save Grupa : {}", grupa);
        if (grupa.getId() != null) {
            throw new BadRequestAlertException("A new grupa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Grupa result = grupaService.save(grupa);
        return ResponseEntity
            .created(new URI("/api/grupas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grupas/:id} : Updates an existing grupa.
     *
     * @param id the id of the grupa to save.
     * @param grupa the grupa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupa,
     * or with status {@code 400 (Bad Request)} if the grupa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grupas/{id}")
    public ResponseEntity<Grupa> updateGrupa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Grupa grupa)
        throws URISyntaxException {
        log.debug("REST request to update Grupa : {}, {}", id, grupa);
        if (grupa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Grupa result = grupaService.update(grupa);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grupas/:id} : Partial updates given fields of an existing grupa, field will ignore if it is null
     *
     * @param id the id of the grupa to save.
     * @param grupa the grupa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupa,
     * or with status {@code 400 (Bad Request)} if the grupa is not valid,
     * or with status {@code 404 (Not Found)} if the grupa is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grupas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Grupa> partialUpdateGrupa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Grupa grupa)
        throws URISyntaxException {
        log.debug("REST request to partial update Grupa partially : {}, {}", id, grupa);
        if (grupa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Grupa> result = grupaService.partialUpdate(grupa);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupa.getId().toString())
        );
    }

    /**
     * {@code GET  /grupas} : get all the grupas.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupas in body.
     */
    @GetMapping("/grupas")
    public ResponseEntity<List<Grupa>> getAllGrupas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("dnevnik-is-null".equals(filter)) {
            log.debug("REST request to get all Grupas where dnevnik is null");
            return new ResponseEntity<>(grupaService.findAllWhereDnevnikIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Grupas");
        Page<Grupa> page = grupaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grupas/:id} : get the "id" grupa.
     *
     * @param id the id of the grupa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grupas/{id}")
    public ResponseEntity<Grupa> getGrupa(@PathVariable Long id) {
        log.debug("REST request to get Grupa : {}", id);
        Optional<Grupa> grupa = grupaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(grupa);
    }

    /**
     * {@code DELETE  /grupas/:id} : delete the "id" grupa.
     *
     * @param id the id of the grupa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grupas/{id}")
    public ResponseEntity<Void> deleteGrupa(@PathVariable Long id) {
        log.debug("REST request to delete Grupa : {}", id);
        grupaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
