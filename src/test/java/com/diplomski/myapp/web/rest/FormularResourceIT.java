package com.diplomski.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diplomski.myapp.IntegrationTest;
import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.domain.enumeration.StatusFormulara;
import com.diplomski.myapp.domain.enumeration.TipGrupe;
import com.diplomski.myapp.repository.FormularRepository;
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
 * Integration tests for the {@link FormularResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormularResourceIT {

    private static final Integer DEFAULT_MESEC_UPISA = 1;
    private static final Integer UPDATED_MESEC_UPISA = 2;

    private static final String DEFAULT_JMBG = "AAAAAAAAAA";
    private static final String UPDATED_JMBG = "BBBBBBBBBB";

    private static final String DEFAULT_DATUM_RODJENJA = "AAAAAAAAAA";
    private static final String UPDATED_DATUM_RODJENJA = "BBBBBBBBBB";

    private static final String DEFAULT_IME_DETETA = "AAAAAAAAAA";
    private static final String UPDATED_IME_DETETA = "BBBBBBBBBB";

    private static final String DEFAULT_MESTO_RODJENJA = "AAAAAAAAAA";
    private static final String UPDATED_MESTO_RODJENJA = "BBBBBBBBBB";

    private static final String DEFAULT_OPSTINA_RODJENJA = "AAAAAAAAAA";
    private static final String UPDATED_OPSTINA_RODJENJA = "BBBBBBBBBB";

    private static final String DEFAULT_DRZAVA = "AAAAAAAAAA";
    private static final String UPDATED_DRZAVA = "BBBBBBBBBB";

    private static final Integer DEFAULT_BR_DECE_U_PORODICI = 1;
    private static final Integer UPDATED_BR_DECE_U_PORODICI = 2;

    private static final String DEFAULT_ZDRAVSTVENI_PROBLEMI = "AAAAAAAAAA";
    private static final String UPDATED_ZDRAVSTVENI_PROBLEMI = "BBBBBBBBBB";

    private static final String DEFAULT_ZDRAVSTVENI_USLOVI = "AAAAAAAAAA";
    private static final String UPDATED_ZDRAVSTVENI_USLOVI = "BBBBBBBBBB";

    private static final StatusFormulara DEFAULT_STATUS_FORMULARA = StatusFormulara.NOV;
    private static final StatusFormulara UPDATED_STATUS_FORMULARA = StatusFormulara.ODOBREN;

    private static final TipGrupe DEFAULT_TIP_GRUPE = TipGrupe.POLUDNEVNA;
    private static final TipGrupe UPDATED_TIP_GRUPE = TipGrupe.JASLICE;

    private static final String ENTITY_API_URL = "/api/formulars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormularRepository formularRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormularMockMvc;

    private Formular formular;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formular createEntity(EntityManager em) {
        Formular formular = new Formular()
            .mesecUpisa(DEFAULT_MESEC_UPISA)
            .jmbg(DEFAULT_JMBG)
            .datumRodjenja(DEFAULT_DATUM_RODJENJA)
            .imeDeteta(DEFAULT_IME_DETETA)
            .mestoRodjenja(DEFAULT_MESTO_RODJENJA)
            .opstinaRodjenja(DEFAULT_OPSTINA_RODJENJA)
            .drzava(DEFAULT_DRZAVA)
            .brDeceUPorodici(DEFAULT_BR_DECE_U_PORODICI)
            .zdravstveniProblemi(DEFAULT_ZDRAVSTVENI_PROBLEMI)
            .zdravstveniUslovi(DEFAULT_ZDRAVSTVENI_USLOVI)
            .statusFormulara(DEFAULT_STATUS_FORMULARA)
            .tipGrupe(DEFAULT_TIP_GRUPE);
        return formular;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formular createUpdatedEntity(EntityManager em) {
        Formular formular = new Formular()
            .mesecUpisa(UPDATED_MESEC_UPISA)
            .jmbg(UPDATED_JMBG)
            .datumRodjenja(UPDATED_DATUM_RODJENJA)
            .imeDeteta(UPDATED_IME_DETETA)
            .mestoRodjenja(UPDATED_MESTO_RODJENJA)
            .opstinaRodjenja(UPDATED_OPSTINA_RODJENJA)
            .drzava(UPDATED_DRZAVA)
            .brDeceUPorodici(UPDATED_BR_DECE_U_PORODICI)
            .zdravstveniProblemi(UPDATED_ZDRAVSTVENI_PROBLEMI)
            .zdravstveniUslovi(UPDATED_ZDRAVSTVENI_USLOVI)
            .statusFormulara(UPDATED_STATUS_FORMULARA)
            .tipGrupe(UPDATED_TIP_GRUPE);
        return formular;
    }

    @BeforeEach
    public void initTest() {
        formular = createEntity(em);
    }

    @Test
    @Transactional
    void createFormular() throws Exception {
        int databaseSizeBeforeCreate = formularRepository.findAll().size();
        // Create the Formular
        restFormularMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formular)))
            .andExpect(status().isCreated());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeCreate + 1);
        Formular testFormular = formularList.get(formularList.size() - 1);
        assertThat(testFormular.getMesecUpisa()).isEqualTo(DEFAULT_MESEC_UPISA);
        assertThat(testFormular.getJmbg()).isEqualTo(DEFAULT_JMBG);
        assertThat(testFormular.getDatumRodjenja()).isEqualTo(DEFAULT_DATUM_RODJENJA);
        assertThat(testFormular.getImeDeteta()).isEqualTo(DEFAULT_IME_DETETA);
        assertThat(testFormular.getMestoRodjenja()).isEqualTo(DEFAULT_MESTO_RODJENJA);
        assertThat(testFormular.getOpstinaRodjenja()).isEqualTo(DEFAULT_OPSTINA_RODJENJA);
        assertThat(testFormular.getDrzava()).isEqualTo(DEFAULT_DRZAVA);
        assertThat(testFormular.getBrDeceUPorodici()).isEqualTo(DEFAULT_BR_DECE_U_PORODICI);
        assertThat(testFormular.getZdravstveniProblemi()).isEqualTo(DEFAULT_ZDRAVSTVENI_PROBLEMI);
        assertThat(testFormular.getZdravstveniUslovi()).isEqualTo(DEFAULT_ZDRAVSTVENI_USLOVI);
        assertThat(testFormular.getStatusFormulara()).isEqualTo(DEFAULT_STATUS_FORMULARA);
        assertThat(testFormular.getTipGrupe()).isEqualTo(DEFAULT_TIP_GRUPE);
    }

    @Test
    @Transactional
    void createFormularWithExistingId() throws Exception {
        // Create the Formular with an existing ID
        formular.setId(1L);

        int databaseSizeBeforeCreate = formularRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormularMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formular)))
            .andExpect(status().isBadRequest());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormulars() throws Exception {
        // Initialize the database
        formularRepository.saveAndFlush(formular);

        // Get all the formularList
        restFormularMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formular.getId().intValue())))
            .andExpect(jsonPath("$.[*].mesecUpisa").value(hasItem(DEFAULT_MESEC_UPISA)))
            .andExpect(jsonPath("$.[*].jmbg").value(hasItem(DEFAULT_JMBG)))
            .andExpect(jsonPath("$.[*].datumRodjenja").value(hasItem(DEFAULT_DATUM_RODJENJA)))
            .andExpect(jsonPath("$.[*].imeDeteta").value(hasItem(DEFAULT_IME_DETETA)))
            .andExpect(jsonPath("$.[*].mestoRodjenja").value(hasItem(DEFAULT_MESTO_RODJENJA)))
            .andExpect(jsonPath("$.[*].opstinaRodjenja").value(hasItem(DEFAULT_OPSTINA_RODJENJA)))
            .andExpect(jsonPath("$.[*].drzava").value(hasItem(DEFAULT_DRZAVA)))
            .andExpect(jsonPath("$.[*].brDeceUPorodici").value(hasItem(DEFAULT_BR_DECE_U_PORODICI)))
            .andExpect(jsonPath("$.[*].zdravstveniProblemi").value(hasItem(DEFAULT_ZDRAVSTVENI_PROBLEMI)))
            .andExpect(jsonPath("$.[*].zdravstveniUslovi").value(hasItem(DEFAULT_ZDRAVSTVENI_USLOVI)))
            .andExpect(jsonPath("$.[*].statusFormulara").value(hasItem(DEFAULT_STATUS_FORMULARA.toString())))
            .andExpect(jsonPath("$.[*].tipGrupe").value(hasItem(DEFAULT_TIP_GRUPE.toString())));
    }

    @Test
    @Transactional
    void getFormular() throws Exception {
        // Initialize the database
        formularRepository.saveAndFlush(formular);

        // Get the formular
        restFormularMockMvc
            .perform(get(ENTITY_API_URL_ID, formular.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formular.getId().intValue()))
            .andExpect(jsonPath("$.mesecUpisa").value(DEFAULT_MESEC_UPISA))
            .andExpect(jsonPath("$.jmbg").value(DEFAULT_JMBG))
            .andExpect(jsonPath("$.datumRodjenja").value(DEFAULT_DATUM_RODJENJA))
            .andExpect(jsonPath("$.imeDeteta").value(DEFAULT_IME_DETETA))
            .andExpect(jsonPath("$.mestoRodjenja").value(DEFAULT_MESTO_RODJENJA))
            .andExpect(jsonPath("$.opstinaRodjenja").value(DEFAULT_OPSTINA_RODJENJA))
            .andExpect(jsonPath("$.drzava").value(DEFAULT_DRZAVA))
            .andExpect(jsonPath("$.brDeceUPorodici").value(DEFAULT_BR_DECE_U_PORODICI))
            .andExpect(jsonPath("$.zdravstveniProblemi").value(DEFAULT_ZDRAVSTVENI_PROBLEMI))
            .andExpect(jsonPath("$.zdravstveniUslovi").value(DEFAULT_ZDRAVSTVENI_USLOVI))
            .andExpect(jsonPath("$.statusFormulara").value(DEFAULT_STATUS_FORMULARA.toString()))
            .andExpect(jsonPath("$.tipGrupe").value(DEFAULT_TIP_GRUPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFormular() throws Exception {
        // Get the formular
        restFormularMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormular() throws Exception {
        // Initialize the database
        formularRepository.saveAndFlush(formular);

        int databaseSizeBeforeUpdate = formularRepository.findAll().size();

        // Update the formular
        Formular updatedFormular = formularRepository.findById(formular.getId()).get();
        // Disconnect from session so that the updates on updatedFormular are not directly saved in db
        em.detach(updatedFormular);
        updatedFormular
            .mesecUpisa(UPDATED_MESEC_UPISA)
            .jmbg(UPDATED_JMBG)
            .datumRodjenja(UPDATED_DATUM_RODJENJA)
            .imeDeteta(UPDATED_IME_DETETA)
            .mestoRodjenja(UPDATED_MESTO_RODJENJA)
            .opstinaRodjenja(UPDATED_OPSTINA_RODJENJA)
            .drzava(UPDATED_DRZAVA)
            .brDeceUPorodici(UPDATED_BR_DECE_U_PORODICI)
            .zdravstveniProblemi(UPDATED_ZDRAVSTVENI_PROBLEMI)
            .zdravstveniUslovi(UPDATED_ZDRAVSTVENI_USLOVI)
            .statusFormulara(UPDATED_STATUS_FORMULARA)
            .tipGrupe(UPDATED_TIP_GRUPE);

        restFormularMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormular.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormular))
            )
            .andExpect(status().isOk());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
        Formular testFormular = formularList.get(formularList.size() - 1);
        assertThat(testFormular.getMesecUpisa()).isEqualTo(UPDATED_MESEC_UPISA);
        assertThat(testFormular.getJmbg()).isEqualTo(UPDATED_JMBG);
        assertThat(testFormular.getDatumRodjenja()).isEqualTo(UPDATED_DATUM_RODJENJA);
        assertThat(testFormular.getImeDeteta()).isEqualTo(UPDATED_IME_DETETA);
        assertThat(testFormular.getMestoRodjenja()).isEqualTo(UPDATED_MESTO_RODJENJA);
        assertThat(testFormular.getOpstinaRodjenja()).isEqualTo(UPDATED_OPSTINA_RODJENJA);
        assertThat(testFormular.getDrzava()).isEqualTo(UPDATED_DRZAVA);
        assertThat(testFormular.getBrDeceUPorodici()).isEqualTo(UPDATED_BR_DECE_U_PORODICI);
        assertThat(testFormular.getZdravstveniProblemi()).isEqualTo(UPDATED_ZDRAVSTVENI_PROBLEMI);
        assertThat(testFormular.getZdravstveniUslovi()).isEqualTo(UPDATED_ZDRAVSTVENI_USLOVI);
        assertThat(testFormular.getStatusFormulara()).isEqualTo(UPDATED_STATUS_FORMULARA);
        assertThat(testFormular.getTipGrupe()).isEqualTo(UPDATED_TIP_GRUPE);
    }

    @Test
    @Transactional
    void putNonExistingFormular() throws Exception {
        int databaseSizeBeforeUpdate = formularRepository.findAll().size();
        formular.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormularMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formular.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formular))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormular() throws Exception {
        int databaseSizeBeforeUpdate = formularRepository.findAll().size();
        formular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formular))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormular() throws Exception {
        int databaseSizeBeforeUpdate = formularRepository.findAll().size();
        formular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formular)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormularWithPatch() throws Exception {
        // Initialize the database
        formularRepository.saveAndFlush(formular);

        int databaseSizeBeforeUpdate = formularRepository.findAll().size();

        // Update the formular using partial update
        Formular partialUpdatedFormular = new Formular();
        partialUpdatedFormular.setId(formular.getId());

        partialUpdatedFormular
            .jmbg(UPDATED_JMBG)
            .datumRodjenja(UPDATED_DATUM_RODJENJA)
            .drzava(UPDATED_DRZAVA)
            .brDeceUPorodici(UPDATED_BR_DECE_U_PORODICI)
            .zdravstveniUslovi(UPDATED_ZDRAVSTVENI_USLOVI);

        restFormularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormular.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormular))
            )
            .andExpect(status().isOk());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
        Formular testFormular = formularList.get(formularList.size() - 1);
        assertThat(testFormular.getMesecUpisa()).isEqualTo(DEFAULT_MESEC_UPISA);
        assertThat(testFormular.getJmbg()).isEqualTo(UPDATED_JMBG);
        assertThat(testFormular.getDatumRodjenja()).isEqualTo(UPDATED_DATUM_RODJENJA);
        assertThat(testFormular.getImeDeteta()).isEqualTo(DEFAULT_IME_DETETA);
        assertThat(testFormular.getMestoRodjenja()).isEqualTo(DEFAULT_MESTO_RODJENJA);
        assertThat(testFormular.getOpstinaRodjenja()).isEqualTo(DEFAULT_OPSTINA_RODJENJA);
        assertThat(testFormular.getDrzava()).isEqualTo(UPDATED_DRZAVA);
        assertThat(testFormular.getBrDeceUPorodici()).isEqualTo(UPDATED_BR_DECE_U_PORODICI);
        assertThat(testFormular.getZdravstveniProblemi()).isEqualTo(DEFAULT_ZDRAVSTVENI_PROBLEMI);
        assertThat(testFormular.getZdravstveniUslovi()).isEqualTo(UPDATED_ZDRAVSTVENI_USLOVI);
        assertThat(testFormular.getStatusFormulara()).isEqualTo(DEFAULT_STATUS_FORMULARA);
        assertThat(testFormular.getTipGrupe()).isEqualTo(DEFAULT_TIP_GRUPE);
    }

    @Test
    @Transactional
    void fullUpdateFormularWithPatch() throws Exception {
        // Initialize the database
        formularRepository.saveAndFlush(formular);

        int databaseSizeBeforeUpdate = formularRepository.findAll().size();

        // Update the formular using partial update
        Formular partialUpdatedFormular = new Formular();
        partialUpdatedFormular.setId(formular.getId());

        partialUpdatedFormular
            .mesecUpisa(UPDATED_MESEC_UPISA)
            .jmbg(UPDATED_JMBG)
            .datumRodjenja(UPDATED_DATUM_RODJENJA)
            .imeDeteta(UPDATED_IME_DETETA)
            .mestoRodjenja(UPDATED_MESTO_RODJENJA)
            .opstinaRodjenja(UPDATED_OPSTINA_RODJENJA)
            .drzava(UPDATED_DRZAVA)
            .brDeceUPorodici(UPDATED_BR_DECE_U_PORODICI)
            .zdravstveniProblemi(UPDATED_ZDRAVSTVENI_PROBLEMI)
            .zdravstveniUslovi(UPDATED_ZDRAVSTVENI_USLOVI)
            .statusFormulara(UPDATED_STATUS_FORMULARA)
            .tipGrupe(UPDATED_TIP_GRUPE);

        restFormularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormular.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormular))
            )
            .andExpect(status().isOk());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
        Formular testFormular = formularList.get(formularList.size() - 1);
        assertThat(testFormular.getMesecUpisa()).isEqualTo(UPDATED_MESEC_UPISA);
        assertThat(testFormular.getJmbg()).isEqualTo(UPDATED_JMBG);
        assertThat(testFormular.getDatumRodjenja()).isEqualTo(UPDATED_DATUM_RODJENJA);
        assertThat(testFormular.getImeDeteta()).isEqualTo(UPDATED_IME_DETETA);
        assertThat(testFormular.getMestoRodjenja()).isEqualTo(UPDATED_MESTO_RODJENJA);
        assertThat(testFormular.getOpstinaRodjenja()).isEqualTo(UPDATED_OPSTINA_RODJENJA);
        assertThat(testFormular.getDrzava()).isEqualTo(UPDATED_DRZAVA);
        assertThat(testFormular.getBrDeceUPorodici()).isEqualTo(UPDATED_BR_DECE_U_PORODICI);
        assertThat(testFormular.getZdravstveniProblemi()).isEqualTo(UPDATED_ZDRAVSTVENI_PROBLEMI);
        assertThat(testFormular.getZdravstveniUslovi()).isEqualTo(UPDATED_ZDRAVSTVENI_USLOVI);
        assertThat(testFormular.getStatusFormulara()).isEqualTo(UPDATED_STATUS_FORMULARA);
        assertThat(testFormular.getTipGrupe()).isEqualTo(UPDATED_TIP_GRUPE);
    }

    @Test
    @Transactional
    void patchNonExistingFormular() throws Exception {
        int databaseSizeBeforeUpdate = formularRepository.findAll().size();
        formular.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formular.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formular))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormular() throws Exception {
        int databaseSizeBeforeUpdate = formularRepository.findAll().size();
        formular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formular))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormular() throws Exception {
        int databaseSizeBeforeUpdate = formularRepository.findAll().size();
        formular.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formular)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formular in the database
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormular() throws Exception {
        // Initialize the database
        formularRepository.saveAndFlush(formular);

        int databaseSizeBeforeDelete = formularRepository.findAll().size();

        // Delete the formular
        restFormularMockMvc
            .perform(delete(ENTITY_API_URL_ID, formular.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formular> formularList = formularRepository.findAll();
        assertThat(formularList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
