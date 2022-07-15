package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Pedagog;
import com.diplomski.myapp.repository.PedagogRepository;
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
 * Integration tests for the {@link PedagogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PedagogResourceIT {

    private static final String ENTITY_API_URL = "/api/pedagogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PedagogRepository pedagogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedagogMockMvc;

    private Pedagog pedagog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedagog createEntity(EntityManager em) {
        Pedagog pedagog = new Pedagog();
        return pedagog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedagog createUpdatedEntity(EntityManager em) {
        Pedagog pedagog = new Pedagog();
        return pedagog;
    }

    @BeforeEach
    public void initTest() {
        pedagog = createEntity(em);
    }

    @Test
    @Transactional
    void createPedagog() throws Exception {
        int databaseSizeBeforeCreate = pedagogRepository.findAll().size();
        // Create the Pedagog
        restPedagogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedagog)))
            .andExpect(status().isCreated());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeCreate + 1);
        Pedagog testPedagog = pedagogList.get(pedagogList.size() - 1);
    }

    @Test
    @Transactional
    void createPedagogWithExistingId() throws Exception {
        // Create the Pedagog with an existing ID
        pedagog.setId(1L);

        int databaseSizeBeforeCreate = pedagogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedagogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedagog)))
            .andExpect(status().isBadRequest());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPedagogs() throws Exception {
        // Initialize the database
        pedagogRepository.saveAndFlush(pedagog);

        // Get all the pedagogList
        restPedagogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedagog.getId().intValue())));
    }

    @Test
    @Transactional
    void getPedagog() throws Exception {
        // Initialize the database
        pedagogRepository.saveAndFlush(pedagog);

        // Get the pedagog
        restPedagogMockMvc
            .perform(get(ENTITY_API_URL_ID, pedagog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedagog.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPedagog() throws Exception {
        // Get the pedagog
        restPedagogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPedagog() throws Exception {
        // Initialize the database
        pedagogRepository.saveAndFlush(pedagog);

        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();

        // Update the pedagog
        Pedagog updatedPedagog = pedagogRepository.findById(pedagog.getId()).get();
        // Disconnect from session so that the updates on updatedPedagog are not directly saved in db
        em.detach(updatedPedagog);

        restPedagogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPedagog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPedagog))
            )
            .andExpect(status().isOk());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
        Pedagog testPedagog = pedagogList.get(pedagogList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPedagog() throws Exception {
        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();
        pedagog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedagogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedagog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedagog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedagog() throws Exception {
        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();
        pedagog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedagogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedagog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedagog() throws Exception {
        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();
        pedagog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedagogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedagog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedagogWithPatch() throws Exception {
        // Initialize the database
        pedagogRepository.saveAndFlush(pedagog);

        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();

        // Update the pedagog using partial update
        Pedagog partialUpdatedPedagog = new Pedagog();
        partialUpdatedPedagog.setId(pedagog.getId());

        restPedagogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedagog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPedagog))
            )
            .andExpect(status().isOk());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
        Pedagog testPedagog = pedagogList.get(pedagogList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePedagogWithPatch() throws Exception {
        // Initialize the database
        pedagogRepository.saveAndFlush(pedagog);

        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();

        // Update the pedagog using partial update
        Pedagog partialUpdatedPedagog = new Pedagog();
        partialUpdatedPedagog.setId(pedagog.getId());

        restPedagogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedagog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPedagog))
            )
            .andExpect(status().isOk());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
        Pedagog testPedagog = pedagogList.get(pedagogList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPedagog() throws Exception {
        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();
        pedagog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedagogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedagog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pedagog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedagog() throws Exception {
        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();
        pedagog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedagogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pedagog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedagog() throws Exception {
        int databaseSizeBeforeUpdate = pedagogRepository.findAll().size();
        pedagog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedagogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pedagog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedagog in the database
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedagog() throws Exception {
        // Initialize the database
        pedagogRepository.saveAndFlush(pedagog);

        int databaseSizeBeforeDelete = pedagogRepository.findAll().size();

        // Delete the pedagog
        restPedagogMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedagog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pedagog> pedagogList = pedagogRepository.findAll();
        assertThat(pedagogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
