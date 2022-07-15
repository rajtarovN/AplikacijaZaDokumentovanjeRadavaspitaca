package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.repository.FormularRepository;
import com.diplomski.myapp.service.FormularService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Formular}.
 */
@Service
@Transactional
public class FormularServiceImpl implements FormularService {

    private final Logger log = LoggerFactory.getLogger(FormularServiceImpl.class);

    private final FormularRepository formularRepository;

    public FormularServiceImpl(FormularRepository formularRepository) {
        this.formularRepository = formularRepository;
    }

    @Override
    public Formular save(Formular formular) {
        log.debug("Request to save Formular : {}", formular);
        return formularRepository.save(formular);
    }

    @Override
    public Formular update(Formular formular) {
        log.debug("Request to save Formular : {}", formular);
        return formularRepository.save(formular);
    }

    @Override
    public Optional<Formular> partialUpdate(Formular formular) {
        log.debug("Request to partially update Formular : {}", formular);

        return formularRepository
            .findById(formular.getId())
            .map(existingFormular -> {
                if (formular.getMesecUpisa() != null) {
                    existingFormular.setMesecUpisa(formular.getMesecUpisa());
                }
                if (formular.getJmbg() != null) {
                    existingFormular.setJmbg(formular.getJmbg());
                }
                if (formular.getDatumRodjenja() != null) {
                    existingFormular.setDatumRodjenja(formular.getDatumRodjenja());
                }
                if (formular.getImeDeteta() != null) {
                    existingFormular.setImeDeteta(formular.getImeDeteta());
                }
                if (formular.getMestoRodjenja() != null) {
                    existingFormular.setMestoRodjenja(formular.getMestoRodjenja());
                }
                if (formular.getOpstinaRodjenja() != null) {
                    existingFormular.setOpstinaRodjenja(formular.getOpstinaRodjenja());
                }
                if (formular.getDrzava() != null) {
                    existingFormular.setDrzava(formular.getDrzava());
                }
                if (formular.getBrDeceUPorodici() != null) {
                    existingFormular.setBrDeceUPorodici(formular.getBrDeceUPorodici());
                }
                if (formular.getZdravstveniProblemi() != null) {
                    existingFormular.setZdravstveniProblemi(formular.getZdravstveniProblemi());
                }
                if (formular.getZdravstveniUslovi() != null) {
                    existingFormular.setZdravstveniUslovi(formular.getZdravstveniUslovi());
                }
                if (formular.getStatusFormulara() != null) {
                    existingFormular.setStatusFormulara(formular.getStatusFormulara());
                }
                if (formular.getTipGrupe() != null) {
                    existingFormular.setTipGrupe(formular.getTipGrupe());
                }

                return existingFormular;
            })
            .map(formularRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Formular> findAll(Pageable pageable) {
        log.debug("Request to get all Formulars");
        return formularRepository.findAll(pageable);
    }

    /**
     *  Get all the formulars where Dete is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Formular> findAllWhereDeteIsNull() {
        log.debug("Request to get all formulars where Dete is null");
        return StreamSupport
            .stream(formularRepository.findAll().spliterator(), false)
            .filter(formular -> formular.getDete() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Formular> findOne(Long id) {
        log.debug("Request to get Formular : {}", id);
        return formularRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formular : {}", id);
        formularRepository.deleteById(id);
    }
}
