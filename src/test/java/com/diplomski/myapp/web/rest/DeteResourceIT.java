package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.repository.DeteRepository;
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
 * Integration tests for the {@link DeteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeteResourceIT {

    private static final Integer DEFAULT_VISINA = 1;
    private static final Integer UPDATED_VISINA = 2;

    private static final Integer DEFAULT_TEZINA = 1;
    private static final Integer UPDATED_TEZINA = 2;

    private static final String ENTITY_API_URL = "/api/detes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeteRepository deteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeteMockMvc;

    private Dete dete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dete createEntity(EntityManager em) {
        Dete dete = new Dete().visina(DEFAULT_VISINA).tezina(DEFAULT_TEZINA);
        return dete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dete createUpdatedEntity(EntityManager em) {
        Dete dete = new Dete().visina(UPDATED_VISINA).tezina(UPDATED_TEZINA);
        return dete;
    }

    @BeforeEach
    public void initTest() {
        dete = createEntity(em);
    }

    @Test
    @Transactional
    void createDete() throws Exception {
        int databaseSizeBeforeCreate = deteRepository.findAll().size();
        // Create the Dete
        restDeteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dete)))
            .andExpect(status().isCreated());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeCreate + 1);
        Dete testDete = deteList.get(deteList.size() - 1);
        assertThat(testDete.getVisina()).isEqualTo(DEFAULT_VISINA);
        assertThat(testDete.getTezina()).isEqualTo(DEFAULT_TEZINA);
    }

    @Test
    @Transactional
    void createDeteWithExistingId() throws Exception {
        // Create the Dete with an existing ID
        dete.setId(1L);

        int databaseSizeBeforeCreate = deteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dete)))
            .andExpect(status().isBadRequest());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetes() throws Exception {
        // Initialize the database
        deteRepository.saveAndFlush(dete);

        // Get all the deteList
        restDeteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dete.getId().intValue())))
            .andExpect(jsonPath("$.[*].visina").value(hasItem(DEFAULT_VISINA)))
            .andExpect(jsonPath("$.[*].tezina").value(hasItem(DEFAULT_TEZINA)));
    }

    @Test
    @Transactional
    void getDete() throws Exception {
        // Initialize the database
        deteRepository.saveAndFlush(dete);

        // Get the dete
        restDeteMockMvc
            .perform(get(ENTITY_API_URL_ID, dete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dete.getId().intValue()))
            .andExpect(jsonPath("$.visina").value(DEFAULT_VISINA))
            .andExpect(jsonPath("$.tezina").value(DEFAULT_TEZINA));
    }

    @Test
    @Transactional
    void getNonExistingDete() throws Exception {
        // Get the dete
        restDeteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDete() throws Exception {
        // Initialize the database
        deteRepository.saveAndFlush(dete);

        int databaseSizeBeforeUpdate = deteRepository.findAll().size();

        // Update the dete
        Dete updatedDete = deteRepository.findById(dete.getId()).get();
        // Disconnect from session so that the updates on updatedDete are not directly saved in db
        em.detach(updatedDete);
        updatedDete.visina(UPDATED_VISINA).tezina(UPDATED_TEZINA);

        restDeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDete))
            )
            .andExpect(status().isOk());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
        Dete testDete = deteList.get(deteList.size() - 1);
        assertThat(testDete.getVisina()).isEqualTo(UPDATED_VISINA);
        assertThat(testDete.getTezina()).isEqualTo(UPDATED_TEZINA);
    }

    @Test
    @Transactional
    void putNonExistingDete() throws Exception {
        int databaseSizeBeforeUpdate = deteRepository.findAll().size();
        dete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDete() throws Exception {
        int databaseSizeBeforeUpdate = deteRepository.findAll().size();
        dete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDete() throws Exception {
        int databaseSizeBeforeUpdate = deteRepository.findAll().size();
        dete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeteWithPatch() throws Exception {
        // Initialize the database
        deteRepository.saveAndFlush(dete);

        int databaseSizeBeforeUpdate = deteRepository.findAll().size();

        // Update the dete using partial update
        Dete partialUpdatedDete = new Dete();
        partialUpdatedDete.setId(dete.getId());

        partialUpdatedDete.tezina(UPDATED_TEZINA);

        restDeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDete))
            )
            .andExpect(status().isOk());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
        Dete testDete = deteList.get(deteList.size() - 1);
        assertThat(testDete.getVisina()).isEqualTo(DEFAULT_VISINA);
        assertThat(testDete.getTezina()).isEqualTo(UPDATED_TEZINA);
    }

    @Test
    @Transactional
    void fullUpdateDeteWithPatch() throws Exception {
        // Initialize the database
        deteRepository.saveAndFlush(dete);

        int databaseSizeBeforeUpdate = deteRepository.findAll().size();

        // Update the dete using partial update
        Dete partialUpdatedDete = new Dete();
        partialUpdatedDete.setId(dete.getId());

        partialUpdatedDete.visina(UPDATED_VISINA).tezina(UPDATED_TEZINA);

        restDeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDete))
            )
            .andExpect(status().isOk());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
        Dete testDete = deteList.get(deteList.size() - 1);
        assertThat(testDete.getVisina()).isEqualTo(UPDATED_VISINA);
        assertThat(testDete.getTezina()).isEqualTo(UPDATED_TEZINA);
    }

    @Test
    @Transactional
    void patchNonExistingDete() throws Exception {
        int databaseSizeBeforeUpdate = deteRepository.findAll().size();
        dete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDete() throws Exception {
        int databaseSizeBeforeUpdate = deteRepository.findAll().size();
        dete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDete() throws Exception {
        int databaseSizeBeforeUpdate = deteRepository.findAll().size();
        dete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dete in the database
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDete() throws Exception {
        // Initialize the database
        deteRepository.saveAndFlush(dete);

        int databaseSizeBeforeDelete = deteRepository.findAll().size();

        // Delete the dete
        restDeteMockMvc
            .perform(delete(ENTITY_API_URL_ID, dete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dete> deteList = deteRepository.findAll();
        assertThat(deteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
