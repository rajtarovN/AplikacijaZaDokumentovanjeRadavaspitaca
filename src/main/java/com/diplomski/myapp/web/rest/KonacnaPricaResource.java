package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.KonacnaPrica;
import com.diplomski.myapp.repository.KonacnaPricaRepository;
import com.diplomski.myapp.repository.PricaRepository;
import com.diplomski.myapp.service.KonacnaPricaService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diplomski.myapp.domain.KonacnaPrica}.
 */
@RestController
@RequestMapping("/api")
public class KonacnaPricaResource {

    private final Logger log = LoggerFactory.getLogger(KonacnaPricaResource.class);

    private static final String ENTITY_NAME = "konacnaPrica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KonacnaPricaService konacnaPricaService;

    private final KonacnaPricaRepository konacnaPricaRepository;

    private final PricaService pricaService;

    private final PricaRepository pricaRepository;

    public KonacnaPricaResource(
        KonacnaPricaService konacnaPricaService,
        KonacnaPricaRepository konacnaPricaRepository,
        PricaService pricaService,
        PricaRepository pricaRepository
    ) {
        this.konacnaPricaService = konacnaPricaService;
        this.konacnaPricaRepository = konacnaPricaRepository;
        this.pricaService = pricaService;
        this.pricaRepository = pricaRepository;
    }

    /**
     * {@code POST  /konacna-pricas} : Create a new konacnaPrica.
     *
     * @param konacnaPrica the konacnaPrica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new konacnaPrica, or with status {@code 400 (Bad Request)} if the konacnaPrica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/konacna-pricas/{id}")
    public ResponseEntity<KonacnaPrica> createKonacnaPrica(@RequestBody KonacnaPrica konacnaPrica, @PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to save KonacnaPrica : {}", konacnaPrica);
        if (konacnaPrica.getId() != null) {
            throw new BadRequestAlertException("A new konacnaPrica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KonacnaPrica result = konacnaPricaService.save(konacnaPrica, id);
        return ResponseEntity
            .created(new URI("/api/konacna-pricas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /konacna-pricas/:id} : Updates an existing konacnaPrica.
     *
     * @param id the id of the konacnaPrica to save.
     * @param konacnaPrica the konacnaPrica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated konacnaPrica,
     * or with status {@code 400 (Bad Request)} if the konacnaPrica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the konacnaPrica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/konacna-pricas/{id}")
    public ResponseEntity<KonacnaPrica> updateKonacnaPrica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody KonacnaPrica konacnaPrica
    ) throws URISyntaxException {
        log.debug("REST request to update KonacnaPrica : {}, {}", id, konacnaPrica);
        if (konacnaPrica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, konacnaPrica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!konacnaPricaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KonacnaPrica result = konacnaPricaService.update(konacnaPrica);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, konacnaPrica.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /konacna-pricas/:id} : Partial updates given fields of an existing konacnaPrica, field will ignore if it is null
     *
     * @param id the id of the konacnaPrica to save.
     * @param konacnaPrica the konacnaPrica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated konacnaPrica,
     * or with status {@code 400 (Bad Request)} if the konacnaPrica is not valid,
     * or with status {@code 404 (Not Found)} if the konacnaPrica is not found,
     * or with status {@code 500 (Internal Server Error)} if the konacnaPrica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/konacna-pricas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KonacnaPrica> partialUpdateKonacnaPrica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody KonacnaPrica konacnaPrica
    ) throws URISyntaxException {
        log.debug("REST request to partial update KonacnaPrica partially : {}, {}", id, konacnaPrica);
        if (konacnaPrica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, konacnaPrica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!konacnaPricaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KonacnaPrica> result = konacnaPricaService.partialUpdate(konacnaPrica);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, konacnaPrica.getId().toString())
        );
    }

    /**
     * {@code GET  /konacna-pricas} : get all the konacnaPricas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of konacnaPricas in body.
     */
    @GetMapping("/konacna-pricas")
    public ResponseEntity<List<KonacnaPrica>> getAllKonacnaPricas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of KonacnaPricas");
        Page<KonacnaPrica> page = konacnaPricaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /konacna-pricas/:id} : get the "id" konacnaPrica.
     *
     * @param id the id of the konacnaPrica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the konacnaPrica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/konacna-pricas/{id}")
    public ResponseEntity<KonacnaPrica> getKonacnaPrica(@PathVariable Long id) {
        log.debug("REST request to get KonacnaPrica : {}", id);
        Optional<KonacnaPrica> konacnaPrica = konacnaPricaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(konacnaPrica);
    }

    /**
     * {@code DELETE  /konacna-pricas/:id} : delete the "id" konacnaPrica.
     *
     * @param id the id of the konacnaPrica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/konacna-pricas/{id}")
    public ResponseEntity<Void> deleteKonacnaPrica(@PathVariable Long id) {
        log.debug("REST request to delete KonacnaPrica : {}", id);
        konacnaPricaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/konacna-pricas/getPocetnaPrica/{id}")
    public ResponseEntity<KonacnaPrica> getPocetnaPrica(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id
    ) {
        log.debug("REST request to get a page of KonacnaPricas");
        KonacnaPrica prica = this.pricaService.startWriting(id);
        return ResponseEntity.ok(prica);
    }
}
