package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.ZapazanjeUVeziDeteta;
import com.diplomski.myapp.repository.ZapazanjeUVeziDetetaRepository;
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
 * Integration tests for the {@link ZapazanjeUVeziDetetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ZapazanjeUVeziDetetaResourceIT {

    private static final String DEFAULT_INTERESOVANJA = "AAAAAAAAAA";
    private static final String UPDATED_INTERESOVANJA = "BBBBBBBBBB";

    private static final String DEFAULT_PREDNOSTI = "AAAAAAAAAA";
    private static final String UPDATED_PREDNOSTI = "BBBBBBBBBB";

    private static final String DEFAULT_MANE = "AAAAAAAAAA";
    private static final String UPDATED_MANE = "BBBBBBBBBB";

    private static final String DEFAULT_PREDLOZI = "AAAAAAAAAA";
    private static final String UPDATED_PREDLOZI = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATUM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/zapazanje-u-vezi-detetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZapazanjeUVeziDetetaRepository zapazanjeUVeziDetetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZapazanjeUVeziDetetaMockMvc;

    private ZapazanjeUVeziDeteta zapazanjeUVeziDeteta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZapazanjeUVeziDeteta createEntity(EntityManager em) {
        ZapazanjeUVeziDeteta zapazanjeUVeziDeteta = new ZapazanjeUVeziDeteta()
            .interesovanja(DEFAULT_INTERESOVANJA)
            .prednosti(DEFAULT_PREDNOSTI)
            .mane(DEFAULT_MANE)
            .predlozi(DEFAULT_PREDLOZI)
            .datum(DEFAULT_DATUM);
        return zapazanjeUVeziDeteta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZapazanjeUVeziDeteta createUpdatedEntity(EntityManager em) {
        ZapazanjeUVeziDeteta zapazanjeUVeziDeteta = new ZapazanjeUVeziDeteta()
            .interesovanja(UPDATED_INTERESOVANJA)
            .prednosti(UPDATED_PREDNOSTI)
            .mane(UPDATED_MANE)
            .predlozi(UPDATED_PREDLOZI)
            .datum(UPDATED_DATUM);
        return zapazanjeUVeziDeteta;
    }

    @BeforeEach
    public void initTest() {
        zapazanjeUVeziDeteta = createEntity(em);
    }

    @Test
    @Transactional
    void createZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeCreate = zapazanjeUVeziDetetaRepository.findAll().size();
        // Create the ZapazanjeUVeziDeteta
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isCreated());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeCreate + 1);
        ZapazanjeUVeziDeteta testZapazanjeUVeziDeteta = zapazanjeUVeziDetetaList.get(zapazanjeUVeziDetetaList.size() - 1);
        assertThat(testZapazanjeUVeziDeteta.getInteresovanja()).isEqualTo(DEFAULT_INTERESOVANJA);
        assertThat(testZapazanjeUVeziDeteta.getPrednosti()).isEqualTo(DEFAULT_PREDNOSTI);
        assertThat(testZapazanjeUVeziDeteta.getMane()).isEqualTo(DEFAULT_MANE);
        assertThat(testZapazanjeUVeziDeteta.getPredlozi()).isEqualTo(DEFAULT_PREDLOZI);
        assertThat(testZapazanjeUVeziDeteta.getDatum()).isEqualTo(DEFAULT_DATUM);
    }

    @Test
    @Transactional
    void createZapazanjeUVeziDetetaWithExistingId() throws Exception {
        // Create the ZapazanjeUVeziDeteta with an existing ID
        zapazanjeUVeziDeteta.setId(1L);

        int databaseSizeBeforeCreate = zapazanjeUVeziDetetaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllZapazanjeUVeziDetetas() throws Exception {
        // Initialize the database
        zapazanjeUVeziDetetaRepository.saveAndFlush(zapazanjeUVeziDeteta);

        // Get all the zapazanjeUVeziDetetaList
        restZapazanjeUVeziDetetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zapazanjeUVeziDeteta.getId().intValue())))
            .andExpect(jsonPath("$.[*].interesovanja").value(hasItem(DEFAULT_INTERESOVANJA)))
            .andExpect(jsonPath("$.[*].prednosti").value(hasItem(DEFAULT_PREDNOSTI)))
            .andExpect(jsonPath("$.[*].mane").value(hasItem(DEFAULT_MANE)))
            .andExpect(jsonPath("$.[*].predlozi").value(hasItem(DEFAULT_PREDLOZI)))
            .andExpect(jsonPath("$.[*].datum").value(hasItem(DEFAULT_DATUM.toString())));
    }

    @Test
    @Transactional
    void getZapazanjeUVeziDeteta() throws Exception {
        // Initialize the database
        zapazanjeUVeziDetetaRepository.saveAndFlush(zapazanjeUVeziDeteta);

        // Get the zapazanjeUVeziDeteta
        restZapazanjeUVeziDetetaMockMvc
            .perform(get(ENTITY_API_URL_ID, zapazanjeUVeziDeteta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zapazanjeUVeziDeteta.getId().intValue()))
            .andExpect(jsonPath("$.interesovanja").value(DEFAULT_INTERESOVANJA))
            .andExpect(jsonPath("$.prednosti").value(DEFAULT_PREDNOSTI))
            .andExpect(jsonPath("$.mane").value(DEFAULT_MANE))
            .andExpect(jsonPath("$.predlozi").value(DEFAULT_PREDLOZI))
            .andExpect(jsonPath("$.datum").value(DEFAULT_DATUM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingZapazanjeUVeziDeteta() throws Exception {
        // Get the zapazanjeUVeziDeteta
        restZapazanjeUVeziDetetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewZapazanjeUVeziDeteta() throws Exception {
        // Initialize the database
        zapazanjeUVeziDetetaRepository.saveAndFlush(zapazanjeUVeziDeteta);

        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();

        // Update the zapazanjeUVeziDeteta
        ZapazanjeUVeziDeteta updatedZapazanjeUVeziDeteta = zapazanjeUVeziDetetaRepository.findById(zapazanjeUVeziDeteta.getId()).get();
        // Disconnect from session so that the updates on updatedZapazanjeUVeziDeteta are not directly saved in db
        em.detach(updatedZapazanjeUVeziDeteta);
        updatedZapazanjeUVeziDeteta
            .interesovanja(UPDATED_INTERESOVANJA)
            .prednosti(UPDATED_PREDNOSTI)
            .mane(UPDATED_MANE)
            .predlozi(UPDATED_PREDLOZI)
            .datum(UPDATED_DATUM);

        restZapazanjeUVeziDetetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedZapazanjeUVeziDeteta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedZapazanjeUVeziDeteta))
            )
            .andExpect(status().isOk());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
        ZapazanjeUVeziDeteta testZapazanjeUVeziDeteta = zapazanjeUVeziDetetaList.get(zapazanjeUVeziDetetaList.size() - 1);
        assertThat(testZapazanjeUVeziDeteta.getInteresovanja()).isEqualTo(UPDATED_INTERESOVANJA);
        assertThat(testZapazanjeUVeziDeteta.getPrednosti()).isEqualTo(UPDATED_PREDNOSTI);
        assertThat(testZapazanjeUVeziDeteta.getMane()).isEqualTo(UPDATED_MANE);
        assertThat(testZapazanjeUVeziDeteta.getPredlozi()).isEqualTo(UPDATED_PREDLOZI);
        assertThat(testZapazanjeUVeziDeteta.getDatum()).isEqualTo(UPDATED_DATUM);
    }

    @Test
    @Transactional
    void putNonExistingZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();
        zapazanjeUVeziDeteta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zapazanjeUVeziDeteta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();
        zapazanjeUVeziDeteta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();
        zapazanjeUVeziDeteta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZapazanjeUVeziDetetaWithPatch() throws Exception {
        // Initialize the database
        zapazanjeUVeziDetetaRepository.saveAndFlush(zapazanjeUVeziDeteta);

        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();

        // Update the zapazanjeUVeziDeteta using partial update
        ZapazanjeUVeziDeteta partialUpdatedZapazanjeUVeziDeteta = new ZapazanjeUVeziDeteta();
        partialUpdatedZapazanjeUVeziDeteta.setId(zapazanjeUVeziDeteta.getId());

        partialUpdatedZapazanjeUVeziDeteta.mane(UPDATED_MANE).predlozi(UPDATED_PREDLOZI).datum(UPDATED_DATUM);

        restZapazanjeUVeziDetetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZapazanjeUVeziDeteta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZapazanjeUVeziDeteta))
            )
            .andExpect(status().isOk());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
        ZapazanjeUVeziDeteta testZapazanjeUVeziDeteta = zapazanjeUVeziDetetaList.get(zapazanjeUVeziDetetaList.size() - 1);
        assertThat(testZapazanjeUVeziDeteta.getInteresovanja()).isEqualTo(DEFAULT_INTERESOVANJA);
        assertThat(testZapazanjeUVeziDeteta.getPrednosti()).isEqualTo(DEFAULT_PREDNOSTI);
        assertThat(testZapazanjeUVeziDeteta.getMane()).isEqualTo(UPDATED_MANE);
        assertThat(testZapazanjeUVeziDeteta.getPredlozi()).isEqualTo(UPDATED_PREDLOZI);
        assertThat(testZapazanjeUVeziDeteta.getDatum()).isEqualTo(UPDATED_DATUM);
    }

    @Test
    @Transactional
    void fullUpdateZapazanjeUVeziDetetaWithPatch() throws Exception {
        // Initialize the database
        zapazanjeUVeziDetetaRepository.saveAndFlush(zapazanjeUVeziDeteta);

        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();

        // Update the zapazanjeUVeziDeteta using partial update
        ZapazanjeUVeziDeteta partialUpdatedZapazanjeUVeziDeteta = new ZapazanjeUVeziDeteta();
        partialUpdatedZapazanjeUVeziDeteta.setId(zapazanjeUVeziDeteta.getId());

        partialUpdatedZapazanjeUVeziDeteta
            .interesovanja(UPDATED_INTERESOVANJA)
            .prednosti(UPDATED_PREDNOSTI)
            .mane(UPDATED_MANE)
            .predlozi(UPDATED_PREDLOZI)
            .datum(UPDATED_DATUM);

        restZapazanjeUVeziDetetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZapazanjeUVeziDeteta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZapazanjeUVeziDeteta))
            )
            .andExpect(status().isOk());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
        ZapazanjeUVeziDeteta testZapazanjeUVeziDeteta = zapazanjeUVeziDetetaList.get(zapazanjeUVeziDetetaList.size() - 1);
        assertThat(testZapazanjeUVeziDeteta.getInteresovanja()).isEqualTo(UPDATED_INTERESOVANJA);
        assertThat(testZapazanjeUVeziDeteta.getPrednosti()).isEqualTo(UPDATED_PREDNOSTI);
        assertThat(testZapazanjeUVeziDeteta.getMane()).isEqualTo(UPDATED_MANE);
        assertThat(testZapazanjeUVeziDeteta.getPredlozi()).isEqualTo(UPDATED_PREDLOZI);
        assertThat(testZapazanjeUVeziDeteta.getDatum()).isEqualTo(UPDATED_DATUM);
    }

    @Test
    @Transactional
    void patchNonExistingZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();
        zapazanjeUVeziDeteta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zapazanjeUVeziDeteta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();
        zapazanjeUVeziDeteta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZapazanjeUVeziDeteta() throws Exception {
        int databaseSizeBeforeUpdate = zapazanjeUVeziDetetaRepository.findAll().size();
        zapazanjeUVeziDeteta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZapazanjeUVeziDetetaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zapazanjeUVeziDeteta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZapazanjeUVeziDeteta in the database
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZapazanjeUVeziDeteta() throws Exception {
        // Initialize the database
        zapazanjeUVeziDetetaRepository.saveAndFlush(zapazanjeUVeziDeteta);

        int databaseSizeBeforeDelete = zapazanjeUVeziDetetaRepository.findAll().size();

        // Delete the zapazanjeUVeziDeteta
        restZapazanjeUVeziDetetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, zapazanjeUVeziDeteta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = zapazanjeUVeziDetetaRepository.findAll();
        assertThat(zapazanjeUVeziDetetaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
