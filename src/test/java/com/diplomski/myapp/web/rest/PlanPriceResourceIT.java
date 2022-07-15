package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.PlanPrice;
import com.diplomski.myapp.repository.PlanPriceRepository;
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
 * Integration tests for the {@link PlanPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanPriceResourceIT {

    private static final String DEFAULT_IZVORI_SAZNANJA = "AAAAAAAAAA";
    private static final String UPDATED_IZVORI_SAZNANJA = "BBBBBBBBBB";

    private static final String DEFAULT_NAZIV_TEME = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV_TEME = "BBBBBBBBBB";

    private static final String DEFAULT_POCETNA_IDEJA = "AAAAAAAAAA";
    private static final String UPDATED_POCETNA_IDEJA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATUM_POCETKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_POCETKA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NACIN_UCESCA_VASPITACA = "AAAAAAAAAA";
    private static final String UPDATED_NACIN_UCESCA_VASPITACA = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIJALI = "AAAAAAAAAA";
    private static final String UPDATED_MATERIJALI = "BBBBBBBBBB";

    private static final String DEFAULT_UCESCE_PORODICE = "AAAAAAAAAA";
    private static final String UPDATED_UCESCE_PORODICE = "BBBBBBBBBB";

    private static final String DEFAULT_MESTA = "AAAAAAAAAA";
    private static final String UPDATED_MESTA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATUM_ZAVRSETKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_ZAVRSETKA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/plan-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanPriceRepository planPriceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanPriceMockMvc;

    private PlanPrice planPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanPrice createEntity(EntityManager em) {
        PlanPrice planPrice = new PlanPrice()
            .izvoriSaznanja(DEFAULT_IZVORI_SAZNANJA)
            .nazivTeme(DEFAULT_NAZIV_TEME)
            .pocetnaIdeja(DEFAULT_POCETNA_IDEJA)
            .datumPocetka(DEFAULT_DATUM_POCETKA)
            .nacinUcescaVaspitaca(DEFAULT_NACIN_UCESCA_VASPITACA)
            .materijali(DEFAULT_MATERIJALI)
            .ucescePorodice(DEFAULT_UCESCE_PORODICE)
            .mesta(DEFAULT_MESTA)
            .datumZavrsetka(DEFAULT_DATUM_ZAVRSETKA);
        return planPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanPrice createUpdatedEntity(EntityManager em) {
        PlanPrice planPrice = new PlanPrice()
            .izvoriSaznanja(UPDATED_IZVORI_SAZNANJA)
            .nazivTeme(UPDATED_NAZIV_TEME)
            .pocetnaIdeja(UPDATED_POCETNA_IDEJA)
            .datumPocetka(UPDATED_DATUM_POCETKA)
            .nacinUcescaVaspitaca(UPDATED_NACIN_UCESCA_VASPITACA)
            .materijali(UPDATED_MATERIJALI)
            .ucescePorodice(UPDATED_UCESCE_PORODICE)
            .mesta(UPDATED_MESTA)
            .datumZavrsetka(UPDATED_DATUM_ZAVRSETKA);
        return planPrice;
    }

    @BeforeEach
    public void initTest() {
        planPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createPlanPrice() throws Exception {
        int databaseSizeBeforeCreate = planPriceRepository.findAll().size();
        // Create the PlanPrice
        restPlanPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planPrice)))
            .andExpect(status().isCreated());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeCreate + 1);
        PlanPrice testPlanPrice = planPriceList.get(planPriceList.size() - 1);
        assertThat(testPlanPrice.getIzvoriSaznanja()).isEqualTo(DEFAULT_IZVORI_SAZNANJA);
        assertThat(testPlanPrice.getNazivTeme()).isEqualTo(DEFAULT_NAZIV_TEME);
        assertThat(testPlanPrice.getPocetnaIdeja()).isEqualTo(DEFAULT_POCETNA_IDEJA);
        assertThat(testPlanPrice.getDatumPocetka()).isEqualTo(DEFAULT_DATUM_POCETKA);
        assertThat(testPlanPrice.getNacinUcescaVaspitaca()).isEqualTo(DEFAULT_NACIN_UCESCA_VASPITACA);
        assertThat(testPlanPrice.getMaterijali()).isEqualTo(DEFAULT_MATERIJALI);
        assertThat(testPlanPrice.getUcescePorodice()).isEqualTo(DEFAULT_UCESCE_PORODICE);
        assertThat(testPlanPrice.getMesta()).isEqualTo(DEFAULT_MESTA);
        assertThat(testPlanPrice.getDatumZavrsetka()).isEqualTo(DEFAULT_DATUM_ZAVRSETKA);
    }

    @Test
    @Transactional
    void createPlanPriceWithExistingId() throws Exception {
        // Create the PlanPrice with an existing ID
        planPrice.setId(1L);

        int databaseSizeBeforeCreate = planPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planPrice)))
            .andExpect(status().isBadRequest());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlanPrices() throws Exception {
        // Initialize the database
        planPriceRepository.saveAndFlush(planPrice);

        // Get all the planPriceList
        restPlanPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].izvoriSaznanja").value(hasItem(DEFAULT_IZVORI_SAZNANJA)))
            .andExpect(jsonPath("$.[*].nazivTeme").value(hasItem(DEFAULT_NAZIV_TEME)))
            .andExpect(jsonPath("$.[*].pocetnaIdeja").value(hasItem(DEFAULT_POCETNA_IDEJA)))
            .andExpect(jsonPath("$.[*].datumPocetka").value(hasItem(DEFAULT_DATUM_POCETKA.toString())))
            .andExpect(jsonPath("$.[*].nacinUcescaVaspitaca").value(hasItem(DEFAULT_NACIN_UCESCA_VASPITACA)))
            .andExpect(jsonPath("$.[*].materijali").value(hasItem(DEFAULT_MATERIJALI)))
            .andExpect(jsonPath("$.[*].ucescePorodice").value(hasItem(DEFAULT_UCESCE_PORODICE)))
            .andExpect(jsonPath("$.[*].mesta").value(hasItem(DEFAULT_MESTA)))
            .andExpect(jsonPath("$.[*].datumZavrsetka").value(hasItem(DEFAULT_DATUM_ZAVRSETKA.toString())));
    }

    @Test
    @Transactional
    void getPlanPrice() throws Exception {
        // Initialize the database
        planPriceRepository.saveAndFlush(planPrice);

        // Get the planPrice
        restPlanPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, planPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planPrice.getId().intValue()))
            .andExpect(jsonPath("$.izvoriSaznanja").value(DEFAULT_IZVORI_SAZNANJA))
            .andExpect(jsonPath("$.nazivTeme").value(DEFAULT_NAZIV_TEME))
            .andExpect(jsonPath("$.pocetnaIdeja").value(DEFAULT_POCETNA_IDEJA))
            .andExpect(jsonPath("$.datumPocetka").value(DEFAULT_DATUM_POCETKA.toString()))
            .andExpect(jsonPath("$.nacinUcescaVaspitaca").value(DEFAULT_NACIN_UCESCA_VASPITACA))
            .andExpect(jsonPath("$.materijali").value(DEFAULT_MATERIJALI))
            .andExpect(jsonPath("$.ucescePorodice").value(DEFAULT_UCESCE_PORODICE))
            .andExpect(jsonPath("$.mesta").value(DEFAULT_MESTA))
            .andExpect(jsonPath("$.datumZavrsetka").value(DEFAULT_DATUM_ZAVRSETKA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlanPrice() throws Exception {
        // Get the planPrice
        restPlanPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlanPrice() throws Exception {
        // Initialize the database
        planPriceRepository.saveAndFlush(planPrice);

        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();

        // Update the planPrice
        PlanPrice updatedPlanPrice = planPriceRepository.findById(planPrice.getId()).get();
        // Disconnect from session so that the updates on updatedPlanPrice are not directly saved in db
        em.detach(updatedPlanPrice);
        updatedPlanPrice
            .izvoriSaznanja(UPDATED_IZVORI_SAZNANJA)
            .nazivTeme(UPDATED_NAZIV_TEME)
            .pocetnaIdeja(UPDATED_POCETNA_IDEJA)
            .datumPocetka(UPDATED_DATUM_POCETKA)
            .nacinUcescaVaspitaca(UPDATED_NACIN_UCESCA_VASPITACA)
            .materijali(UPDATED_MATERIJALI)
            .ucescePorodice(UPDATED_UCESCE_PORODICE)
            .mesta(UPDATED_MESTA)
            .datumZavrsetka(UPDATED_DATUM_ZAVRSETKA);

        restPlanPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlanPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlanPrice))
            )
            .andExpect(status().isOk());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
        PlanPrice testPlanPrice = planPriceList.get(planPriceList.size() - 1);
        assertThat(testPlanPrice.getIzvoriSaznanja()).isEqualTo(UPDATED_IZVORI_SAZNANJA);
        assertThat(testPlanPrice.getNazivTeme()).isEqualTo(UPDATED_NAZIV_TEME);
        assertThat(testPlanPrice.getPocetnaIdeja()).isEqualTo(UPDATED_POCETNA_IDEJA);
        assertThat(testPlanPrice.getDatumPocetka()).isEqualTo(UPDATED_DATUM_POCETKA);
        assertThat(testPlanPrice.getNacinUcescaVaspitaca()).isEqualTo(UPDATED_NACIN_UCESCA_VASPITACA);
        assertThat(testPlanPrice.getMaterijali()).isEqualTo(UPDATED_MATERIJALI);
        assertThat(testPlanPrice.getUcescePorodice()).isEqualTo(UPDATED_UCESCE_PORODICE);
        assertThat(testPlanPrice.getMesta()).isEqualTo(UPDATED_MESTA);
        assertThat(testPlanPrice.getDatumZavrsetka()).isEqualTo(UPDATED_DATUM_ZAVRSETKA);
    }

    @Test
    @Transactional
    void putNonExistingPlanPrice() throws Exception {
        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();
        planPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanPrice() throws Exception {
        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();
        planPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanPrice() throws Exception {
        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();
        planPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planPrice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanPriceWithPatch() throws Exception {
        // Initialize the database
        planPriceRepository.saveAndFlush(planPrice);

        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();

        // Update the planPrice using partial update
        PlanPrice partialUpdatedPlanPrice = new PlanPrice();
        partialUpdatedPlanPrice.setId(planPrice.getId());

        partialUpdatedPlanPrice.nazivTeme(UPDATED_NAZIV_TEME).ucescePorodice(UPDATED_UCESCE_PORODICE);

        restPlanPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanPrice))
            )
            .andExpect(status().isOk());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
        PlanPrice testPlanPrice = planPriceList.get(planPriceList.size() - 1);
        assertThat(testPlanPrice.getIzvoriSaznanja()).isEqualTo(DEFAULT_IZVORI_SAZNANJA);
        assertThat(testPlanPrice.getNazivTeme()).isEqualTo(UPDATED_NAZIV_TEME);
        assertThat(testPlanPrice.getPocetnaIdeja()).isEqualTo(DEFAULT_POCETNA_IDEJA);
        assertThat(testPlanPrice.getDatumPocetka()).isEqualTo(DEFAULT_DATUM_POCETKA);
        assertThat(testPlanPrice.getNacinUcescaVaspitaca()).isEqualTo(DEFAULT_NACIN_UCESCA_VASPITACA);
        assertThat(testPlanPrice.getMaterijali()).isEqualTo(DEFAULT_MATERIJALI);
        assertThat(testPlanPrice.getUcescePorodice()).isEqualTo(UPDATED_UCESCE_PORODICE);
        assertThat(testPlanPrice.getMesta()).isEqualTo(DEFAULT_MESTA);
        assertThat(testPlanPrice.getDatumZavrsetka()).isEqualTo(DEFAULT_DATUM_ZAVRSETKA);
    }

    @Test
    @Transactional
    void fullUpdatePlanPriceWithPatch() throws Exception {
        // Initialize the database
        planPriceRepository.saveAndFlush(planPrice);

        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();

        // Update the planPrice using partial update
        PlanPrice partialUpdatedPlanPrice = new PlanPrice();
        partialUpdatedPlanPrice.setId(planPrice.getId());

        partialUpdatedPlanPrice
            .izvoriSaznanja(UPDATED_IZVORI_SAZNANJA)
            .nazivTeme(UPDATED_NAZIV_TEME)
            .pocetnaIdeja(UPDATED_POCETNA_IDEJA)
            .datumPocetka(UPDATED_DATUM_POCETKA)
            .nacinUcescaVaspitaca(UPDATED_NACIN_UCESCA_VASPITACA)
            .materijali(UPDATED_MATERIJALI)
            .ucescePorodice(UPDATED_UCESCE_PORODICE)
            .mesta(UPDATED_MESTA)
            .datumZavrsetka(UPDATED_DATUM_ZAVRSETKA);

        restPlanPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanPrice))
            )
            .andExpect(status().isOk());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
        PlanPrice testPlanPrice = planPriceList.get(planPriceList.size() - 1);
        assertThat(testPlanPrice.getIzvoriSaznanja()).isEqualTo(UPDATED_IZVORI_SAZNANJA);
        assertThat(testPlanPrice.getNazivTeme()).isEqualTo(UPDATED_NAZIV_TEME);
        assertThat(testPlanPrice.getPocetnaIdeja()).isEqualTo(UPDATED_POCETNA_IDEJA);
        assertThat(testPlanPrice.getDatumPocetka()).isEqualTo(UPDATED_DATUM_POCETKA);
        assertThat(testPlanPrice.getNacinUcescaVaspitaca()).isEqualTo(UPDATED_NACIN_UCESCA_VASPITACA);
        assertThat(testPlanPrice.getMaterijali()).isEqualTo(UPDATED_MATERIJALI);
        assertThat(testPlanPrice.getUcescePorodice()).isEqualTo(UPDATED_UCESCE_PORODICE);
        assertThat(testPlanPrice.getMesta()).isEqualTo(UPDATED_MESTA);
        assertThat(testPlanPrice.getDatumZavrsetka()).isEqualTo(UPDATED_DATUM_ZAVRSETKA);
    }

    @Test
    @Transactional
    void patchNonExistingPlanPrice() throws Exception {
        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();
        planPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanPrice() throws Exception {
        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();
        planPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanPrice() throws Exception {
        int databaseSizeBeforeUpdate = planPriceRepository.findAll().size();
        planPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanPriceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(planPrice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanPrice in the database
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanPrice() throws Exception {
        // Initialize the database
        planPriceRepository.saveAndFlush(planPrice);

        int databaseSizeBeforeDelete = planPriceRepository.findAll().size();

        // Delete the planPrice
        restPlanPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, planPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanPrice> planPriceList = planPriceRepository.findAll();
        assertThat(planPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
