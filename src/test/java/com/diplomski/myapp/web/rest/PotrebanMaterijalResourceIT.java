package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.PotrebanMaterijal;
import com.diplomski.myapp.domain.enumeration.StatusMaterijala;
import com.diplomski.myapp.repository.PotrebanMaterijalRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PotrebanMaterijalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PotrebanMaterijalResourceIT {

    private static final String DEFAULT_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV = "BBBBBBBBBB";

    private static final Integer DEFAULT_KOLICINA = 1;
    private static final Integer UPDATED_KOLICINA = 2;

    private static final StatusMaterijala DEFAULT_STATUS_MATERIJALA = StatusMaterijala.NOV;
    private static final StatusMaterijala UPDATED_STATUS_MATERIJALA = StatusMaterijala.KUPLJEN;

    private static final String ENTITY_API_URL = "/api/potreban-materijals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PotrebanMaterijalRepository potrebanMaterijalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPotrebanMaterijalMockMvc;

    private PotrebanMaterijal potrebanMaterijal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PotrebanMaterijal createEntity(EntityManager em) {
        PotrebanMaterijal potrebanMaterijal = new PotrebanMaterijal()
            .naziv(DEFAULT_NAZIV)
            .kolicina(DEFAULT_KOLICINA)
            .statusMaterijala(DEFAULT_STATUS_MATERIJALA);
        return potrebanMaterijal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PotrebanMaterijal createUpdatedEntity(EntityManager em) {
        PotrebanMaterijal potrebanMaterijal = new PotrebanMaterijal()
            .naziv(UPDATED_NAZIV)
            .kolicina(UPDATED_KOLICINA)
            .statusMaterijala(UPDATED_STATUS_MATERIJALA);
        return potrebanMaterijal;
    }

    @BeforeEach
    public void initTest() {
        potrebanMaterijal = createEntity(em);
    }

    @Test
    @Transactional
    void createPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeCreate = potrebanMaterijalRepository.findAll().size();
        // Create the PotrebanMaterijal
        restPotrebanMaterijalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isCreated());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeCreate + 1);
        PotrebanMaterijal testPotrebanMaterijal = potrebanMaterijalList.get(potrebanMaterijalList.size() - 1);
        assertThat(testPotrebanMaterijal.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testPotrebanMaterijal.getKolicina()).isEqualTo(DEFAULT_KOLICINA);
        assertThat(testPotrebanMaterijal.getStatusMaterijala()).isEqualTo(DEFAULT_STATUS_MATERIJALA);
    }

    @Test
    @Transactional
    void createPotrebanMaterijalWithExistingId() throws Exception {
        // Create the PotrebanMaterijal with an existing ID
        potrebanMaterijal.setId(1L);

        int databaseSizeBeforeCreate = potrebanMaterijalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPotrebanMaterijalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isBadRequest());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPotrebanMaterijals() throws Exception {
        // Initialize the database
        potrebanMaterijalRepository.saveAndFlush(potrebanMaterijal);

        // Get all the potrebanMaterijalList
        restPotrebanMaterijalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potrebanMaterijal.getId().intValue())))
            .andExpect(jsonPath("$.[*].naziv").value(hasItem(DEFAULT_NAZIV)))
            .andExpect(jsonPath("$.[*].kolicina").value(hasItem(DEFAULT_KOLICINA)))
            .andExpect(jsonPath("$.[*].statusMaterijala").value(hasItem(DEFAULT_STATUS_MATERIJALA.toString())));
    }

    @Test
    @Transactional
    void getPotrebanMaterijal() throws Exception {
        // Initialize the database
        potrebanMaterijalRepository.saveAndFlush(potrebanMaterijal);

        // Get the potrebanMaterijal
        restPotrebanMaterijalMockMvc
            .perform(get(ENTITY_API_URL_ID, potrebanMaterijal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(potrebanMaterijal.getId().intValue()))
            .andExpect(jsonPath("$.naziv").value(DEFAULT_NAZIV))
            .andExpect(jsonPath("$.kolicina").value(DEFAULT_KOLICINA))
            .andExpect(jsonPath("$.statusMaterijala").value(DEFAULT_STATUS_MATERIJALA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPotrebanMaterijal() throws Exception {
        // Get the potrebanMaterijal
        restPotrebanMaterijalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPotrebanMaterijal() throws Exception {
        // Initialize the database
        potrebanMaterijalRepository.saveAndFlush(potrebanMaterijal);

        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();

        // Update the potrebanMaterijal
        PotrebanMaterijal updatedPotrebanMaterijal = potrebanMaterijalRepository.findById(potrebanMaterijal.getId()).get();
        // Disconnect from session so that the updates on updatedPotrebanMaterijal are not directly saved in db
        em.detach(updatedPotrebanMaterijal);
        updatedPotrebanMaterijal.naziv(UPDATED_NAZIV).kolicina(UPDATED_KOLICINA).statusMaterijala(UPDATED_STATUS_MATERIJALA);

        restPotrebanMaterijalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPotrebanMaterijal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPotrebanMaterijal))
            )
            .andExpect(status().isOk());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
        PotrebanMaterijal testPotrebanMaterijal = potrebanMaterijalList.get(potrebanMaterijalList.size() - 1);
        assertThat(testPotrebanMaterijal.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testPotrebanMaterijal.getKolicina()).isEqualTo(UPDATED_KOLICINA);
        assertThat(testPotrebanMaterijal.getStatusMaterijala()).isEqualTo(UPDATED_STATUS_MATERIJALA);
    }

    @Test
    @Transactional
    void putNonExistingPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();
        potrebanMaterijal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotrebanMaterijalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, potrebanMaterijal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isBadRequest());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();
        potrebanMaterijal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotrebanMaterijalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isBadRequest());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();
        potrebanMaterijal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotrebanMaterijalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePotrebanMaterijalWithPatch() throws Exception {
        // Initialize the database
        potrebanMaterijalRepository.saveAndFlush(potrebanMaterijal);

        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();

        // Update the potrebanMaterijal using partial update
        PotrebanMaterijal partialUpdatedPotrebanMaterijal = new PotrebanMaterijal();
        partialUpdatedPotrebanMaterijal.setId(potrebanMaterijal.getId());

        partialUpdatedPotrebanMaterijal.naziv(UPDATED_NAZIV).statusMaterijala(UPDATED_STATUS_MATERIJALA);

        restPotrebanMaterijalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPotrebanMaterijal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPotrebanMaterijal))
            )
            .andExpect(status().isOk());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
        PotrebanMaterijal testPotrebanMaterijal = potrebanMaterijalList.get(potrebanMaterijalList.size() - 1);
        assertThat(testPotrebanMaterijal.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testPotrebanMaterijal.getKolicina()).isEqualTo(DEFAULT_KOLICINA);
        assertThat(testPotrebanMaterijal.getStatusMaterijala()).isEqualTo(UPDATED_STATUS_MATERIJALA);
    }

    @Test
    @Transactional
    void fullUpdatePotrebanMaterijalWithPatch() throws Exception {
        // Initialize the database
        potrebanMaterijalRepository.saveAndFlush(potrebanMaterijal);

        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();

        // Update the potrebanMaterijal using partial update
        PotrebanMaterijal partialUpdatedPotrebanMaterijal = new PotrebanMaterijal();
        partialUpdatedPotrebanMaterijal.setId(potrebanMaterijal.getId());

        partialUpdatedPotrebanMaterijal.naziv(UPDATED_NAZIV).kolicina(UPDATED_KOLICINA).statusMaterijala(UPDATED_STATUS_MATERIJALA);

        restPotrebanMaterijalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPotrebanMaterijal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPotrebanMaterijal))
            )
            .andExpect(status().isOk());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
        PotrebanMaterijal testPotrebanMaterijal = potrebanMaterijalList.get(potrebanMaterijalList.size() - 1);
        assertThat(testPotrebanMaterijal.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testPotrebanMaterijal.getKolicina()).isEqualTo(UPDATED_KOLICINA);
        assertThat(testPotrebanMaterijal.getStatusMaterijala()).isEqualTo(UPDATED_STATUS_MATERIJALA);
    }

    @Test
    @Transactional
    void patchNonExistingPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();
        potrebanMaterijal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotrebanMaterijalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, potrebanMaterijal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isBadRequest());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();
        potrebanMaterijal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotrebanMaterijalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isBadRequest());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPotrebanMaterijal() throws Exception {
        int databaseSizeBeforeUpdate = potrebanMaterijalRepository.findAll().size();
        potrebanMaterijal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotrebanMaterijalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(potrebanMaterijal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PotrebanMaterijal in the database
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePotrebanMaterijal() throws Exception {
        // Initialize the database
        potrebanMaterijalRepository.saveAndFlush(potrebanMaterijal);

        int databaseSizeBeforeDelete = potrebanMaterijalRepository.findAll().size();

        // Delete the potrebanMaterijal
        restPotrebanMaterijalMockMvc
            .perform(delete(ENTITY_API_URL_ID, potrebanMaterijal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PotrebanMaterijal> potrebanMaterijalList = potrebanMaterijalRepository.findAll();
        assertThat(potrebanMaterijalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
