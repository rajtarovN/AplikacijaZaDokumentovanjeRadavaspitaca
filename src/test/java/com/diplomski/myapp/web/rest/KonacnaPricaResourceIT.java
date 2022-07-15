package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.KonacnaPrica;
import com.diplomski.myapp.repository.KonacnaPricaRepository;
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
 * Integration tests for the {@link KonacnaPricaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KonacnaPricaResourceIT {

    private static final String DEFAULT_TEKST = "AAAAAAAAAA";
    private static final String UPDATED_TEKST = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/konacna-pricas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KonacnaPricaRepository konacnaPricaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKonacnaPricaMockMvc;

    private KonacnaPrica konacnaPrica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KonacnaPrica createEntity(EntityManager em) {
        KonacnaPrica konacnaPrica = new KonacnaPrica().tekst(DEFAULT_TEKST);
        return konacnaPrica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KonacnaPrica createUpdatedEntity(EntityManager em) {
        KonacnaPrica konacnaPrica = new KonacnaPrica().tekst(UPDATED_TEKST);
        return konacnaPrica;
    }

    @BeforeEach
    public void initTest() {
        konacnaPrica = createEntity(em);
    }

    @Test
    @Transactional
    void createKonacnaPrica() throws Exception {
        int databaseSizeBeforeCreate = konacnaPricaRepository.findAll().size();
        // Create the KonacnaPrica
        restKonacnaPricaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(konacnaPrica)))
            .andExpect(status().isCreated());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeCreate + 1);
        KonacnaPrica testKonacnaPrica = konacnaPricaList.get(konacnaPricaList.size() - 1);
        assertThat(testKonacnaPrica.getTekst()).isEqualTo(DEFAULT_TEKST);
    }

    @Test
    @Transactional
    void createKonacnaPricaWithExistingId() throws Exception {
        // Create the KonacnaPrica with an existing ID
        konacnaPrica.setId(1L);

        int databaseSizeBeforeCreate = konacnaPricaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKonacnaPricaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(konacnaPrica)))
            .andExpect(status().isBadRequest());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKonacnaPricas() throws Exception {
        // Initialize the database
        konacnaPricaRepository.saveAndFlush(konacnaPrica);

        // Get all the konacnaPricaList
        restKonacnaPricaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(konacnaPrica.getId().intValue())))
            .andExpect(jsonPath("$.[*].tekst").value(hasItem(DEFAULT_TEKST)));
    }

    @Test
    @Transactional
    void getKonacnaPrica() throws Exception {
        // Initialize the database
        konacnaPricaRepository.saveAndFlush(konacnaPrica);

        // Get the konacnaPrica
        restKonacnaPricaMockMvc
            .perform(get(ENTITY_API_URL_ID, konacnaPrica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(konacnaPrica.getId().intValue()))
            .andExpect(jsonPath("$.tekst").value(DEFAULT_TEKST));
    }

    @Test
    @Transactional
    void getNonExistingKonacnaPrica() throws Exception {
        // Get the konacnaPrica
        restKonacnaPricaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKonacnaPrica() throws Exception {
        // Initialize the database
        konacnaPricaRepository.saveAndFlush(konacnaPrica);

        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();

        // Update the konacnaPrica
        KonacnaPrica updatedKonacnaPrica = konacnaPricaRepository.findById(konacnaPrica.getId()).get();
        // Disconnect from session so that the updates on updatedKonacnaPrica are not directly saved in db
        em.detach(updatedKonacnaPrica);
        updatedKonacnaPrica.tekst(UPDATED_TEKST);

        restKonacnaPricaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKonacnaPrica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKonacnaPrica))
            )
            .andExpect(status().isOk());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
        KonacnaPrica testKonacnaPrica = konacnaPricaList.get(konacnaPricaList.size() - 1);
        assertThat(testKonacnaPrica.getTekst()).isEqualTo(UPDATED_TEKST);
    }

    @Test
    @Transactional
    void putNonExistingKonacnaPrica() throws Exception {
        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();
        konacnaPrica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKonacnaPricaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, konacnaPrica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(konacnaPrica))
            )
            .andExpect(status().isBadRequest());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKonacnaPrica() throws Exception {
        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();
        konacnaPrica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKonacnaPricaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(konacnaPrica))
            )
            .andExpect(status().isBadRequest());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKonacnaPrica() throws Exception {
        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();
        konacnaPrica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKonacnaPricaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(konacnaPrica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKonacnaPricaWithPatch() throws Exception {
        // Initialize the database
        konacnaPricaRepository.saveAndFlush(konacnaPrica);

        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();

        // Update the konacnaPrica using partial update
        KonacnaPrica partialUpdatedKonacnaPrica = new KonacnaPrica();
        partialUpdatedKonacnaPrica.setId(konacnaPrica.getId());

        partialUpdatedKonacnaPrica.tekst(UPDATED_TEKST);

        restKonacnaPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKonacnaPrica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKonacnaPrica))
            )
            .andExpect(status().isOk());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
        KonacnaPrica testKonacnaPrica = konacnaPricaList.get(konacnaPricaList.size() - 1);
        assertThat(testKonacnaPrica.getTekst()).isEqualTo(UPDATED_TEKST);
    }

    @Test
    @Transactional
    void fullUpdateKonacnaPricaWithPatch() throws Exception {
        // Initialize the database
        konacnaPricaRepository.saveAndFlush(konacnaPrica);

        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();

        // Update the konacnaPrica using partial update
        KonacnaPrica partialUpdatedKonacnaPrica = new KonacnaPrica();
        partialUpdatedKonacnaPrica.setId(konacnaPrica.getId());

        partialUpdatedKonacnaPrica.tekst(UPDATED_TEKST);

        restKonacnaPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKonacnaPrica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKonacnaPrica))
            )
            .andExpect(status().isOk());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
        KonacnaPrica testKonacnaPrica = konacnaPricaList.get(konacnaPricaList.size() - 1);
        assertThat(testKonacnaPrica.getTekst()).isEqualTo(UPDATED_TEKST);
    }

    @Test
    @Transactional
    void patchNonExistingKonacnaPrica() throws Exception {
        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();
        konacnaPrica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKonacnaPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, konacnaPrica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(konacnaPrica))
            )
            .andExpect(status().isBadRequest());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKonacnaPrica() throws Exception {
        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();
        konacnaPrica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKonacnaPricaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(konacnaPrica))
            )
            .andExpect(status().isBadRequest());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKonacnaPrica() throws Exception {
        int databaseSizeBeforeUpdate = konacnaPricaRepository.findAll().size();
        konacnaPrica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKonacnaPricaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(konacnaPrica))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KonacnaPrica in the database
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKonacnaPrica() throws Exception {
        // Initialize the database
        konacnaPricaRepository.saveAndFlush(konacnaPrica);

        int databaseSizeBeforeDelete = konacnaPricaRepository.findAll().size();

        // Delete the konacnaPrica
        restKonacnaPricaMockMvc
            .perform(delete(ENTITY_API_URL_ID, konacnaPrica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KonacnaPrica> konacnaPricaList = konacnaPricaRepository.findAll();
        assertThat(konacnaPricaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
