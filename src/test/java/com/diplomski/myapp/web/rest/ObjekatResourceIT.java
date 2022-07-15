package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Objekat;
import com.diplomski.myapp.repository.ObjekatRepository;
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
 * Integration tests for the {@link ObjekatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjekatResourceIT {

    private static final String DEFAULT_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_OPIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/objekats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjekatRepository objekatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObjekatMockMvc;

    private Objekat objekat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objekat createEntity(EntityManager em) {
        Objekat objekat = new Objekat().opis(DEFAULT_OPIS);
        return objekat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objekat createUpdatedEntity(EntityManager em) {
        Objekat objekat = new Objekat().opis(UPDATED_OPIS);
        return objekat;
    }

    @BeforeEach
    public void initTest() {
        objekat = createEntity(em);
    }

    @Test
    @Transactional
    void createObjekat() throws Exception {
        int databaseSizeBeforeCreate = objekatRepository.findAll().size();
        // Create the Objekat
        restObjekatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objekat)))
            .andExpect(status().isCreated());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeCreate + 1);
        Objekat testObjekat = objekatList.get(objekatList.size() - 1);
        assertThat(testObjekat.getOpis()).isEqualTo(DEFAULT_OPIS);
    }

    @Test
    @Transactional
    void createObjekatWithExistingId() throws Exception {
        // Create the Objekat with an existing ID
        objekat.setId(1L);

        int databaseSizeBeforeCreate = objekatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjekatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objekat)))
            .andExpect(status().isBadRequest());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllObjekats() throws Exception {
        // Initialize the database
        objekatRepository.saveAndFlush(objekat);

        // Get all the objekatList
        restObjekatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objekat.getId().intValue())))
            .andExpect(jsonPath("$.[*].opis").value(hasItem(DEFAULT_OPIS)));
    }

    @Test
    @Transactional
    void getObjekat() throws Exception {
        // Initialize the database
        objekatRepository.saveAndFlush(objekat);

        // Get the objekat
        restObjekatMockMvc
            .perform(get(ENTITY_API_URL_ID, objekat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objekat.getId().intValue()))
            .andExpect(jsonPath("$.opis").value(DEFAULT_OPIS));
    }

    @Test
    @Transactional
    void getNonExistingObjekat() throws Exception {
        // Get the objekat
        restObjekatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewObjekat() throws Exception {
        // Initialize the database
        objekatRepository.saveAndFlush(objekat);

        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();

        // Update the objekat
        Objekat updatedObjekat = objekatRepository.findById(objekat.getId()).get();
        // Disconnect from session so that the updates on updatedObjekat are not directly saved in db
        em.detach(updatedObjekat);
        updatedObjekat.opis(UPDATED_OPIS);

        restObjekatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedObjekat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedObjekat))
            )
            .andExpect(status().isOk());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
        Objekat testObjekat = objekatList.get(objekatList.size() - 1);
        assertThat(testObjekat.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void putNonExistingObjekat() throws Exception {
        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();
        objekat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjekatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objekat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objekat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObjekat() throws Exception {
        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();
        objekat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjekatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objekat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObjekat() throws Exception {
        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();
        objekat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjekatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objekat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObjekatWithPatch() throws Exception {
        // Initialize the database
        objekatRepository.saveAndFlush(objekat);

        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();

        // Update the objekat using partial update
        Objekat partialUpdatedObjekat = new Objekat();
        partialUpdatedObjekat.setId(objekat.getId());

        restObjekatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjekat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjekat))
            )
            .andExpect(status().isOk());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
        Objekat testObjekat = objekatList.get(objekatList.size() - 1);
        assertThat(testObjekat.getOpis()).isEqualTo(DEFAULT_OPIS);
    }

    @Test
    @Transactional
    void fullUpdateObjekatWithPatch() throws Exception {
        // Initialize the database
        objekatRepository.saveAndFlush(objekat);

        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();

        // Update the objekat using partial update
        Objekat partialUpdatedObjekat = new Objekat();
        partialUpdatedObjekat.setId(objekat.getId());

        partialUpdatedObjekat.opis(UPDATED_OPIS);

        restObjekatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjekat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjekat))
            )
            .andExpect(status().isOk());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
        Objekat testObjekat = objekatList.get(objekatList.size() - 1);
        assertThat(testObjekat.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void patchNonExistingObjekat() throws Exception {
        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();
        objekat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjekatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objekat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objekat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObjekat() throws Exception {
        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();
        objekat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjekatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objekat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObjekat() throws Exception {
        int databaseSizeBeforeUpdate = objekatRepository.findAll().size();
        objekat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjekatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(objekat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objekat in the database
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObjekat() throws Exception {
        // Initialize the database
        objekatRepository.saveAndFlush(objekat);

        int databaseSizeBeforeDelete = objekatRepository.findAll().size();

        // Delete the objekat
        restObjekatMockMvc
            .perform(delete(ENTITY_API_URL_ID, objekat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Objekat> objekatList = objekatRepository.findAll();
        assertThat(objekatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
