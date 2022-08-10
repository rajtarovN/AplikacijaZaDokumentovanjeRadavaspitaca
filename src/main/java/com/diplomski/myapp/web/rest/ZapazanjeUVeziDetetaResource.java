package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.ZapazanjeUVeziDeteta;
import com.diplomski.myapp.repository.ZapazanjeUVeziDetetaRepository;
import com.diplomski.myapp.service.ZapazanjeUVeziDetetaService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.ZapazanjeUVeziDeteta}.
 */
@RestController
@RequestMapping("/api")
public class ZapazanjeUVeziDetetaResource {

    private final Logger log = LoggerFactory.getLogger(ZapazanjeUVeziDetetaResource.class);

    private static final String ENTITY_NAME = "zapazanjeUVeziDeteta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZapazanjeUVeziDetetaService zapazanjeUVeziDetetaService;

    private final ZapazanjeUVeziDetetaRepository zapazanjeUVeziDetetaRepository;

    public ZapazanjeUVeziDetetaResource(
        ZapazanjeUVeziDetetaService zapazanjeUVeziDetetaService,
        ZapazanjeUVeziDetetaRepository zapazanjeUVeziDetetaRepository
    ) {
        this.zapazanjeUVeziDetetaService = zapazanjeUVeziDetetaService;
        this.zapazanjeUVeziDetetaRepository = zapazanjeUVeziDetetaRepository;
    }

    /**
     * {@code POST  /zapazanje-u-vezi-detetas} : Create a new zapazanjeUVeziDeteta.
     *
     * @param zapazanjeUVeziDeteta the zapazanjeUVeziDeteta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zapazanjeUVeziDeteta, or with status {@code 400 (Bad Request)} if the zapazanjeUVeziDeteta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/zapazanje-u-vezi-detetas")
    public ResponseEntity<ZapazanjeUVeziDeteta> createZapazanjeUVeziDeteta(@RequestBody ZapazanjeUVeziDeteta zapazanjeUVeziDeteta)
        throws URISyntaxException {
        log.debug("REST request to save ZapazanjeUVeziDeteta : {}", zapazanjeUVeziDeteta);
        if (zapazanjeUVeziDeteta.getId() != null) {
            throw new BadRequestAlertException("A new zapazanjeUVeziDeteta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ZapazanjeUVeziDeteta result = zapazanjeUVeziDetetaService.save(zapazanjeUVeziDeteta);
        return ResponseEntity
            .created(new URI("/api/zapazanje-u-vezi-detetas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zapazanje-u-vezi-detetas/:id} : Updates an existing zapazanjeUVeziDeteta.
     *
     * @param id the id of the zapazanjeUVeziDeteta to save.
     * @param zapazanjeUVeziDeteta the zapazanjeUVeziDeteta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zapazanjeUVeziDeteta,
     * or with status {@code 400 (Bad Request)} if the zapazanjeUVeziDeteta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zapazanjeUVeziDeteta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zapazanje-u-vezi-detetas/{id}")
    public ResponseEntity<ZapazanjeUVeziDeteta> updateZapazanjeUVeziDeteta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ZapazanjeUVeziDeteta zapazanjeUVeziDeteta
    ) throws URISyntaxException {
        log.debug("REST request to update ZapazanjeUVeziDeteta : {}, {}", id, zapazanjeUVeziDeteta);
        if (zapazanjeUVeziDeteta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zapazanjeUVeziDeteta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zapazanjeUVeziDetetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ZapazanjeUVeziDeteta result = zapazanjeUVeziDetetaService.update(zapazanjeUVeziDeteta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zapazanjeUVeziDeteta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /zapazanje-u-vezi-detetas/:id} : Partial updates given fields of an existing zapazanjeUVeziDeteta, field will ignore if it is null
     *
     * @param id the id of the zapazanjeUVeziDeteta to save.
     * @param zapazanjeUVeziDeteta the zapazanjeUVeziDeteta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zapazanjeUVeziDeteta,
     * or with status {@code 400 (Bad Request)} if the zapazanjeUVeziDeteta is not valid,
     * or with status {@code 404 (Not Found)} if the zapazanjeUVeziDeteta is not found,
     * or with status {@code 500 (Internal Server Error)} if the zapazanjeUVeziDeteta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/zapazanje-u-vezi-detetas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ZapazanjeUVeziDeteta> partialUpdateZapazanjeUVeziDeteta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ZapazanjeUVeziDeteta zapazanjeUVeziDeteta
    ) throws URISyntaxException {
        log.debug("REST request to partial update ZapazanjeUVeziDeteta partially : {}, {}", id, zapazanjeUVeziDeteta);
        if (zapazanjeUVeziDeteta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zapazanjeUVeziDeteta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zapazanjeUVeziDetetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ZapazanjeUVeziDeteta> result = zapazanjeUVeziDetetaService.partialUpdate(zapazanjeUVeziDeteta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zapazanjeUVeziDeteta.getId().toString())
        );
    }

    /**
     * {@code GET  /zapazanje-u-vezi-detetas} : get all the zapazanjeUVeziDetetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zapazanjeUVeziDetetas in body.
     */
    @GetMapping("/zapazanje-u-vezi-detetas")
    public ResponseEntity<List<ZapazanjeUVeziDeteta>> getAllZapazanjeUVeziDetetas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ZapazanjeUVeziDetetas");
        Page<ZapazanjeUVeziDeteta> page = zapazanjeUVeziDetetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /zapazanje-u-vezi-detetas/:id} : get the "id" zapazanjeUVeziDeteta.
     *
     * @param id the id of the zapazanjeUVeziDeteta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zapazanjeUVeziDeteta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/zapazanje-u-vezi-detetas/{id}")
    public ResponseEntity<ZapazanjeUVeziDeteta> getZapazanjeUVeziDeteta(@PathVariable Long id) {
        log.debug("REST request to get ZapazanjeUVeziDeteta : {}", id);
        Optional<ZapazanjeUVeziDeteta> zapazanjeUVeziDeteta = zapazanjeUVeziDetetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(zapazanjeUVeziDeteta);
    }

    /**
     * {@code DELETE  /zapazanje-u-vezi-detetas/:id} : delete the "id" zapazanjeUVeziDeteta.
     *
     * @param id the id of the zapazanjeUVeziDeteta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/zapazanje-u-vezi-detetas/{id}")
    public ResponseEntity<Void> deleteZapazanjeUVeziDeteta(@PathVariable Long id) {
        log.debug("REST request to delete ZapazanjeUVeziDeteta : {}", id);
        zapazanjeUVeziDetetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/zapazanje-u-vezi-detetas/findByDete/{id}")
    public ResponseEntity<List<ZapazanjeUVeziDeteta>> getAllZapazanjeUVeziDetetas(@PathVariable Long id) {
        log.debug("REST request to get a page of ZapazanjeUVeziDetetas by dete");
        Page<ZapazanjeUVeziDeteta> page = zapazanjeUVeziDetetaService.findByDete(id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
