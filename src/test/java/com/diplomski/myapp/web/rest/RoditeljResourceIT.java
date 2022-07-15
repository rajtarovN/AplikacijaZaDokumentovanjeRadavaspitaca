package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Roditelj;
import com.diplomski.myapp.repository.RoditeljRepository;
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
 * Integration tests for the {@link RoditeljResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoditeljResourceIT {

    private static final String ENTITY_API_URL = "/api/roditeljs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoditeljRepository roditeljRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoditeljMockMvc;

    private Roditelj roditelj;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roditelj createEntity(EntityManager em) {
        Roditelj roditelj = new Roditelj();
        return roditelj;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roditelj createUpdatedEntity(EntityManager em) {
        Roditelj roditelj = new Roditelj();
        return roditelj;
    }

    @BeforeEach
    public void initTest() {
        roditelj = createEntity(em);
    }

    @Test
    @Transactional
    void createRoditelj() throws Exception {
        int databaseSizeBeforeCreate = roditeljRepository.findAll().size();
        // Create the Roditelj
        restRoditeljMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roditelj)))
            .andExpect(status().isCreated());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeCreate + 1);
        Roditelj testRoditelj = roditeljList.get(roditeljList.size() - 1);
    }

    @Test
    @Transactional
    void createRoditeljWithExistingId() throws Exception {
        // Create the Roditelj with an existing ID
        roditelj.setId(1L);

        int databaseSizeBeforeCreate = roditeljRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoditeljMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roditelj)))
            .andExpect(status().isBadRequest());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoditeljs() throws Exception {
        // Initialize the database
        roditeljRepository.saveAndFlush(roditelj);

        // Get all the roditeljList
        restRoditeljMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roditelj.getId().intValue())));
    }

    @Test
    @Transactional
    void getRoditelj() throws Exception {
        // Initialize the database
        roditeljRepository.saveAndFlush(roditelj);

        // Get the roditelj
        restRoditeljMockMvc
            .perform(get(ENTITY_API_URL_ID, roditelj.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roditelj.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRoditelj() throws Exception {
        // Get the roditelj
        restRoditeljMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoditelj() throws Exception {
        // Initialize the database
        roditeljRepository.saveAndFlush(roditelj);

        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();

        // Update the roditelj
        Roditelj updatedRoditelj = roditeljRepository.findById(roditelj.getId()).get();
        // Disconnect from session so that the updates on updatedRoditelj are not directly saved in db
        em.detach(updatedRoditelj);

        restRoditeljMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoditelj.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoditelj))
            )
            .andExpect(status().isOk());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
        Roditelj testRoditelj = roditeljList.get(roditeljList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingRoditelj() throws Exception {
        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();
        roditelj.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoditeljMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roditelj.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roditelj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoditelj() throws Exception {
        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();
        roditelj.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoditeljMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roditelj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoditelj() throws Exception {
        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();
        roditelj.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoditeljMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roditelj)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoditeljWithPatch() throws Exception {
        // Initialize the database
        roditeljRepository.saveAndFlush(roditelj);

        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();

        // Update the roditelj using partial update
        Roditelj partialUpdatedRoditelj = new Roditelj();
        partialUpdatedRoditelj.setId(roditelj.getId());

        restRoditeljMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoditelj.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoditelj))
            )
            .andExpect(status().isOk());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
        Roditelj testRoditelj = roditeljList.get(roditeljList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateRoditeljWithPatch() throws Exception {
        // Initialize the database
        roditeljRepository.saveAndFlush(roditelj);

        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();

        // Update the roditelj using partial update
        Roditelj partialUpdatedRoditelj = new Roditelj();
        partialUpdatedRoditelj.setId(roditelj.getId());

        restRoditeljMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoditelj.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoditelj))
            )
            .andExpect(status().isOk());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
        Roditelj testRoditelj = roditeljList.get(roditeljList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingRoditelj() throws Exception {
        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();
        roditelj.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoditeljMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roditelj.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roditelj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoditelj() throws Exception {
        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();
        roditelj.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoditeljMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roditelj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoditelj() throws Exception {
        int databaseSizeBeforeUpdate = roditeljRepository.findAll().size();
        roditelj.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoditeljMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roditelj)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roditelj in the database
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoditelj() throws Exception {
        // Initialize the database
        roditeljRepository.saveAndFlush(roditelj);

        int databaseSizeBeforeDelete = roditeljRepository.findAll().size();

        // Delete the roditelj
        restRoditeljMockMvc
            .perform(delete(ENTITY_API_URL_ID, roditelj.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roditelj> roditeljList = roditeljRepository.findAll();
        assertThat(roditeljList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
