package com.diplomski.myapp.web.rest;

import com.diplomski.myapp.domain.User;
import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.repository.VaspitacRepository;
import com.diplomski.myapp.service.UserService;
import com.diplomski.myapp.service.VaspitacService;
import com.diplomski.myapp.web.rest.dto.VaspitacDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diplomski.myapp.domain.Vaspitac}.
 */
@RestController
@RequestMapping("/api")
public class VaspitacResource {

    private final Logger log = LoggerFactory.getLogger(VaspitacResource.class);

    private static final String ENTITY_NAME = "vaspitac";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaspitacService vaspitacService;

    private final VaspitacRepository vaspitacRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    public VaspitacResource(
        VaspitacService vaspitacService,
        VaspitacRepository vaspitacRepository,
        UserService userService,
        UserRepository userRepository
    ) {
        this.vaspitacService = vaspitacService;
        this.vaspitacRepository = vaspitacRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /vaspitacs} : Create a new vaspitac.
     *
     * @param vaspitac the vaspitac to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaspitac, or with status {@code 400 (Bad Request)} if the vaspitac has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR') ")
    @PostMapping("/vaspitacs")
    public ResponseEntity<Vaspitac> createVaspitac(@RequestBody Vaspitac vaspitac) throws URISyntaxException {
        log.debug("REST request to save Vaspitac : {}", vaspitac);
        if (vaspitac.getId() != null) {
            throw new BadRequestAlertException("A new vaspitac cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vaspitac result = vaspitacService.save(vaspitac);
        return ResponseEntity
            .created(new URI("/api/vaspitacs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaspitacs/:id} : Updates an existing vaspitac.
     *
     * @param id the id of the vaspitac to save.
     * @param vaspitac the vaspitac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaspitac,
     * or with status {@code 400 (Bad Request)} if the vaspitac is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaspitac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC')")
    @PutMapping("/vaspitacs/{id}")
    public ResponseEntity<Vaspitac> updateVaspitac(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vaspitac vaspitac
    ) throws URISyntaxException {
        log.debug("REST request to update Vaspitac : {}, {}", id, vaspitac);
        if (vaspitac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaspitac.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaspitacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vaspitac result = vaspitacService.update(vaspitac);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaspitac.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaspitacs/:id} : Partial updates given fields of an existing vaspitac, field will ignore if it is null
     *
     * @param id the id of the vaspitac to save.
     * @param vaspitac the vaspitac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaspitac,
     * or with status {@code 400 (Bad Request)} if the vaspitac is not valid,
     * or with status {@code 404 (Not Found)} if the vaspitac is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaspitac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC')")
    @PatchMapping(value = "/vaspitacs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vaspitac> partialUpdateVaspitac(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vaspitac vaspitac
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vaspitac partially : {}, {}", id, vaspitac);
        if (vaspitac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaspitac.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaspitacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vaspitac> result = vaspitacService.partialUpdate(vaspitac);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaspitac.getId().toString())
        );
    }

    /**
     * {@code GET  /vaspitacs} : get all the vaspitacs.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaspitacs in body.
     */
    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC') or hasRole('ROLE_PEDAGOG')  or hasRole('ROLE_RODITELJ')"
    )
    @GetMapping("/vaspitacs")
    public ResponseEntity<List<Vaspitac>> getAllVaspitacs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("zapazanjeuvezideteta-is-null".equals(filter)) {
            log.debug("REST request to get all Vaspitacs where zapazanjeUVeziDeteta is null");
            return new ResponseEntity<>(vaspitacService.findAllWhereZapazanjeUVeziDetetaIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Vaspitacs");
        Page<Vaspitac> page = vaspitacService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaspitacs/:id} : get the "id" vaspitac.
     *
     * @param id the id of the vaspitac to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaspitac, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC') or hasRole('ROLE_PEDAGOG')  or hasRole('ROLE_RODITELJ')"
    )
    @GetMapping("/vaspitacs/{id}")
    public ResponseEntity<Vaspitac> getVaspitac(@PathVariable Long id) {
        log.debug("REST request to get Vaspitac : {}", id);
        Optional<Vaspitac> vaspitac = vaspitacService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaspitac);
    }

    /**
     * {@code DELETE  /vaspitacs/:id} : delete the "id" vaspitac.
     *
     * @param id the id of the vaspitac to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC')")
    @DeleteMapping("/vaspitacs/{id}")
    public ResponseEntity<Void> deleteVaspitac(@PathVariable Long id) {
        log.debug("REST request to delete Vaspitac : {}", id);
        vaspitacService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC') or hasRole('ROLE_PEDAGOG')  or hasRole('ROLE_RODITELJ')"
    )
    @GetMapping("/vaspitacs/getByObjekat/{id}")
    public ResponseEntity<List<Vaspitac>> getByObjekat(@PathVariable Long id) {
        log.debug("REST request to get Vaspitac : {}", id);
        List<Vaspitac> vaspitac = vaspitacService.getByObjekat(id);
        return ResponseEntity.ok().body(vaspitac);
    }

    @PreAuthorize(
        "hasRole('ROLE_ADMIN') or hasRole('ROLE_DIREKTOR')  or hasRole('ROLE_VASPITAC') or hasRole('ROLE_PEDAGOG')  or hasRole('ROLE_RODITELJ')"
    )
    @GetMapping("/vaspitacs/getImena")
    public ResponseEntity<List<VaspitacDTO>> getImena() {
        log.debug("REST request to get imena Vaspitac");
        List<VaspitacDTO> vaspitac = vaspitacService.getImena();
        return ResponseEntity.ok().body(vaspitac);
    }

    @PostMapping("/createVaspitac")
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

    @PostMapping("/changeStatus/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable Long id, @RequestBody String status) {
        log.debug("REST request to change status Vaspitac : {}", id);

        String ret = vaspitacService.changeStatus(id, status);
        return ResponseEntity.ok().body(ret);
    }
}
