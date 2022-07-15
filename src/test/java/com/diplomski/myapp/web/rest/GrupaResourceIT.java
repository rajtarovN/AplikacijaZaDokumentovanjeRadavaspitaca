package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Grupa;
import com.diplomski.myapp.domain.enumeration.TipGrupe;
import com.diplomski.myapp.repository.GrupaRepository;
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
 * Integration tests for the {@link GrupaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GrupaResourceIT {

    private static final TipGrupe DEFAULT_TIP_GRUPE = TipGrupe.POLUDNEVNA;
    private static final TipGrupe UPDATED_TIP_GRUPE = TipGrupe.JASLICE;

    private static final String ENTITY_API_URL = "/api/grupas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrupaRepository grupaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupaMockMvc;

    private Grupa grupa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupa createEntity(EntityManager em) {
        Grupa grupa = new Grupa().tipGrupe(DEFAULT_TIP_GRUPE);
        return grupa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupa createUpdatedEntity(EntityManager em) {
        Grupa grupa = new Grupa().tipGrupe(UPDATED_TIP_GRUPE);
        return grupa;
    }

    @BeforeEach
    public void initTest() {
        grupa = createEntity(em);
    }

    @Test
    @Transactional
    void createGrupa() throws Exception {
        int databaseSizeBeforeCreate = grupaRepository.findAll().size();
        // Create the Grupa
        restGrupaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupa)))
            .andExpect(status().isCreated());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeCreate + 1);
        Grupa testGrupa = grupaList.get(grupaList.size() - 1);
        assertThat(testGrupa.getTipGrupe()).isEqualTo(DEFAULT_TIP_GRUPE);
    }

    @Test
    @Transactional
    void createGrupaWithExistingId() throws Exception {
        // Create the Grupa with an existing ID
        grupa.setId(1L);

        int databaseSizeBeforeCreate = grupaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupa)))
            .andExpect(status().isBadRequest());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGrupas() throws Exception {
        // Initialize the database
        grupaRepository.saveAndFlush(grupa);

        // Get all the grupaList
        restGrupaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupa.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipGrupe").value(hasItem(DEFAULT_TIP_GRUPE.toString())));
    }

    @Test
    @Transactional
    void getGrupa() throws Exception {
        // Initialize the database
        grupaRepository.saveAndFlush(grupa);

        // Get the grupa
        restGrupaMockMvc
            .perform(get(ENTITY_API_URL_ID, grupa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupa.getId().intValue()))
            .andExpect(jsonPath("$.tipGrupe").value(DEFAULT_TIP_GRUPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGrupa() throws Exception {
        // Get the grupa
        restGrupaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGrupa() throws Exception {
        // Initialize the database
        grupaRepository.saveAndFlush(grupa);

        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();

        // Update the grupa
        Grupa updatedGrupa = grupaRepository.findById(grupa.getId()).get();
        // Disconnect from session so that the updates on updatedGrupa are not directly saved in db
        em.detach(updatedGrupa);
        updatedGrupa.tipGrupe(UPDATED_TIP_GRUPE);

        restGrupaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGrupa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGrupa))
            )
            .andExpect(status().isOk());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
        Grupa testGrupa = grupaList.get(grupaList.size() - 1);
        assertThat(testGrupa.getTipGrupe()).isEqualTo(UPDATED_TIP_GRUPE);
    }

    @Test
    @Transactional
    void putNonExistingGrupa() throws Exception {
        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();
        grupa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupa() throws Exception {
        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();
        grupa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupa() throws Exception {
        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();
        grupa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupaWithPatch() throws Exception {
        // Initialize the database
        grupaRepository.saveAndFlush(grupa);

        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();

        // Update the grupa using partial update
        Grupa partialUpdatedGrupa = new Grupa();
        partialUpdatedGrupa.setId(grupa.getId());

        restGrupaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupa))
            )
            .andExpect(status().isOk());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
        Grupa testGrupa = grupaList.get(grupaList.size() - 1);
        assertThat(testGrupa.getTipGrupe()).isEqualTo(DEFAULT_TIP_GRUPE);
    }

    @Test
    @Transactional
    void fullUpdateGrupaWithPatch() throws Exception {
        // Initialize the database
        grupaRepository.saveAndFlush(grupa);

        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();

        // Update the grupa using partial update
        Grupa partialUpdatedGrupa = new Grupa();
        partialUpdatedGrupa.setId(grupa.getId());

        partialUpdatedGrupa.tipGrupe(UPDATED_TIP_GRUPE);

        restGrupaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupa))
            )
            .andExpect(status().isOk());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
        Grupa testGrupa = grupaList.get(grupaList.size() - 1);
        assertThat(testGrupa.getTipGrupe()).isEqualTo(UPDATED_TIP_GRUPE);
    }

    @Test
    @Transactional
    void patchNonExistingGrupa() throws Exception {
        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();
        grupa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupa() throws Exception {
        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();
        grupa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupa() throws Exception {
        int databaseSizeBeforeUpdate = grupaRepository.findAll().size();
        grupa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(grupa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grupa in the database
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupa() throws Exception {
        // Initialize the database
        grupaRepository.saveAndFlush(grupa);

        int databaseSizeBeforeDelete = grupaRepository.findAll().size();

        // Delete the grupa
        restGrupaMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Grupa> grupaList = grupaRepository.findAll();
        assertThat(grupaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
