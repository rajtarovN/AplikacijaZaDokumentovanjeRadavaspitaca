package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.NeDolasci;
import com.diplomski.myapp.repository.NeDolasciRepository;
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
 * Integration tests for the {@link NeDolasciResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NeDolasciResourceIT {

    private static final LocalDate DEFAULT_DATUM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_RAZLOG = "AAAAAAAAAA";
    private static final String UPDATED_RAZLOG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ne-dolascis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NeDolasciRepository neDolasciRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNeDolasciMockMvc;

    private NeDolasci neDolasci;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NeDolasci createEntity(EntityManager em) {
        NeDolasci neDolasci = new NeDolasci().datum(DEFAULT_DATUM).razlog(DEFAULT_RAZLOG);
        return neDolasci;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NeDolasci createUpdatedEntity(EntityManager em) {
        NeDolasci neDolasci = new NeDolasci().datum(UPDATED_DATUM).razlog(UPDATED_RAZLOG);
        return neDolasci;
    }

    @BeforeEach
    public void initTest() {
        neDolasci = createEntity(em);
    }

    @Test
    @Transactional
    void createNeDolasci() throws Exception {
        int databaseSizeBeforeCreate = neDolasciRepository.findAll().size();
        // Create the NeDolasci
        restNeDolasciMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(neDolasci)))
            .andExpect(status().isCreated());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeCreate + 1);
        NeDolasci testNeDolasci = neDolasciList.get(neDolasciList.size() - 1);
        assertThat(testNeDolasci.getDatum()).isEqualTo(DEFAULT_DATUM);
        assertThat(testNeDolasci.getRazlog()).isEqualTo(DEFAULT_RAZLOG);
    }

    @Test
    @Transactional
    void createNeDolasciWithExistingId() throws Exception {
        // Create the NeDolasci with an existing ID
        neDolasci.setId(1L);

        int databaseSizeBeforeCreate = neDolasciRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNeDolasciMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(neDolasci)))
            .andExpect(status().isBadRequest());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNeDolascis() throws Exception {
        // Initialize the database
        neDolasciRepository.saveAndFlush(neDolasci);

        // Get all the neDolasciList
        restNeDolasciMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(neDolasci.getId().intValue())))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())))
            .andExpect(jsonPath("$.[*].razlog").value(hasItem(DEFAULT_RAZLOG)));
    }

    @Test
    @Transactional
    void getNeDolasci() throws Exception {
        // Initialize the database
        neDolasciRepository.saveAndFlush(neDolasci);

        // Get the neDolasci
        restNeDolasciMockMvc
            .perform(get(ENTITY_API_URL_ID, neDolasci.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(neDolasci.getId().intValue()))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()))
            .andExpect(jsonPath("$.razlog").value(DEFAULT_RAZLOG));
    }

    @Test
    @Transactional
    void getNonExistingNeDolasci() throws Exception {
        // Get the neDolasci
        restNeDolasciMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNeDolasci() throws Exception {
        // Initialize the database
        neDolasciRepository.saveAndFlush(neDolasci);

        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();

        // Update the neDolasci
        NeDolasci updatedNeDolasci = neDolasciRepository.findById(neDolasci.getId()).get();
        // Disconnect from session so that the updates on updatedNeDolasci are not directly saved in db
        em.detach(updatedNeDolasci);
        updatedNeDolasci.datum(UPDATED_DATUM).razlog(UPDATED_RAZLOG);

        restNeDolasciMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNeDolasci.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNeDolasci))
            )
            .andExpect(status().isOk());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
        NeDolasci testNeDolasci = neDolasciList.get(neDolasciList.size() - 1);
        assertThat(testNeDolasci.getDatum()).isEqualTo(UPDATED_DATUM);
        assertThat(testNeDolasci.getRazlog()).isEqualTo(UPDATED_RAZLOG);
    }

    @Test
    @Transactional
    void putNonExistingNeDolasci() throws Exception {
        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();
        neDolasci.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNeDolasciMockMvc
            .perform(
                put(ENTITY_API_URL_ID, neDolasci.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(neDolasci))
            )
            .andExpect(status().isBadRequest());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNeDolasci() throws Exception {
        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();
        neDolasci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeDolasciMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(neDolasci))
            )
            .andExpect(status().isBadRequest());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNeDolasci() throws Exception {
        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();
        neDolasci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeDolasciMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(neDolasci)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNeDolasciWithPatch() throws Exception {
        // Initialize the database
        neDolasciRepository.saveAndFlush(neDolasci);

        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();

        // Update the neDolasci using partial update
        NeDolasci partialUpdatedNeDolasci = new NeDolasci();
        partialUpdatedNeDolasci.setId(neDolasci.getId());

        partialUpdatedNeDolasci.datum(UPDATED_DATUM).razlog(UPDATED_RAZLOG);

        restNeDolasciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNeDolasci.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNeDolasci))
            )
            .andExpect(status().isOk());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
        NeDolasci testNeDolasci = neDolasciList.get(neDolasciList.size() - 1);
        assertThat(testNeDolasci.getDatum()).isEqualTo(UPDATED_DATUM);
        assertThat(testNeDolasci.getRazlog()).isEqualTo(UPDATED_RAZLOG);
    }

    @Test
    @Transactional
    void fullUpdateNeDolasciWithPatch() throws Exception {
        // Initialize the database
        neDolasciRepository.saveAndFlush(neDolasci);

        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();

        // Update the neDolasci using partial update
        NeDolasci partialUpdatedNeDolasci = new NeDolasci();
        partialUpdatedNeDolasci.setId(neDolasci.getId());

        partialUpdatedNeDolasci.datum(UPDATED_DATUM).razlog(UPDATED_RAZLOG);

        restNeDolasciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNeDolasci.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNeDolasci))
            )
            .andExpect(status().isOk());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
        NeDolasci testNeDolasci = neDolasciList.get(neDolasciList.size() - 1);
        assertThat(testNeDolasci.getDatum()).isEqualTo(UPDATED_DATUM);
        assertThat(testNeDolasci.getRazlog()).isEqualTo(UPDATED_RAZLOG);
    }

    @Test
    @Transactional
    void patchNonExistingNeDolasci() throws Exception {
        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();
        neDolasci.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNeDolasciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, neDolasci.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(neDolasci))
            )
            .andExpect(status().isBadRequest());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNeDolasci() throws Exception {
        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();
        neDolasci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeDolasciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(neDolasci))
            )
            .andExpect(status().isBadRequest());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNeDolasci() throws Exception {
        int databaseSizeBeforeUpdate = neDolasciRepository.findAll().size();
        neDolasci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeDolasciMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(neDolasci))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NeDolasci in the database
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNeDolasci() throws Exception {
        // Initialize the database
        neDolasciRepository.saveAndFlush(neDolasci);

        int databaseSizeBeforeDelete = neDolasciRepository.findAll().size();

        // Delete the neDolasci
        restNeDolasciMockMvc
            .perform(delete(ENTITY_API_URL_ID, neDolasci.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NeDolasci> neDolasciList = neDolasciRepository.findAll();
        assertThat(neDolasciList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
