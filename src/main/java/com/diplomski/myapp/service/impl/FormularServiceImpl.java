package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.domain.PodaciORoditeljima;
import com.diplomski.myapp.domain.Roditelj;
import com.diplomski.myapp.domain.User;
import com.diplomski.myapp.domain.enumeration.StatusFormulara;
import com.diplomski.myapp.repository.AdresaRepository;
import com.diplomski.myapp.repository.FormularRepository;
import com.diplomski.myapp.repository.PodaciORoditeljimaRepository;
import com.diplomski.myapp.repository.RoditeljRepository;
import com.diplomski.myapp.service.FormularService;
import com.diplomski.myapp.web.rest.dto.DeteZaGrupuDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private final RoditeljRepository roditeljRepository;

    private final PodaciORoditeljimaRepository podaciORoditeljimaRepository;

    private final AdresaRepository adresaRepository;

    public FormularServiceImpl(
        FormularRepository formularRepository,
        RoditeljRepository roditeljRepository,
        PodaciORoditeljimaRepository podaciORoditeljimaRepository,
        AdresaRepository adresaRepository
    ) {
        this.formularRepository = formularRepository;
        this.roditeljRepository = roditeljRepository;
        this.podaciORoditeljimaRepository = podaciORoditeljimaRepository;
        this.adresaRepository = adresaRepository;
    }

    @Override
    public Formular save(Formular formular, String username) {
        log.debug("Request to save Formular : {}", formular);
        Roditelj roditelj = this.roditeljRepository.findByUsername(username);
        this.adresaRepository.save(formular.getAdresa());

        Formular ret = formularRepository.save(formular);
        if (ret != null) {
            ret.setRoditelj(roditelj);
            formularRepository.save(ret);
        }
        roditelj.getFormulars().add(ret);
        this.roditeljRepository.save(roditelj);
        for (PodaciORoditeljima p : formular.getPodaciORoditeljimas()) {
            p.setFormular(ret);
            this.podaciORoditeljimaRepository.save(p);
        }
        return ret;
    }

    @Override
    public Formular update(Formular formular) {
        log.debug("Request to save Formular : {}", formular);
        this.adresaRepository.save(formular.getAdresa());
        formular.setPodaciORoditeljimas(this.podaciORoditeljimaRepository.findByFormular(formular.getId()));
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
        Set<PodaciORoditeljima> podaci = this.podaciORoditeljimaRepository.findByFormular(id);
        User u = this.roditeljRepository.findByUser(formularRepository.findById(id).get().getRoditelj().getId());
        Optional<Formular> formular = formularRepository.findById(id);
        formular.get().getRoditelj().setUser(u);
        formular.get().setPodaciORoditeljimas(podaci);
        return formular;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formular : {}", id);
        formularRepository.deleteById(id);
    }

    @Override
    public List<DeteZaGrupuDTO> findAllDecaZaGrupu() {
        List<Formular> listFormulara = formularRepository.findAllOdobren();
        List<DeteZaGrupuDTO> dtos = new ArrayList<>();
        listFormulara.forEach(f -> {
            dtos.add(new DeteZaGrupuDTO(f));
        });
        return dtos;
    }

    @Override
    public Page<Formular> findAllByRoditelj(Pageable pageable, String username) {
        List<Formular> formular = this.formularRepository.findAllByRoditelj(username);
        //.subList((pageable.getPageNumber()) * pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());

        Page<Formular> page = new PageImpl<>(formular);
        return page;
    }

    @Override
    public Formular approve(Long id) {
        Optional<Formular> formular = this.formularRepository.findById(id);
        formular.get().setStatusFormulara(StatusFormulara.ODOBREN);
        return this.formularRepository.save(formular.get());
    }

    @Override
    public Formular reject(Long id) {
        Optional<Formular> formular = this.formularRepository.findById(id);
        formular.get().setStatusFormulara(StatusFormulara.ODBIJEN);
        return this.formularRepository.save(formular.get());
    }
}
