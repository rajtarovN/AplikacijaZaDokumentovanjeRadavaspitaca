package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.PodaciORoditeljima;
import com.diplomski.myapp.domain.enumeration.RadniStatus;
import com.diplomski.myapp.repository.PodaciORoditeljimaRepository;
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
 * Integration tests for the {@link PodaciORoditeljimaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PodaciORoditeljimaResourceIT {

    private static final String DEFAULT_JMBG = "AAAAAAAAAA";
    private static final String UPDATED_JMBG = "BBBBBBBBBB";

    private static final String DEFAULT_IME = "AAAAAAAAAA";
    private static final String UPDATED_IME = "BBBBBBBBBB";

    private static final String DEFAULT_PREZIME = "AAAAAAAAAA";
    private static final String UPDATED_PREZIME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON = "BBBBBBBBBB";

    private static final String DEFAULT_FIRMA = "AAAAAAAAAA";
    private static final String UPDATED_FIRMA = "BBBBBBBBBB";

    private static final String DEFAULT_RADNO_VREME = "AAAAAAAAAA";
    private static final String UPDATED_RADNO_VREME = "BBBBBBBBBB";

    private static final RadniStatus DEFAULT_RADNI_STATUS = RadniStatus.ZAPOSLEN;
    private static final RadniStatus UPDATED_RADNI_STATUS = RadniStatus.ZAPOSLEN;

    private static final String ENTITY_API_URL = "/api/podaci-o-roditeljimas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PodaciORoditeljimaRepository podaciORoditeljimaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPodaciORoditeljimaMockMvc;

    private PodaciORoditeljima podaciORoditeljima;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PodaciORoditeljima createEntity(EntityManager em) {
        PodaciORoditeljima podaciORoditeljima = new PodaciORoditeljima()
            .jmbg(DEFAULT_JMBG)
            .ime(DEFAULT_IME)
            .prezime(DEFAULT_PREZIME)
            .telefon(DEFAULT_TELEFON)
            .firma(DEFAULT_FIRMA)
            .radnoVreme(DEFAULT_RADNO_VREME)
            .radniStatus(DEFAULT_RADNI_STATUS);
        return podaciORoditeljima;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PodaciORoditeljima createUpdatedEntity(EntityManager em) {
        PodaciORoditeljima podaciORoditeljima = new PodaciORoditeljima()
            .jmbg(UPDATED_JMBG)
            .ime(UPDATED_IME)
            .prezime(UPDATED_PREZIME)
            .telefon(UPDATED_TELEFON)
            .firma(UPDATED_FIRMA)
            .radnoVreme(UPDATED_RADNO_VREME)
            .radniStatus(UPDATED_RADNI_STATUS);
        return podaciORoditeljima;
    }

    @BeforeEach
    public void initTest() {
        podaciORoditeljima = createEntity(em);
    }

    @Test
    @Transactional
    void createPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeCreate = podaciORoditeljimaRepository.findAll().size();
        // Create the PodaciORoditeljima
        restPodaciORoditeljimaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isCreated());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeCreate + 1);
        PodaciORoditeljima testPodaciORoditeljima = podaciORoditeljimaList.get(podaciORoditeljimaList.size() - 1);
        assertThat(testPodaciORoditeljima.getJmbg()).isEqualTo(DEFAULT_JMBG);
        assertThat(testPodaciORoditeljima.getIme()).isEqualTo(DEFAULT_IME);
        assertThat(testPodaciORoditeljima.getPrezime()).isEqualTo(DEFAULT_PREZIME);
        assertThat(testPodaciORoditeljima.getTelefon()).isEqualTo(DEFAULT_TELEFON);
        assertThat(testPodaciORoditeljima.getFirma()).isEqualTo(DEFAULT_FIRMA);
        assertThat(testPodaciORoditeljima.getRadnoVreme()).isEqualTo(DEFAULT_RADNO_VREME);
        assertThat(testPodaciORoditeljima.getRadniStatus()).isEqualTo(DEFAULT_RADNI_STATUS);
    }

    @Test
    @Transactional
    void createPodaciORoditeljimaWithExistingId() throws Exception {
        // Create the PodaciORoditeljima with an existing ID
        podaciORoditeljima.setId(1L);

        int databaseSizeBeforeCreate = podaciORoditeljimaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPodaciORoditeljimaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isBadRequest());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPodaciORoditeljimas() throws Exception {
        // Initialize the database
        podaciORoditeljimaRepository.saveAndFlush(podaciORoditeljima);

        // Get all the podaciORoditeljimaList
        restPodaciORoditeljimaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(podaciORoditeljima.getId().intValue())))
            .andExpect(jsonPath("$.[*].jmbg").value(hasItem(DEFAULT_JMBG)))
            .andExpect(jsonPath("$.[*].ime").value(hasItem(DEFAULT_IME)))
            .andExpect(jsonPath("$.[*].prezime").value(hasItem(DEFAULT_PREZIME)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)))
            .andExpect(jsonPath("$.[*].firma").value(hasItem(DEFAULT_FIRMA)))
            .andExpect(jsonPath("$.[*].radnoVreme").value(hasItem(DEFAULT_RADNO_VREME)))
            .andExpect(jsonPath("$.[*].radniStatus").value(hasItem(DEFAULT_RADNI_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPodaciORoditeljima() throws Exception {
        // Initialize the database
        podaciORoditeljimaRepository.saveAndFlush(podaciORoditeljima);

        // Get the podaciORoditeljima
        restPodaciORoditeljimaMockMvc
            .perform(get(ENTITY_API_URL_ID, podaciORoditeljima.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(podaciORoditeljima.getId().intValue()))
            .andExpect(jsonPath("$.jmbg").value(DEFAULT_JMBG))
            .andExpect(jsonPath("$.ime").value(DEFAULT_IME))
            .andExpect(jsonPath("$.prezime").value(DEFAULT_PREZIME))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON))
            .andExpect(jsonPath("$.firma").value(DEFAULT_FIRMA))
            .andExpect(jsonPath("$.radnoVreme").value(DEFAULT_RADNO_VREME))
            .andExpect(jsonPath("$.radniStatus").value(DEFAULT_RADNI_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPodaciORoditeljima() throws Exception {
        // Get the podaciORoditeljima
        restPodaciORoditeljimaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPodaciORoditeljima() throws Exception {
        // Initialize the database
        podaciORoditeljimaRepository.saveAndFlush(podaciORoditeljima);

        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();

        // Update the podaciORoditeljima
        PodaciORoditeljima updatedPodaciORoditeljima = podaciORoditeljimaRepository.findById(podaciORoditeljima.getId()).get();
        // Disconnect from session so that the updates on updatedPodaciORoditeljima are not directly saved in db
        em.detach(updatedPodaciORoditeljima);
        updatedPodaciORoditeljima
            .jmbg(UPDATED_JMBG)
            .ime(UPDATED_IME)
            .prezime(UPDATED_PREZIME)
            .telefon(UPDATED_TELEFON)
            .firma(UPDATED_FIRMA)
            .radnoVreme(UPDATED_RADNO_VREME)
            .radniStatus(UPDATED_RADNI_STATUS);

        restPodaciORoditeljimaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPodaciORoditeljima.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPodaciORoditeljima))
            )
            .andExpect(status().isOk());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
        PodaciORoditeljima testPodaciORoditeljima = podaciORoditeljimaList.get(podaciORoditeljimaList.size() - 1);
        assertThat(testPodaciORoditeljima.getJmbg()).isEqualTo(UPDATED_JMBG);
        assertThat(testPodaciORoditeljima.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testPodaciORoditeljima.getPrezime()).isEqualTo(UPDATED_PREZIME);
        assertThat(testPodaciORoditeljima.getTelefon()).isEqualTo(UPDATED_TELEFON);
        assertThat(testPodaciORoditeljima.getFirma()).isEqualTo(UPDATED_FIRMA);
        assertThat(testPodaciORoditeljima.getRadnoVreme()).isEqualTo(UPDATED_RADNO_VREME);
        assertThat(testPodaciORoditeljima.getRadniStatus()).isEqualTo(UPDATED_RADNI_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();
        podaciORoditeljima.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPodaciORoditeljimaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, podaciORoditeljima.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isBadRequest());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();
        podaciORoditeljima.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPodaciORoditeljimaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isBadRequest());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();
        podaciORoditeljima.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPodaciORoditeljimaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePodaciORoditeljimaWithPatch() throws Exception {
        // Initialize the database
        podaciORoditeljimaRepository.saveAndFlush(podaciORoditeljima);

        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();

        // Update the podaciORoditeljima using partial update
        PodaciORoditeljima partialUpdatedPodaciORoditeljima = new PodaciORoditeljima();
        partialUpdatedPodaciORoditeljima.setId(podaciORoditeljima.getId());

        partialUpdatedPodaciORoditeljima.jmbg(UPDATED_JMBG).ime(UPDATED_IME);

        restPodaciORoditeljimaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPodaciORoditeljima.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPodaciORoditeljima))
            )
            .andExpect(status().isOk());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
        PodaciORoditeljima testPodaciORoditeljima = podaciORoditeljimaList.get(podaciORoditeljimaList.size() - 1);
        assertThat(testPodaciORoditeljima.getJmbg()).isEqualTo(UPDATED_JMBG);
        assertThat(testPodaciORoditeljima.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testPodaciORoditeljima.getPrezime()).isEqualTo(DEFAULT_PREZIME);
        assertThat(testPodaciORoditeljima.getTelefon()).isEqualTo(DEFAULT_TELEFON);
        assertThat(testPodaciORoditeljima.getFirma()).isEqualTo(DEFAULT_FIRMA);
        assertThat(testPodaciORoditeljima.getRadnoVreme()).isEqualTo(DEFAULT_RADNO_VREME);
        assertThat(testPodaciORoditeljima.getRadniStatus()).isEqualTo(DEFAULT_RADNI_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePodaciORoditeljimaWithPatch() throws Exception {
        // Initialize the database
        podaciORoditeljimaRepository.saveAndFlush(podaciORoditeljima);

        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();

        // Update the podaciORoditeljima using partial update
        PodaciORoditeljima partialUpdatedPodaciORoditeljima = new PodaciORoditeljima();
        partialUpdatedPodaciORoditeljima.setId(podaciORoditeljima.getId());

        partialUpdatedPodaciORoditeljima
            .jmbg(UPDATED_JMBG)
            .ime(UPDATED_IME)
            .prezime(UPDATED_PREZIME)
            .telefon(UPDATED_TELEFON)
            .firma(UPDATED_FIRMA)
            .radnoVreme(UPDATED_RADNO_VREME)
            .radniStatus(UPDATED_RADNI_STATUS);

        restPodaciORoditeljimaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPodaciORoditeljima.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPodaciORoditeljima))
            )
            .andExpect(status().isOk());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
        PodaciORoditeljima testPodaciORoditeljima = podaciORoditeljimaList.get(podaciORoditeljimaList.size() - 1);
        assertThat(testPodaciORoditeljima.getJmbg()).isEqualTo(UPDATED_JMBG);
        assertThat(testPodaciORoditeljima.getIme()).isEqualTo(UPDATED_IME);
        assertThat(testPodaciORoditeljima.getPrezime()).isEqualTo(UPDATED_PREZIME);
        assertThat(testPodaciORoditeljima.getTelefon()).isEqualTo(UPDATED_TELEFON);
        assertThat(testPodaciORoditeljima.getFirma()).isEqualTo(UPDATED_FIRMA);
        assertThat(testPodaciORoditeljima.getRadnoVreme()).isEqualTo(UPDATED_RADNO_VREME);
        assertThat(testPodaciORoditeljima.getRadniStatus()).isEqualTo(UPDATED_RADNI_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();
        podaciORoditeljima.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPodaciORoditeljimaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, podaciORoditeljima.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isBadRequest());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();
        podaciORoditeljima.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPodaciORoditeljimaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isBadRequest());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPodaciORoditeljima() throws Exception {
        int databaseSizeBeforeUpdate = podaciORoditeljimaRepository.findAll().size();
        podaciORoditeljima.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPodaciORoditeljimaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(podaciORoditeljima))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PodaciORoditeljima in the database
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePodaciORoditeljima() throws Exception {
        // Initialize the database
        podaciORoditeljimaRepository.saveAndFlush(podaciORoditeljima);

        int databaseSizeBeforeDelete = podaciORoditeljimaRepository.findAll().size();

        // Delete the podaciORoditeljima
        restPodaciORoditeljimaMockMvc
            .perform(delete(ENTITY_API_URL_ID, podaciORoditeljima.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PodaciORoditeljima> podaciORoditeljimaList = podaciORoditeljimaRepository.findAll();
        assertThat(podaciORoditeljimaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
