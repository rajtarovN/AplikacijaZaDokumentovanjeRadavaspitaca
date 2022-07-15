package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.repository.VaspitacRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link VaspitacResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaspitacResourceIT {

    private static final LocalDate DEFAULT_DATUM_ZAPOSLENJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_ZAPOSLENJA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_GODINE_ISKUSTVA = 1;
    private static final Integer UPDATED_GODINE_ISKUSTVA = 2;

    private static final String DEFAULT_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_OPIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vaspitacs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaspitacRepository vaspitacRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaspitacMockMvc;

    private Vaspitac vaspitac;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaspitac createEntity(EntityManager em) {
        Vaspitac vaspitac = new Vaspitac()
            .datumZaposlenja(DEFAULT_DATUM_ZAPOSLENJA)
            .godineIskustva(DEFAULT_GODINE_ISKUSTVA)
            .opis(DEFAULT_OPIS);
        return vaspitac;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaspitac createUpdatedEntity(EntityManager em) {
        Vaspitac vaspitac = new Vaspitac()
            .datumZaposlenja(UPDATED_DATUM_ZAPOSLENJA)
            .godineIskustva(UPDATED_GODINE_ISKUSTVA)
            .opis(UPDATED_OPIS);
        return vaspitac;
    }

    @BeforeEach
    public void initTest() {
        vaspitac = createEntity(em);
    }

    @Test
    @Transactional
    void createVaspitac() throws Exception {
        int databaseSizeBeforeCreate = vaspitacRepository.findAll().size();
        // Create the Vaspitac
        restVaspitacMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaspitac)))
            .andExpect(status().isCreated());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeCreate + 1);
        Vaspitac testVaspitac = vaspitacList.get(vaspitacList.size() - 1);
        assertThat(testVaspitac.getDatumZaposlenja()).isEqualTo(DEFAULT_DATUM_ZAPOSLENJA);
        assertThat(testVaspitac.getGodineIskustva()).isEqualTo(DEFAULT_GODINE_ISKUSTVA);
        assertThat(testVaspitac.getOpis()).isEqualTo(DEFAULT_OPIS);
    }

    @Test
    @Transactional
    void createVaspitacWithExistingId() throws Exception {
        // Create the Vaspitac with an existing ID
        vaspitac.setId(1L);

        int databaseSizeBeforeCreate = vaspitacRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaspitacMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaspitac)))
            .andExpect(status().isBadRequest());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVaspitacs() throws Exception {
        // Initialize the database
        vaspitacRepository.saveAndFlush(vaspitac);

        // Get all the vaspitacList
        restVaspitacMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaspitac.getId().intValue())))
            .andExpect(jsonPath("$.[*].datumZaposlenja").value(hasItem(DEFAULT_DATUM_ZAPOSLENJA.toString())))
            .andExpect(jsonPath("$.[*].godineIskustva").value(hasItem(DEFAULT_GODINE_ISKUSTVA)))
            .andExpect(jsonPath("$.[*].opis").value(hasItem(DEFAULT_OPIS)));
    }

    @Test
    @Transactional
    void getVaspitac() throws Exception {
        // Initialize the database
        vaspitacRepository.saveAndFlush(vaspitac);

        // Get the vaspitac
        restVaspitacMockMvc
            .perform(get(ENTITY_API_URL_ID, vaspitac.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaspitac.getId().intValue()))
            .andExpect(jsonPath("$.datumZaposlenja").value(DEFAULT_DATUM_ZAPOSLENJA.toString()))
            .andExpect(jsonPath("$.godineIskustva").value(DEFAULT_GODINE_ISKUSTVA))
            .andExpect(jsonPath("$.opis").value(DEFAULT_OPIS));
    }

    @Test
    @Transactional
    void getNonExistingVaspitac() throws Exception {
        // Get the vaspitac
        restVaspitacMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaspitac() throws Exception {
        // Initialize the database
        vaspitacRepository.saveAndFlush(vaspitac);

        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();

        // Update the vaspitac
        Vaspitac updatedVaspitac = vaspitacRepository.findById(vaspitac.getId()).get();
        // Disconnect from session so that the updates on updatedVaspitac are not directly saved in db
        em.detach(updatedVaspitac);
        updatedVaspitac.datumZaposlenja(UPDATED_DATUM_ZAPOSLENJA).godineIskustva(UPDATED_GODINE_ISKUSTVA).opis(UPDATED_OPIS);

        restVaspitacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVaspitac.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVaspitac))
            )
            .andExpect(status().isOk());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
        Vaspitac testVaspitac = vaspitacList.get(vaspitacList.size() - 1);
        assertThat(testVaspitac.getDatumZaposlenja()).isEqualTo(UPDATED_DATUM_ZAPOSLENJA);
        assertThat(testVaspitac.getGodineIskustva()).isEqualTo(UPDATED_GODINE_ISKUSTVA);
        assertThat(testVaspitac.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void putNonExistingVaspitac() throws Exception {
        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();
        vaspitac.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaspitacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaspitac.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaspitac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaspitac() throws Exception {
        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();
        vaspitac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaspitacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaspitac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaspitac() throws Exception {
        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();
        vaspitac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaspitacMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaspitac)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaspitacWithPatch() throws Exception {
        // Initialize the database
        vaspitacRepository.saveAndFlush(vaspitac);

        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();

        // Update the vaspitac using partial update
        Vaspitac partialUpdatedVaspitac = new Vaspitac();
        partialUpdatedVaspitac.setId(vaspitac.getId());

        partialUpdatedVaspitac.datumZaposlenja(UPDATED_DATUM_ZAPOSLENJA).godineIskustva(UPDATED_GODINE_ISKUSTVA);

        restVaspitacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaspitac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaspitac))
            )
            .andExpect(status().isOk());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
        Vaspitac testVaspitac = vaspitacList.get(vaspitacList.size() - 1);
        assertThat(testVaspitac.getDatumZaposlenja()).isEqualTo(UPDATED_DATUM_ZAPOSLENJA);
        assertThat(testVaspitac.getGodineIskustva()).isEqualTo(UPDATED_GODINE_ISKUSTVA);
        assertThat(testVaspitac.getOpis()).isEqualTo(DEFAULT_OPIS);
    }

    @Test
    @Transactional
    void fullUpdateVaspitacWithPatch() throws Exception {
        // Initialize the database
        vaspitacRepository.saveAndFlush(vaspitac);

        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();

        // Update the vaspitac using partial update
        Vaspitac partialUpdatedVaspitac = new Vaspitac();
        partialUpdatedVaspitac.setId(vaspitac.getId());

        partialUpdatedVaspitac.datumZaposlenja(UPDATED_DATUM_ZAPOSLENJA).godineIskustva(UPDATED_GODINE_ISKUSTVA).opis(UPDATED_OPIS);

        restVaspitacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaspitac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaspitac))
            )
            .andExpect(status().isOk());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
        Vaspitac testVaspitac = vaspitacList.get(vaspitacList.size() - 1);
        assertThat(testVaspitac.getDatumZaposlenja()).isEqualTo(UPDATED_DATUM_ZAPOSLENJA);
        assertThat(testVaspitac.getGodineIskustva()).isEqualTo(UPDATED_GODINE_ISKUSTVA);
        assertThat(testVaspitac.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void patchNonExistingVaspitac() throws Exception {
        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();
        vaspitac.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaspitacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaspitac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaspitac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaspitac() throws Exception {
        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();
        vaspitac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaspitacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaspitac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaspitac() throws Exception {
        int databaseSizeBeforeUpdate = vaspitacRepository.findAll().size();
        vaspitac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaspitacMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vaspitac)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaspitac in the database
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaspitac() throws Exception {
        // Initialize the database
        vaspitacRepository.saveAndFlush(vaspitac);

        int databaseSizeBeforeDelete = vaspitacRepository.findAll().size();

        // Delete the vaspitac
        restVaspitacMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaspitac.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vaspitac> vaspitacList = vaspitacRepository.findAll();
        assertThat(vaspitacList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
