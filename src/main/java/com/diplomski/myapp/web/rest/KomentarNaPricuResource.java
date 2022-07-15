package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.KomentarNaPricu;
import com.diplomski.myapp.repository.KomentarNaPricuRepository;
import com.diplomski.myapp.service.KomentarNaPricuService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.KomentarNaPricu}.
 */
@RestController
@RequestMapping("/api")
public class KomentarNaPricuResource {

    private final Logger log = LoggerFactory.getLogger(KomentarNaPricuResource.class);

    private static final String ENTITY_NAME = "komentarNaPricu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KomentarNaPricuService komentarNaPricuService;

    private final KomentarNaPricuRepository komentarNaPricuRepository;

    public KomentarNaPricuResource(KomentarNaPricuService komentarNaPricuService, KomentarNaPricuRepository komentarNaPricuRepository) {
        this.komentarNaPricuService = komentarNaPricuService;
        this.komentarNaPricuRepository = komentarNaPricuRepository;
    }

    /**
     * {@code POST  /komentar-na-pricus} : Create a new komentarNaPricu.
     *
     * @param komentarNaPricu the komentarNaPricu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new komentarNaPricu, or with status {@code 400 (Bad Request)} if the komentarNaPricu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/komentar-na-pricus")
    public ResponseEntity<KomentarNaPricu> createKomentarNaPricu(@RequestBody KomentarNaPricu komentarNaPricu) throws URISyntaxException {
        log.debug("REST request to save KomentarNaPricu : {}", komentarNaPricu);
        if (komentarNaPricu.getId() != null) {
            throw new BadRequestAlertException("A new komentarNaPricu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KomentarNaPricu result = komentarNaPricuService.save(komentarNaPricu);
        return ResponseEntity
            .created(new URI("/api/komentar-na-pricus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /komentar-na-pricus/:id} : Updates an existing komentarNaPricu.
     *
     * @param id the id of the komentarNaPricu to save.
     * @param komentarNaPricu the komentarNaPricu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated komentarNaPricu,
     * or with status {@code 400 (Bad Request)} if the komentarNaPricu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the komentarNaPricu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/komentar-na-pricus/{id}")
    public ResponseEntity<KomentarNaPricu> updateKomentarNaPricu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody KomentarNaPricu komentarNaPricu
    ) throws URISyntaxException {
        log.debug("REST request to update KomentarNaPricu : {}, {}", id, komentarNaPricu);
        if (komentarNaPricu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, komentarNaPricu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!komentarNaPricuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KomentarNaPricu result = komentarNaPricuService.update(komentarNaPricu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, komentarNaPricu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /komentar-na-pricus/:id} : Partial updates given fields of an existing komentarNaPricu, field will ignore if it is null
     *
     * @param id the id of the komentarNaPricu to save.
     * @param komentarNaPricu the komentarNaPricu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated komentarNaPricu,
     * or with status {@code 400 (Bad Request)} if the komentarNaPricu is not valid,
     * or with status {@code 404 (Not Found)} if the komentarNaPricu is not found,
     * or with status {@code 500 (Internal Server Error)} if the komentarNaPricu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/komentar-na-pricus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KomentarNaPricu> partialUpdateKomentarNaPricu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody KomentarNaPricu komentarNaPricu
    ) throws URISyntaxException {
        log.debug("REST request to partial update KomentarNaPricu partially : {}, {}", id, komentarNaPricu);
        if (komentarNaPricu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, komentarNaPricu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!komentarNaPricuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KomentarNaPricu> result = komentarNaPricuService.partialUpdate(komentarNaPricu);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, komentarNaPricu.getId().toString())
        );
    }

    /**
     * {@code GET  /komentar-na-pricus} : get all the komentarNaPricus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of komentarNaPricus in body.
     */
    @GetMapping("/komentar-na-pricus")
    public ResponseEntity<List<KomentarNaPricu>> getAllKomentarNaPricus(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of KomentarNaPricus");
        Page<KomentarNaPricu> page = komentarNaPricuService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /komentar-na-pricus/:id} : get the "id" komentarNaPricu.
     *
     * @param id the id of the komentarNaPricu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the komentarNaPricu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/komentar-na-pricus/{id}")
    public ResponseEntity<KomentarNaPricu> getKomentarNaPricu(@PathVariable Long id) {
        log.debug("REST request to get KomentarNaPricu : {}", id);
        Optional<KomentarNaPricu> komentarNaPricu = komentarNaPricuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(komentarNaPricu);
    }

    /**
     * {@code DELETE  /komentar-na-pricus/:id} : delete the "id" komentarNaPricu.
     *
     * @param id the id of the komentarNaPricu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/komentar-na-pricus/{id}")
    public ResponseEntity<Void> deleteKomentarNaPricu(@PathVariable Long id) {
        log.debug("REST request to delete KomentarNaPricu : {}", id);
        komentarNaPricuService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
