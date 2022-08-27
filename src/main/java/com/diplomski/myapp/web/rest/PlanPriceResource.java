package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.PlanPrice;
import com.diplomski.myapp.repository.PlanPriceRepository;
import com.diplomski.myapp.service.PlanPriceService;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.PlanPrice}.
 */
@RestController
@RequestMapping("/api")
public class PlanPriceResource {

    private final Logger log = LoggerFactory.getLogger(PlanPriceResource.class);

    private static final String ENTITY_NAME = "planPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanPriceService planPriceService;

    private final PlanPriceRepository planPriceRepository;

    public PlanPriceResource(PlanPriceService planPriceService, PlanPriceRepository planPriceRepository) {
        this.planPriceService = planPriceService;
        this.planPriceRepository = planPriceRepository;
    }

    /**
     * {@code POST  /plan-prices} : Create a new planPrice.
     *
     * @param planPrice the planPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planPrice, or with status {@code 400 (Bad Request)} if the planPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VASPITAC') or hasRole('ROLE_DIREKTOR')")
    @PostMapping("/plan-prices/{username}")
    public ResponseEntity<PlanPrice> createPlanPrice(@RequestBody PlanPrice planPrice, @PathVariable String username)
        throws URISyntaxException {
        log.debug("REST request to save PlanPrice : {}", planPrice);
        if (planPrice.getId() != null) {
            throw new BadRequestAlertException("A new planPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanPrice result = planPriceService.save(planPrice, username);
        return ResponseEntity
            .created(new URI("/api/plan-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plan-prices/:id} : Updates an existing planPrice.
     *
     * @param id the id of the planPrice to save.
     * @param planPrice the planPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planPrice,
     * or with status {@code 400 (Bad Request)} if the planPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VASPITAC') or hasRole('ROLE_DIREKTOR')")
    @PutMapping("/plan-prices/{id}")
    public ResponseEntity<PlanPrice> updatePlanPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanPrice planPrice
    ) throws URISyntaxException {
        log.debug("REST request to update PlanPrice : {}, {}", id, planPrice);
        if (planPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlanPrice result = planPriceService.update(planPrice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planPrice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plan-prices/:id} : Partial updates given fields of an existing planPrice, field will ignore if it is null
     *
     * @param id the id of the planPrice to save.
     * @param planPrice the planPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planPrice,
     * or with status {@code 400 (Bad Request)} if the planPrice is not valid,
     * or with status {@code 404 (Not Found)} if the planPrice is not found,
     * or with status {@code 500 (Internal Server Error)} if the planPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VASPITAC') or hasRole('ROLE_DIREKTOR')")
    @PatchMapping(value = "/plan-prices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanPrice> partialUpdatePlanPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanPrice planPrice
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlanPrice partially : {}, {}", id, planPrice);
        if (planPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanPrice> result = planPriceService.partialUpdate(planPrice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planPrice.getId().toString())
        );
    }

    /**
     * {@code GET  /plan-prices} : get all the planPrices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planPrices in body.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VASPITAC') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG')")
    @GetMapping("/plan-prices")
    public ResponseEntity<List<PlanPrice>> getAllPlanPrices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PlanPrices");
        Page<PlanPrice> page = planPriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plan-prices/:id} : get the "id" planPrice.
     *
     * @param id the id of the planPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planPrice, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VASPITAC') or hasRole('ROLE_DIREKTOR') or hasRole('ROLE_PEDAGOG')")
    @GetMapping("/plan-prices/{id}")
    public ResponseEntity<PlanPrice> getPlanPrice(@PathVariable Long id) {
        log.debug("REST request to get PlanPrice : {}", id);
        Optional<PlanPrice> planPrice = planPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planPrice);
    }

    /**
     * {@code DELETE  /plan-prices/:id} : delete the "id" planPrice.
     *
     * @param id the id of the planPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VASPITAC') ")
    @DeleteMapping("/plan-prices/{id}")
    public ResponseEntity<Void> deletePlanPrice(@PathVariable Long id) {
        log.debug("REST request to delete PlanPrice : {}", id);
        planPriceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
