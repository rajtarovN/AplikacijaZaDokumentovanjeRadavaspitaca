package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Prica;
import com.diplomski.myapp.repository.PricaRepository;
import com.diplomski.myapp.service.PricaService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Prica}.
 */
@RestController
@RequestMapping("/api")
public class PricaResource {

    private final Logger log = LoggerFactory.getLogger(PricaResource.class);

    private static final String ENTITY_NAME = "prica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricaService pricaService;

    private final PricaRepository pricaRepository;

    public PricaResource(PricaService pricaService, PricaRepository pricaRepository) {
        this.pricaService = pricaService;
        this.pricaRepository = pricaRepository;
    }

    /**
     * {@code POST  /pricas} : Create a new prica.
     *
     * @param prica the prica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prica, or with status {@code 400 (Bad Request)} if the prica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pricas")
    public ResponseEntity<Prica> createPrica(@RequestBody Prica prica) throws URISyntaxException {
        log.debug("REST request to save Prica : {}", prica);
        if (prica.getId() != null) {
            throw new BadRequestAlertException("A new prica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prica result = pricaService.save(prica);
        return ResponseEntity
            .created(new URI("/api/pricas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pricas/:id} : Updates an existing prica.
     *
     * @param id the id of the prica to save.
     * @param prica the prica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prica,
     * or with status {@code 400 (Bad Request)} if the prica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pricas/{id}")
    public ResponseEntity<Prica> updatePrica(@PathVariable(value = "id", required = false) final Long id, @RequestBody Prica prica)
        throws URISyntaxException {
        log.debug("REST request to update Prica : {}, {}", id, prica);
        if (prica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prica result = pricaService.update(prica);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prica.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pricas/:id} : Partial updates given fields of an existing prica, field will ignore if it is null
     *
     * @param id the id of the prica to save.
     * @param prica the prica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prica,
     * or with status {@code 400 (Bad Request)} if the prica is not valid,
     * or with status {@code 404 (Not Found)} if the prica is not found,
     * or with status {@code 500 (Internal Server Error)} if the prica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pricas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prica> partialUpdatePrica(@PathVariable(value = "id", required = false) final Long id, @RequestBody Prica prica)
        throws URISyntaxException {
        log.debug("REST request to partial update Prica partially : {}, {}", id, prica);
        if (prica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prica> result = pricaService.partialUpdate(prica);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prica.getId().toString())
        );
    }

    /**
     * {@code GET  /pricas} : get all the pricas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricas in body.
     */
    @GetMapping("/pricas")
    public ResponseEntity<List<Prica>> getAllPricas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pricas");
        Page<Prica> page = pricaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pricas/:id} : get the "id" prica.
     *
     * @param id the id of the prica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pricas/{id}")
    public ResponseEntity<Prica> getPrica(@PathVariable Long id) {
        log.debug("REST request to get Prica : {}", id);
        Optional<Prica> prica = pricaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prica);
    }

    /**
     * {@code DELETE  /pricas/:id} : delete the "id" prica.
     *
     * @param id the id of the prica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pricas/{id}")
    public ResponseEntity<Void> deletePrica(@PathVariable Long id) {
        log.debug("REST request to delete Prica : {}", id);
        pricaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
