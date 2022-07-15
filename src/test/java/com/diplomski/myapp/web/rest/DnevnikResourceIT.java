package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.repository.DnevnikRepository;
import com.diplomski.myapp.service.DnevnikService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DnevnikResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DnevnikResourceIT {

    private static final LocalDate DEFAULT_POCETAK_VAZENJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_POCETAK_VAZENJA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_KRAJ_VAZENJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_KRAJ_VAZENJA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/dnevniks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DnevnikRepository dnevnikRepository;

    @Mock
    private DnevnikRepository dnevnikRepositoryMock;

    @Mock
    private DnevnikService dnevnikServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDnevnikMockMvc;

    private Dnevnik dnevnik;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dnevnik createEntity(EntityManager em) {
        Dnevnik dnevnik = new Dnevnik().pocetakVazenja(DEFAULT_POCETAK_VAZENJA).krajVazenja(DEFAULT_KRAJ_VAZENJA);
        return dnevnik;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dnevnik createUpdatedEntity(EntityManager em) {
        Dnevnik dnevnik = new Dnevnik().pocetakVazenja(UPDATED_POCETAK_VAZENJA).krajVazenja(UPDATED_KRAJ_VAZENJA);
        return dnevnik;
    }

    @BeforeEach
    public void initTest() {
        dnevnik = createEntity(em);
    }

    @Test
    @Transactional
    void createDnevnik() throws Exception {
        int databaseSizeBeforeCreate = dnevnikRepository.findAll().size();
        // Create the Dnevnik
        restDnevnikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dnevnik)))
            .andExpect(status().isCreated());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeCreate + 1);
        Dnevnik testDnevnik = dnevnikList.get(dnevnikList.size() - 1);
        assertThat(testDnevnik.getPocetakVazenja()).isEqualTo(DEFAULT_POCETAK_VAZENJA);
        assertThat(testDnevnik.getKrajVazenja()).isEqualTo(DEFAULT_KRAJ_VAZENJA);
    }

    @Test
    @Transactional
    void createDnevnikWithExistingId() throws Exception {
        // Create the Dnevnik with an existing ID
        dnevnik.setId(1L);

        int databaseSizeBeforeCreate = dnevnikRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDnevnikMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dnevnik)))
            .andExpect(status().isBadRequest());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDnevniks() throws Exception {
        // Initialize the database
        dnevnikRepository.saveAndFlush(dnevnik);

        // Get all the dnevnikList
        restDnevnikMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dnevnik.getId().intValue())))
            .andExpect(jsonPath("$.[*].pocetakVazenja").value(hasItem(DEFAULT_POCETAK_VAZENJA.toString())))
            .andExpect(jsonPath("$.[*].krajVazenja").value(hasItem(DEFAULT_KRAJ_VAZENJA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDnevniksWithEagerRelationshipsIsEnabled() throws Exception {
        when(dnevnikServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDnevnikMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dnevnikServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDnevniksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dnevnikServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDnevnikMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dnevnikServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDnevnik() throws Exception {
        // Initialize the database
        dnevnikRepository.saveAndFlush(dnevnik);

        // Get the dnevnik
        restDnevnikMockMvc
            .perform(get(ENTITY_API_URL_ID, dnevnik.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dnevnik.getId().intValue()))
            .andExpect(jsonPath("$.pocetakVazenja").value(DEFAULT_POCETAK_VAZENJA.toString()))
            .andExpect(jsonPath("$.krajVazenja").value(DEFAULT_KRAJ_VAZENJA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDnevnik() throws Exception {
        // Get the dnevnik
        restDnevnikMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDnevnik() throws Exception {
        // Initialize the database
        dnevnikRepository.saveAndFlush(dnevnik);

        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();

        // Update the dnevnik
        Dnevnik updatedDnevnik = dnevnikRepository.findById(dnevnik.getId()).get();
        // Disconnect from session so that the updates on updatedDnevnik are not directly saved in db
        em.detach(updatedDnevnik);
        updatedDnevnik.pocetakVazenja(UPDATED_POCETAK_VAZENJA).krajVazenja(UPDATED_KRAJ_VAZENJA);

        restDnevnikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDnevnik.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDnevnik))
            )
            .andExpect(status().isOk());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
        Dnevnik testDnevnik = dnevnikList.get(dnevnikList.size() - 1);
        assertThat(testDnevnik.getPocetakVazenja()).isEqualTo(UPDATED_POCETAK_VAZENJA);
        assertThat(testDnevnik.getKrajVazenja()).isEqualTo(UPDATED_KRAJ_VAZENJA);
    }

    @Test
    @Transactional
    void putNonExistingDnevnik() throws Exception {
        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();
        dnevnik.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDnevnikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dnevnik.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dnevnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDnevnik() throws Exception {
        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();
        dnevnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDnevnikMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dnevnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDnevnik() throws Exception {
        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();
        dnevnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDnevnikMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dnevnik)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDnevnikWithPatch() throws Exception {
        // Initialize the database
        dnevnikRepository.saveAndFlush(dnevnik);

        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();

        // Update the dnevnik using partial update
        Dnevnik partialUpdatedDnevnik = new Dnevnik();
        partialUpdatedDnevnik.setId(dnevnik.getId());

        restDnevnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDnevnik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDnevnik))
            )
            .andExpect(status().isOk());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
        Dnevnik testDnevnik = dnevnikList.get(dnevnikList.size() - 1);
        assertThat(testDnevnik.getPocetakVazenja()).isEqualTo(DEFAULT_POCETAK_VAZENJA);
        assertThat(testDnevnik.getKrajVazenja()).isEqualTo(DEFAULT_KRAJ_VAZENJA);
    }

    @Test
    @Transactional
    void fullUpdateDnevnikWithPatch() throws Exception {
        // Initialize the database
        dnevnikRepository.saveAndFlush(dnevnik);

        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();

        // Update the dnevnik using partial update
        Dnevnik partialUpdatedDnevnik = new Dnevnik();
        partialUpdatedDnevnik.setId(dnevnik.getId());

        partialUpdatedDnevnik.pocetakVazenja(UPDATED_POCETAK_VAZENJA).krajVazenja(UPDATED_KRAJ_VAZENJA);

        restDnevnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDnevnik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDnevnik))
            )
            .andExpect(status().isOk());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
        Dnevnik testDnevnik = dnevnikList.get(dnevnikList.size() - 1);
        assertThat(testDnevnik.getPocetakVazenja()).isEqualTo(UPDATED_POCETAK_VAZENJA);
        assertThat(testDnevnik.getKrajVazenja()).isEqualTo(UPDATED_KRAJ_VAZENJA);
    }

    @Test
    @Transactional
    void patchNonExistingDnevnik() throws Exception {
        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();
        dnevnik.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDnevnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dnevnik.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dnevnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDnevnik() throws Exception {
        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();
        dnevnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDnevnikMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dnevnik))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDnevnik() throws Exception {
        int databaseSizeBeforeUpdate = dnevnikRepository.findAll().size();
        dnevnik.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDnevnikMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dnevnik)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dnevnik in the database
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDnevnik() throws Exception {
        // Initialize the database
        dnevnikRepository.saveAndFlush(dnevnik);

        int databaseSizeBeforeDelete = dnevnikRepository.findAll().size();

        // Delete the dnevnik
        restDnevnikMockMvc
            .perform(delete(ENTITY_API_URL_ID, dnevnik.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dnevnik> dnevnikList = dnevnikRepository.findAll();
        assertThat(dnevnikList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
