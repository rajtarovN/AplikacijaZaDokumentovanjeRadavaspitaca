package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.Direktor;
import com.diplomski.myapp.domain.User;
import com.diplomski.myapp.repository.DirektorRepository;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.service.DirektorService;
import com.diplomski.myapp.service.UserService;
import com.diplomski.myapp.web.rest.errors.BadRequestAlertException;
import com.diplomski.myapp.web.rest.errors.EmailAlreadyUsedException;
import com.diplomski.myapp.web.rest.errors.LoginAlreadyUsedException;
import com.diplomski.myapp.web.rest.vm.ManagedUserVM;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
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
 * REST controller for managing {@link com.diplomski.myapp.domain.Direktor}.
 */
@RestController
@RequestMapping("/api")
public class DirektorResource {

    private final Logger log = LoggerFactory.getLogger(DirektorResource.class);

    private static final String ENTITY_NAME = "direktor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirektorService direktorService;

    private final DirektorRepository direktorRepository;
    private final UserService userService;

    private final UserRepository userRepository;

    public DirektorResource(
        DirektorService direktorService,
        DirektorRepository direktorRepository,
        UserService userService,
        UserRepository userRepository
    ) {
        this.direktorService = direktorService;
        this.direktorRepository = direktorRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /direktors} : Create a new direktor.
     *
     * @param direktor the direktor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new direktor, or with status {@code 400 (Bad Request)} if the direktor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/direktors")
    public ResponseEntity<Direktor> createDirektor(@RequestBody Direktor direktor) throws URISyntaxException {
        log.debug("REST request to save Direktor : {}", direktor);
        if (direktor.getId() != null) {
            throw new BadRequestAlertException("A new direktor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Direktor result = direktorService.save(direktor);
        return ResponseEntity
            .created(new URI("/api/direktors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /direktors/:id} : Updates an existing direktor.
     *
     * @param id the id of the direktor to save.
     * @param direktor the direktor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direktor,
     * or with status {@code 400 (Bad Request)} if the direktor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the direktor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/direktors/{id}")
    public ResponseEntity<Direktor> updateDirektor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Direktor direktor
    ) throws URISyntaxException {
        log.debug("REST request to update Direktor : {}, {}", id, direktor);
        if (direktor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direktor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direktorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Direktor result = direktorService.update(direktor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direktor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /direktors/:id} : Partial updates given fields of an existing direktor, field will ignore if it is null
     *
     * @param id the id of the direktor to save.
     * @param direktor the direktor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direktor,
     * or with status {@code 400 (Bad Request)} if the direktor is not valid,
     * or with status {@code 404 (Not Found)} if the direktor is not found,
     * or with status {@code 500 (Internal Server Error)} if the direktor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/direktors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Direktor> partialUpdateDirektor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Direktor direktor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Direktor partially : {}, {}", id, direktor);
        if (direktor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direktor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direktorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Direktor> result = direktorService.partialUpdate(direktor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direktor.getId().toString())
        );
    }

    /**
     * {@code GET  /direktors} : get all the direktors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of direktors in body.
     */
    @GetMapping("/direktors")
    public ResponseEntity<List<Direktor>> getAllDirektors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Direktors");
        Page<Direktor> page = direktorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /direktors/:id} : get the "id" direktor.
     *
     * @param id the id of the direktor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the direktor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/direktors/{id}")
    public ResponseEntity<Direktor> getDirektor(@PathVariable Long id) {
        log.debug("REST request to get Direktor : {}", id);
        Optional<Direktor> direktor = direktorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(direktor);
    }

    /**
     * {@code DELETE  /direktors/:id} : delete the "id" direktor.
     *
     * @param id the id of the direktor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/direktors/{id}")
    public ResponseEntity<Void> deleteDirektor(@PathVariable Long id) {
        log.debug("REST request to delete Direktor : {}", id);
        direktorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/createDirektor")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        if (managedUserVM.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createZaposlen(managedUserVM, managedUserVM.getPassword());
            return ResponseEntity
                .created(new URI("/api/admin/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }
}
