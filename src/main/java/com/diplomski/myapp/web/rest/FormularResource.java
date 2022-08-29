package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.repository.FormularRepository;
import com.diplomski.myapp.service.FormularService;
import com.diplomski.myapp.web.rest.dto.DeteZaGrupuDTO;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Formular}.
 */
@RestController
@RequestMapping("/api")
public class FormularResource {

    private final Logger log = LoggerFactory.getLogger(FormularResource.class);

    private static final String ENTITY_NAME = "formular";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormularService formularService;

    private final FormularRepository formularRepository;

    public FormularResource(FormularService formularService, FormularRepository formularRepository) {
        this.formularService = formularService;
        this.formularRepository = formularRepository;
    }

    /**
     * {@code POST  /formulars} : Create a new formular.
     *
     * @param formular the formular to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formular, or with status {@code 400 (Bad Request)} if the formular has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formulars/{username}")
    @PreAuthorize("hasRole('ROLE_RODITELJ')")
    public ResponseEntity<Formular> createFormular(@RequestBody Formular formular, @PathVariable String username)
        throws URISyntaxException {
        log.debug("REST request to save Formular : {}", formular);
        if (formular.getId() != null) {
            throw new BadRequestAlertException("A new formular cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Formular result = formularService.save(formular, username);
        return ResponseEntity
            .created(new URI("/api/formulars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formulars/:id} : Updates an existing formular.
     *
     * @param id the id of the formular to save.
     * @param formular the formular to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formular,
     * or with status {@code 400 (Bad Request)} if the formular is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formular couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_RODITELJ') or hasRole('ROLE_PEDAGOG')")
    @PutMapping("/formulars/{id}")
    public ResponseEntity<Formular> updateFormular(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Formular formular
    ) throws URISyntaxException {
        log.debug("REST request to update Formular : {}, {}", id, formular);
        if (formular.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formular.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formularRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Formular result = formularService.update(formular);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formular.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formulars/:id} : Partial updates given fields of an existing formular, field will ignore if it is null
     *
     * @param id the id of the formular to save.
     * @param formular the formular to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formular,
     * or with status {@code 400 (Bad Request)} if the formular is not valid,
     * or with status {@code 404 (Not Found)} if the formular is not found,
     * or with status {@code 500 (Internal Server Error)} if the formular couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_RODITELJ') or hasRole('ROLE_PEDAGOG')")
    @PatchMapping(value = "/formulars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Formular> partialUpdateFormular(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Formular formular
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formular partially : {}, {}", id, formular);
        if (formular.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formular.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formularRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Formular> result = formularService.partialUpdate(formular);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formular.getId().toString())
        );
    }

    /**
     * {@code GET  /formulars} : get all the formulars.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulars in body.
     */
    @GetMapping("/formulars")
    public ResponseEntity<List<Formular>> getAllFormulars(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("dete-is-null".equals(filter)) {
            log.debug("REST request to get all Formulars where dete is null");
            return new ResponseEntity<>(formularService.findAllWhereDeteIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Formulars");
        Page<Formular> page = formularService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formulars/:id} : get the "id" formular.
     *
     * @param id the id of the formular to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formular, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formulars/{id}")
    public ResponseEntity<Formular> getFormular(@PathVariable Long id) {
        log.debug("REST request to get Formular : {}", id);
        Optional<Formular> formular = formularService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formular);
    }

    /**
     * {@code DELETE  /formulars/:id} : delete the "id" formular.
     *
     * @param id the id of the formular to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formulars/{id}")
    public ResponseEntity<Void> deleteFormular(@PathVariable Long id) {
        log.debug("REST request to delete Formular : {}", id);
        formularService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/formulars/getDeca")
    public ResponseEntity<List<DeteZaGrupuDTO>> getAllDecaZaGrupu() {
        List<DeteZaGrupuDTO> listDeca = formularService.findAllDecaZaGrupu();
        return ResponseEntity.ok().body(listDeca);
    }

    @GetMapping("/formulars/findByRoditelj/{username}")
    public ResponseEntity<List<Formular>> getAllFormulars(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @PathVariable String username
    ) {
        if ("dete-is-null".equals(filter)) {
            log.debug("REST request to get all Formulars where dete is null");
            return new ResponseEntity<>(formularService.findAllWhereDeteIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Formulars");
        Page<Formular> page = formularService.findAllByRoditelj(pageable, username);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/formulars/odobri/{id}")
    public ResponseEntity<Formular> approveFormular(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody String status
    ) throws URISyntaxException {
        log.debug("REST request to approve Formular : {}, {}", id);

        if (!formularRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Formular result = formularService.approve(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/formulars/odbij/{id}")
    public ResponseEntity<Formular> rejectFormular(@PathVariable(value = "id", required = false) final Long id, @RequestBody String status)
        throws URISyntaxException {
        log.debug("REST request to reject Formular : {}, {}", id);

        if (!formularRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Formular result = formularService.reject(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
