package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Direktor;
import com.diplomski.myapp.repository.DirektorRepository;
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
 * Integration tests for the {@link DirektorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirektorResourceIT {

    private static final String ENTITY_API_URL = "/api/direktors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirektorRepository direktorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirektorMockMvc;

    private Direktor direktor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direktor createEntity(EntityManager em) {
        Direktor direktor = new Direktor();
        return direktor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direktor createUpdatedEntity(EntityManager em) {
        Direktor direktor = new Direktor();
        return direktor;
    }

    @BeforeEach
    public void initTest() {
        direktor = createEntity(em);
    }

    @Test
    @Transactional
    void createDirektor() throws Exception {
        int databaseSizeBeforeCreate = direktorRepository.findAll().size();
        // Create the Direktor
        restDirektorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direktor)))
            .andExpect(status().isCreated());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeCreate + 1);
        Direktor testDirektor = direktorList.get(direktorList.size() - 1);
    }

    @Test
    @Transactional
    void createDirektorWithExistingId() throws Exception {
        // Create the Direktor with an existing ID
        direktor.setId(1L);

        int databaseSizeBeforeCreate = direktorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirektorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direktor)))
            .andExpect(status().isBadRequest());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDirektors() throws Exception {
        // Initialize the database
        direktorRepository.saveAndFlush(direktor);

        // Get all the direktorList
        restDirektorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direktor.getId().intValue())));
    }

    @Test
    @Transactional
    void getDirektor() throws Exception {
        // Initialize the database
        direktorRepository.saveAndFlush(direktor);

        // Get the direktor
        restDirektorMockMvc
            .perform(get(ENTITY_API_URL_ID, direktor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direktor.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDirektor() throws Exception {
        // Get the direktor
        restDirektorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDirektor() throws Exception {
        // Initialize the database
        direktorRepository.saveAndFlush(direktor);

        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();

        // Update the direktor
        Direktor updatedDirektor = direktorRepository.findById(direktor.getId()).get();
        // Disconnect from session so that the updates on updatedDirektor are not directly saved in db
        em.detach(updatedDirektor);

        restDirektorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDirektor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDirektor))
            )
            .andExpect(status().isOk());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
        Direktor testDirektor = direktorList.get(direktorList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingDirektor() throws Exception {
        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();
        direktor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirektorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direktor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direktor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirektor() throws Exception {
        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();
        direktor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirektorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direktor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirektor() throws Exception {
        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();
        direktor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirektorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direktor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirektorWithPatch() throws Exception {
        // Initialize the database
        direktorRepository.saveAndFlush(direktor);

        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();

        // Update the direktor using partial update
        Direktor partialUpdatedDirektor = new Direktor();
        partialUpdatedDirektor.setId(direktor.getId());

        restDirektorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirektor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirektor))
            )
            .andExpect(status().isOk());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
        Direktor testDirektor = direktorList.get(direktorList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateDirektorWithPatch() throws Exception {
        // Initialize the database
        direktorRepository.saveAndFlush(direktor);

        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();

        // Update the direktor using partial update
        Direktor partialUpdatedDirektor = new Direktor();
        partialUpdatedDirektor.setId(direktor.getId());

        restDirektorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirektor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirektor))
            )
            .andExpect(status().isOk());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
        Direktor testDirektor = direktorList.get(direktorList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingDirektor() throws Exception {
        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();
        direktor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirektorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direktor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direktor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirektor() throws Exception {
        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();
        direktor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirektorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direktor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirektor() throws Exception {
        int databaseSizeBeforeUpdate = direktorRepository.findAll().size();
        direktor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirektorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(direktor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direktor in the database
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirektor() throws Exception {
        // Initialize the database
        direktorRepository.saveAndFlush(direktor);

        int databaseSizeBeforeDelete = direktorRepository.findAll().size();

        // Delete the direktor
        restDirektorMockMvc
            .perform(delete(ENTITY_API_URL_ID, direktor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Direktor> direktorList = direktorRepository.findAll();
        assertThat(direktorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
