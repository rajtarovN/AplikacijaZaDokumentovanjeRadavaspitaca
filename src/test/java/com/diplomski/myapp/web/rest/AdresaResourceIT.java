package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Adresa;
import com.diplomski.myapp.repository.AdresaRepository;
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
 * Integration tests for the {@link AdresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdresaResourceIT {

    private static final String DEFAULT_MESTO = "AAAAAAAAAA";
    private static final String UPDATED_MESTO = "BBBBBBBBBB";

    private static final String DEFAULT_ULICA = "AAAAAAAAAA";
    private static final String UPDATED_ULICA = "BBBBBBBBBB";

    private static final String DEFAULT_BROJ = "AAAAAAAAAA";
    private static final String UPDATED_BROJ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/adresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdresaRepository adresaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdresaMockMvc;

    private Adresa adresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adresa createEntity(EntityManager em) {
        Adresa adresa = new Adresa().mesto(DEFAULT_MESTO).ulica(DEFAULT_ULICA).broj(DEFAULT_BROJ);
        return adresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adresa createUpdatedEntity(EntityManager em) {
        Adresa adresa = new Adresa().mesto(UPDATED_MESTO).ulica(UPDATED_ULICA).broj(UPDATED_BROJ);
        return adresa;
    }

    @BeforeEach
    public void initTest() {
        adresa = createEntity(em);
    }

    @Test
    @Transactional
    void createAdresa() throws Exception {
        int databaseSizeBeforeCreate = adresaRepository.findAll().size();
        // Create the Adresa
        restAdresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresa)))
            .andExpect(status().isCreated());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeCreate + 1);
        Adresa testAdresa = adresaList.get(adresaList.size() - 1);
        assertThat(testAdresa.getMesto()).isEqualTo(DEFAULT_MESTO);
        assertThat(testAdresa.getUlica()).isEqualTo(DEFAULT_ULICA);
        assertThat(testAdresa.getBroj()).isEqualTo(DEFAULT_BROJ);
    }

    @Test
    @Transactional
    void createAdresaWithExistingId() throws Exception {
        // Create the Adresa with an existing ID
        adresa.setId(1L);

        int databaseSizeBeforeCreate = adresaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresa)))
            .andExpect(status().isBadRequest());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdresas() throws Exception {
        // Initialize the database
        adresaRepository.saveAndFlush(adresa);

        // Get all the adresaList
        restAdresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].mesto").value(hasItem(DEFAULT_MESTO)))
            .andExpect(jsonPath("$.[*].ulica").value(hasItem(DEFAULT_ULICA)))
            .andExpect(jsonPath("$.[*].broj").value(hasItem(DEFAULT_BROJ)));
    }

    @Test
    @Transactional
    void getAdresa() throws Exception {
        // Initialize the database
        adresaRepository.saveAndFlush(adresa);

        // Get the adresa
        restAdresaMockMvc
            .perform(get(ENTITY_API_URL_ID, adresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adresa.getId().intValue()))
            .andExpect(jsonPath("$.mesto").value(DEFAULT_MESTO))
            .andExpect(jsonPath("$.ulica").value(DEFAULT_ULICA))
            .andExpect(jsonPath("$.broj").value(DEFAULT_BROJ));
    }

    @Test
    @Transactional
    void getNonExistingAdresa() throws Exception {
        // Get the adresa
        restAdresaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdresa() throws Exception {
        // Initialize the database
        adresaRepository.saveAndFlush(adresa);

        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();

        // Update the adresa
        Adresa updatedAdresa = adresaRepository.findById(adresa.getId()).get();
        // Disconnect from session so that the updates on updatedAdresa are not directly saved in db
        em.detach(updatedAdresa);
        updatedAdresa.mesto(UPDATED_MESTO).ulica(UPDATED_ULICA).broj(UPDATED_BROJ);

        restAdresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdresa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdresa))
            )
            .andExpect(status().isOk());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
        Adresa testAdresa = adresaList.get(adresaList.size() - 1);
        assertThat(testAdresa.getMesto()).isEqualTo(UPDATED_MESTO);
        assertThat(testAdresa.getUlica()).isEqualTo(UPDATED_ULICA);
        assertThat(testAdresa.getBroj()).isEqualTo(UPDATED_BROJ);
    }

    @Test
    @Transactional
    void putNonExistingAdresa() throws Exception {
        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();
        adresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adresa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdresa() throws Exception {
        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();
        adresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdresa() throws Exception {
        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();
        adresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdresaWithPatch() throws Exception {
        // Initialize the database
        adresaRepository.saveAndFlush(adresa);

        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();

        // Update the adresa using partial update
        Adresa partialUpdatedAdresa = new Adresa();
        partialUpdatedAdresa.setId(adresa.getId());

        partialUpdatedAdresa.mesto(UPDATED_MESTO).broj(UPDATED_BROJ);

        restAdresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdresa))
            )
            .andExpect(status().isOk());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
        Adresa testAdresa = adresaList.get(adresaList.size() - 1);
        assertThat(testAdresa.getMesto()).isEqualTo(UPDATED_MESTO);
        assertThat(testAdresa.getUlica()).isEqualTo(DEFAULT_ULICA);
        assertThat(testAdresa.getBroj()).isEqualTo(UPDATED_BROJ);
    }

    @Test
    @Transactional
    void fullUpdateAdresaWithPatch() throws Exception {
        // Initialize the database
        adresaRepository.saveAndFlush(adresa);

        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();

        // Update the adresa using partial update
        Adresa partialUpdatedAdresa = new Adresa();
        partialUpdatedAdresa.setId(adresa.getId());

        partialUpdatedAdresa.mesto(UPDATED_MESTO).ulica(UPDATED_ULICA).broj(UPDATED_BROJ);

        restAdresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdresa))
            )
            .andExpect(status().isOk());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
        Adresa testAdresa = adresaList.get(adresaList.size() - 1);
        assertThat(testAdresa.getMesto()).isEqualTo(UPDATED_MESTO);
        assertThat(testAdresa.getUlica()).isEqualTo(UPDATED_ULICA);
        assertThat(testAdresa.getBroj()).isEqualTo(UPDATED_BROJ);
    }

    @Test
    @Transactional
    void patchNonExistingAdresa() throws Exception {
        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();
        adresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdresa() throws Exception {
        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();
        adresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdresa() throws Exception {
        int databaseSizeBeforeUpdate = adresaRepository.findAll().size();
        adresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adresa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adresa in the database
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdresa() throws Exception {
        // Initialize the database
        adresaRepository.saveAndFlush(adresa);

        int databaseSizeBeforeDelete = adresaRepository.findAll().size();

        // Delete the adresa
        restAdresaMockMvc
            .perform(delete(ENTITY_API_URL_ID, adresa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adresa> adresaList = adresaRepository.findAll();
        assertThat(adresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
