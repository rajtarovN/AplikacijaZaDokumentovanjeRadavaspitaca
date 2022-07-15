package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.KomentarNaPricu;
import com.diplomski.myapp.repository.KomentarNaPricuRepository;
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
 * Integration tests for the {@link KomentarNaPricuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KomentarNaPricuResourceIT {

    private static final String DEFAULT_TEKST = "AAAAAAAAAA";
    private static final String UPDATED_TEKST = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATUM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/komentar-na-pricus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KomentarNaPricuRepository komentarNaPricuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKomentarNaPricuMockMvc;

    private KomentarNaPricu komentarNaPricu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KomentarNaPricu createEntity(EntityManager em) {
        KomentarNaPricu komentarNaPricu = new KomentarNaPricu().tekst(DEFAULT_TEKST).datum(DEFAULT_DATUM);
        return komentarNaPricu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KomentarNaPricu createUpdatedEntity(EntityManager em) {
        KomentarNaPricu komentarNaPricu = new KomentarNaPricu().tekst(UPDATED_TEKST).datum(UPDATED_DATUM);
        return komentarNaPricu;
    }

    @BeforeEach
    public void initTest() {
        komentarNaPricu = createEntity(em);
    }

    @Test
    @Transactional
    void createKomentarNaPricu() throws Exception {
        int databaseSizeBeforeCreate = komentarNaPricuRepository.findAll().size();
        // Create the KomentarNaPricu
        restKomentarNaPricuMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isCreated());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeCreate + 1);
        KomentarNaPricu testKomentarNaPricu = komentarNaPricuList.get(komentarNaPricuList.size() - 1);
        assertThat(testKomentarNaPricu.getTekst()).isEqualTo(DEFAULT_TEKST);
        assertThat(testKomentarNaPricu.getDatum()).isEqualTo(DEFAULT_DATUM);
    }

    @Test
    @Transactional
    void createKomentarNaPricuWithExistingId() throws Exception {
        // Create the KomentarNaPricu with an existing ID
        komentarNaPricu.setId(1L);

        int databaseSizeBeforeCreate = komentarNaPricuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKomentarNaPricuMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isBadRequest());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKomentarNaPricus() throws Exception {
        // Initialize the database
        komentarNaPricuRepository.saveAndFlush(komentarNaPricu);

        // Get all the komentarNaPricuList
        restKomentarNaPricuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(komentarNaPricu.getId().intValue())))
            .andExpect(jsonPath("$.[*].tekst").value(hasItem(DEFAULT_TEKST)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())));
    }

    @Test
    @Transactional
    void getKomentarNaPricu() throws Exception {
        // Initialize the database
        komentarNaPricuRepository.saveAndFlush(komentarNaPricu);

        // Get the komentarNaPricu
        restKomentarNaPricuMockMvc
            .perform(get(ENTITY_API_URL_ID, komentarNaPricu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(komentarNaPricu.getId().intValue()))
            .andExpect(jsonPath("$.tekst").value(DEFAULT_TEKST))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingKomentarNaPricu() throws Exception {
        // Get the komentarNaPricu
        restKomentarNaPricuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKomentarNaPricu() throws Exception {
        // Initialize the database
        komentarNaPricuRepository.saveAndFlush(komentarNaPricu);

        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();

        // Update the komentarNaPricu
        KomentarNaPricu updatedKomentarNaPricu = komentarNaPricuRepository.findById(komentarNaPricu.getId()).get();
        // Disconnect from session so that the updates on updatedKomentarNaPricu are not directly saved in db
        em.detach(updatedKomentarNaPricu);
        updatedKomentarNaPricu.tekst(UPDATED_TEKST).datum(UPDATED_DATUM);

        restKomentarNaPricuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKomentarNaPricu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKomentarNaPricu))
            )
            .andExpect(status().isOk());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
        KomentarNaPricu testKomentarNaPricu = komentarNaPricuList.get(komentarNaPricuList.size() - 1);
        assertThat(testKomentarNaPricu.getTekst()).isEqualTo(UPDATED_TEKST);
        assertThat(testKomentarNaPricu.getDatum()).isEqualTo(UPDATED_DATUM);
    }

    @Test
    @Transactional
    void putNonExistingKomentarNaPricu() throws Exception {
        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();
        komentarNaPricu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKomentarNaPricuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, komentarNaPricu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isBadRequest());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKomentarNaPricu() throws Exception {
        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();
        komentarNaPricu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKomentarNaPricuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isBadRequest());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKomentarNaPricu() throws Exception {
        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();
        komentarNaPricu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKomentarNaPricuMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKomentarNaPricuWithPatch() throws Exception {
        // Initialize the database
        komentarNaPricuRepository.saveAndFlush(komentarNaPricu);

        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();

        // Update the komentarNaPricu using partial update
        KomentarNaPricu partialUpdatedKomentarNaPricu = new KomentarNaPricu();
        partialUpdatedKomentarNaPricu.setId(komentarNaPricu.getId());

        partialUpdatedKomentarNaPricu.tekst(UPDATED_TEKST).datum(UPDATED_DATUM);

        restKomentarNaPricuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKomentarNaPricu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKomentarNaPricu))
            )
            .andExpect(status().isOk());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
        KomentarNaPricu testKomentarNaPricu = komentarNaPricuList.get(komentarNaPricuList.size() - 1);
        assertThat(testKomentarNaPricu.getTekst()).isEqualTo(UPDATED_TEKST);
        assertThat(testKomentarNaPricu.getDatum()).isEqualTo(UPDATED_DATUM);
    }

    @Test
    @Transactional
    void fullUpdateKomentarNaPricuWithPatch() throws Exception {
        // Initialize the database
        komentarNaPricuRepository.saveAndFlush(komentarNaPricu);

        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();

        // Update the komentarNaPricu using partial update
        KomentarNaPricu partialUpdatedKomentarNaPricu = new KomentarNaPricu();
        partialUpdatedKomentarNaPricu.setId(komentarNaPricu.getId());

        partialUpdatedKomentarNaPricu.tekst(UPDATED_TEKST).datum(UPDATED_DATUM);

        restKomentarNaPricuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKomentarNaPricu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKomentarNaPricu))
            )
            .andExpect(status().isOk());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
        KomentarNaPricu testKomentarNaPricu = komentarNaPricuList.get(komentarNaPricuList.size() - 1);
        assertThat(testKomentarNaPricu.getTekst()).isEqualTo(UPDATED_TEKST);
        assertThat(testKomentarNaPricu.getDatum()).isEqualTo(UPDATED_DATUM);
    }

    @Test
    @Transactional
    void patchNonExistingKomentarNaPricu() throws Exception {
        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();
        komentarNaPricu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKomentarNaPricuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, komentarNaPricu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isBadRequest());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKomentarNaPricu() throws Exception {
        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();
        komentarNaPricu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKomentarNaPricuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isBadRequest());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKomentarNaPricu() throws Exception {
        int databaseSizeBeforeUpdate = komentarNaPricuRepository.findAll().size();
        komentarNaPricu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKomentarNaPricuMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(komentarNaPricu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KomentarNaPricu in the database
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKomentarNaPricu() throws Exception {
        // Initialize the database
        komentarNaPricuRepository.saveAndFlush(komentarNaPricu);

        int databaseSizeBeforeDelete = komentarNaPricuRepository.findAll().size();

        // Delete the komentarNaPricu
        restKomentarNaPricuMockMvc
            .perform(delete(ENTITY_API_URL_ID, komentarNaPricu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KomentarNaPricu> komentarNaPricuList = komentarNaPricuRepository.findAll();
        assertThat(komentarNaPricuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
