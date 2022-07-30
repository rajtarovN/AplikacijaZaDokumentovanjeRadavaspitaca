package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Korisnik;
import com.diplomski.myapp.repository.KorisnikRepository;
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
 * Integration tests for the {@link KorisnikResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KorisnikResourceIT {

    private static final String DEFAULT_KORISNICKO_IME = "AAAAAAAAAA";
    private static final String UPDATED_KORISNICKO_IME = "BBBBBBBBBB";

    private static final String DEFAULT_SIFRA = "AAAAAAAAAA";
    private static final String UPDATED_SIFRA = "BBBBBBBBBB";

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final String DEFAULT_PREZIME = "AAAAAAAAAA";
    private static final String UPDATED_PREZIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/korisniks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKorisnikMockMvc;

    private Korisnik korisnik;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Korisnik createEntity(EntityManager em) {
        Korisnik korisnik = new Korisnik();
        return korisnik;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Korisnik createUpdatedEntity(EntityManager em) {
        Korisnik korisnik = new Korisnik();
        return korisnik;
    }

    @BeforeEach
    public void initTest() {
        korisnik = createEntity(em);
    }

    @Test
    @Transactional
    void createKorisnik() throws Exception {
        int databaseSizeBeforeCreate = korisnikRepository.findAll().size();
        // Create the Korisnik
        restKorisnikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(korisnik)))
            .andExpect(status().isCreated());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeCreate + 1);
        Korisnik testKorisnik = korisnikList.get(korisnikList.size() - 1);
    }

    @Test
    @Transactional
    void createKorisnikWithExistingId() throws Exception {
        // Create the Korisnik with an existing ID
        korisnik.setId(1L);

        int databaseSizeBeforeCreate = korisnikRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKorisnikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(korisnik)))
            .andExpect(status().isBadRequest());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKorisniks() throws Exception {
        // Initialize the database
        korisnikRepository.saveAndFlush(korisnik);

        // Get all the korisnikList
        restKorisnikMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(korisnik.getId().intValue())))
            .andExpect(jsonPath("$.[*].korisnickoIme").value(hasItem(DEFAULT_KORISNICKO_IME)))
            .andExpect(jsonPath("$.[*].sifra").value(hasItem(DEFAULT_SIFRA)))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].prezime").value(hasItem(DEFAULT_PREZIME)));
    }

    @Test
    @Transactional
    void getKorisnik() throws Exception {
        // Initialize the database
        korisnikRepository.saveAndFlush(korisnik);

        // Get the korisnik
        restKorisnikMockMvc
            .perform(get(ENTITY_API_URL_ID, korisnik.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(korisnik.getId().intValue()))
            .andExpect(jsonPath("$.korisnickoIme").value(DEFAULT_KORISNICKO_IME))
            .andExpect(jsonPath("$.sifra").value(DEFAULT_SIFRA))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME))
            .andExpect(jsonPath("$.prezime").value(DEFAULT_PREZIME));
    }

    @Test
    @Transactional
    void getNonExistingKorisnik() throws Exception {
        // Get the korisnik
        restKorisnikMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKorisnik() throws Exception {
        // Initialize the database
        korisnikRepository.saveAndFlush(korisnik);

        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();

        // Update the korisnik
        Korisnik updatedKorisnik = korisnikRepository.findById(korisnik.getId()).get();
        // Disconnect from session so that the updates on updatedKorisnik are not directly saved in db
        em.detach(updatedKorisnik);

        restKorisnikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKorisnik.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKorisnik))
            )
            .andExpect(status().isOk());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
        Korisnik testKorisnik = korisnikList.get(korisnikList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingKorisnik() throws Exception {
        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();
        korisnik.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKorisnikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, korisnik.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(korisnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKorisnik() throws Exception {
        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();
        korisnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKorisnikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(korisnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKorisnik() throws Exception {
        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();
        korisnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKorisnikMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(korisnik)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKorisnikWithPatch() throws Exception {
        // Initialize the database
        korisnikRepository.saveAndFlush(korisnik);

        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();

        // Update the korisnik using partial update
        Korisnik partialUpdatedKorisnik = new Korisnik();
        partialUpdatedKorisnik.setId(korisnik.getId());

        restKorisnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKorisnik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKorisnik))
            )
            .andExpect(status().isOk());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
        Korisnik testKorisnik = korisnikList.get(korisnikList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateKorisnikWithPatch() throws Exception {
        // Initialize the database
        korisnikRepository.saveAndFlush(korisnik);

        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();

        // Update the korisnik using partial update
        Korisnik partialUpdatedKorisnik = new Korisnik();
        partialUpdatedKorisnik.setId(korisnik.getId());

        restKorisnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKorisnik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKorisnik))
            )
            .andExpect(status().isOk());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
        Korisnik testKorisnik = korisnikList.get(korisnikList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingKorisnik() throws Exception {
        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();
        korisnik.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKorisnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, korisnik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(korisnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKorisnik() throws Exception {
        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();
        korisnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKorisnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(korisnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKorisnik() throws Exception {
        int databaseSizeBeforeUpdate = korisnikRepository.findAll().size();
        korisnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKorisnikMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(korisnik)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Korisnik in the database
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKorisnik() throws Exception {
        // Initialize the database
        korisnikRepository.saveAndFlush(korisnik);

        int databaseSizeBeforeDelete = korisnikRepository.findAll().size();

        // Delete the korisnik
        restKorisnikMockMvc
            .perform(delete(ENTITY_API_URL_ID, korisnik.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Korisnik> korisnikList = korisnikRepository.findAll();
        assertThat(korisnikList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
