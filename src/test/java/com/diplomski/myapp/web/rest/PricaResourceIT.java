package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Prica;
import com.diplomski.myapp.repository.PricaRepository;
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
 * Integration tests for the {@link PricaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PricaResourceIT {

    private static final String ENTITY_API_URL = "/api/pricas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PricaRepository pricaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPricaMockMvc;

    private Prica prica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prica createEntity(EntityManager em) {
        Prica prica = new Prica();
        return prica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prica createUpdatedEntity(EntityManager em) {
        Prica prica = new Prica();
        return prica;
    }

    @BeforeEach
    public void initTest() {
        prica = createEntity(em);
    }

    @Test
    @Transactional
    void createPrica() throws Exception {
        int databaseSizeBeforeCreate = pricaRepository.findAll().size();
        // Create the Prica
        restPricaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prica)))
            .andExpect(status().isCreated());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeCreate + 1);
        Prica testPrica = pricaList.get(pricaList.size() - 1);
    }

    @Test
    @Transactional
    void createPricaWithExistingId() throws Exception {
        // Create the Prica with an existing ID
        prica.setId(1L);

        int databaseSizeBeforeCreate = pricaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prica)))
            .andExpect(status().isBadRequest());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPricas() throws Exception {
        // Initialize the database
        pricaRepository.saveAndFlush(prica);

        // Get all the pricaList
        restPricaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prica.getId().intValue())));
    }

    @Test
    @Transactional
    void getPrica() throws Exception {
        // Initialize the database
        pricaRepository.saveAndFlush(prica);

        // Get the prica
        restPricaMockMvc
            .perform(get(ENTITY_API_URL_ID, prica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prica.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrica() throws Exception {
        // Get the prica
        restPricaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrica() throws Exception {
        // Initialize the database
        pricaRepository.saveAndFlush(prica);

        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();

        // Update the prica
        Prica updatedPrica = pricaRepository.findById(prica.getId()).get();
        // Disconnect from session so that the updates on updatedPrica are not directly saved in db
        em.detach(updatedPrica);

        restPricaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrica))
            )
            .andExpect(status().isOk());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
        Prica testPrica = pricaList.get(pricaList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPrica() throws Exception {
        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();
        prica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrica() throws Exception {
        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();
        prica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrica() throws Exception {
        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();
        prica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePricaWithPatch() throws Exception {
        // Initialize the database
        pricaRepository.saveAndFlush(prica);

        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();

        // Update the prica using partial update
        Prica partialUpdatedPrica = new Prica();
        partialUpdatedPrica.setId(prica.getId());

        restPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrica))
            )
            .andExpect(status().isOk());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
        Prica testPrica = pricaList.get(pricaList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePricaWithPatch() throws Exception {
        // Initialize the database
        pricaRepository.saveAndFlush(prica);

        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();

        // Update the prica using partial update
        Prica partialUpdatedPrica = new Prica();
        partialUpdatedPrica.setId(prica.getId());

        restPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrica))
            )
            .andExpect(status().isOk());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
        Prica testPrica = pricaList.get(pricaList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPrica() throws Exception {
        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();
        prica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrica() throws Exception {
        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();
        prica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrica() throws Exception {
        int databaseSizeBeforeUpdate = pricaRepository.findAll().size();
        prica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prica in the database
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrica() throws Exception {
        // Initialize the database
        pricaRepository.saveAndFlush(prica);

        int databaseSizeBeforeDelete = pricaRepository.findAll().size();

        // Delete the prica
        restPricaMockMvc
            .perform(delete(ENTITY_API_URL_ID, prica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prica> pricaList = pricaRepository.findAll();
        assertThat(pricaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
