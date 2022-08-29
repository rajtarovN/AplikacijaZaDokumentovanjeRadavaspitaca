package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.*;
import com.diplomski.myapp.domain.enumeration.StatusFormulara;
import com.diplomski.myapp.repository.*;
import com.diplomski.myapp.service.EmailAlreadyUsedException;
import com.diplomski.myapp.service.GrupaService;
import com.diplomski.myapp.service.VaspitacAlreadyHaveDnevnikInGivenPeriodException;
import com.diplomski.myapp.web.rest.dto.DeteZaGrupuDTO;
import com.diplomski.myapp.web.rest.dto.GrupaDTO;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Grupa}.
 */
@Service
@Transactional
public class GrupaServiceImpl implements GrupaService {

    private final Logger log = LoggerFactory.getLogger(GrupaServiceImpl.class);

    private final GrupaRepository grupaRepository;
    private final DeteRepository deteRepository;
    private final VaspitacRepository vaspitacRepository;
    private final DnevnikRepository dnevnikRepository;
    private final FormularRepository formularRepository;

    public GrupaServiceImpl(
        GrupaRepository grupaRepository,
        DeteRepository deteRepository,
        VaspitacRepository vaspitacRepository,
        DnevnikRepository dnevnikRepository,
        FormularRepository formularRepository
    ) {
        this.grupaRepository = grupaRepository;
        this.deteRepository = deteRepository;
        this.vaspitacRepository = vaspitacRepository;
        this.dnevnikRepository = dnevnikRepository;
        this.formularRepository = formularRepository;
    }

    @Override
    public Grupa save(Grupa grupa) {
        log.debug("Request to save Grupa : {}", grupa);
        return grupaRepository.save(grupa);
    }

    @Override
    public Grupa update(Grupa grupa) {
        log.debug("Request to save Grupa : {}", grupa);
        return grupaRepository.save(grupa);
    }

    @Override
    public Optional<Grupa> partialUpdate(Grupa grupa) {
        log.debug("Request to partially update Grupa : {}", grupa);

        return grupaRepository
            .findById(grupa.getId())
            .map(existingGrupa -> {
                if (grupa.getTipGrupe() != null) {
                    existingGrupa.setTipGrupe(grupa.getTipGrupe());
                }

                return existingGrupa;
            })
            .map(grupaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Grupa> findAll(Pageable pageable) {
        log.debug("Request to get all Grupas");
        return grupaRepository.findAll(pageable);
    }

    /**
     *  Get all the grupas where Dnevnik is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Grupa> findAllWhereDnevnikIsNull() {
        log.debug("Request to get all grupas where Dnevnik is null");
        return StreamSupport
            .stream(grupaRepository.findAll().spliterator(), false)
            .filter(grupa -> grupa.getDnevnik() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Grupa> findOne(Long id) {
        log.debug("Request to get Grupa : {}", id);
        return grupaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Grupa : {}", id);
        grupaRepository.deleteById(id);
    }

    @Override
    public Grupa saveGrupa(GrupaDTO grupa) {
        Vaspitac vaspitac = this.vaspitacRepository.findById(grupa.getVaspitac().getId()).get();
        for (Dnevnik d : vaspitac.getDnevniks()) {
            if (d.getKrajVazenja().isAfter(grupa.getPocetakVazenja())) {
                throw new VaspitacAlreadyHaveDnevnikInGivenPeriodException();
            }
        }

        Grupa newGrupa = new Grupa();
        newGrupa.setTipGrupe(grupa.tipGrupe);
        Set<Dete> deca = new HashSet<>();
        for (DeteZaGrupuDTO d : grupa.getDeca()) {
            Formular f = this.formularRepository.getFormularById(d.getId());
            f.setStatusFormulara(StatusFormulara.RASPOREDJEN);
            this.formularRepository.save(f);
            Dete dete = new Dete(); //this.deteRepository.findById(d.getId()).get();
            deca.add(dete);
            this.deteRepository.save(dete);
            dete.setGrupa(newGrupa);
        }
        newGrupa.setDetes(deca);

        Dnevnik newDnevnik = new Dnevnik();
        newDnevnik.setGrupa(newGrupa);
        newDnevnik.setKrajVazenja(grupa.getKrajVazenja());
        newDnevnik.setPocetakVazenja(grupa.getPocetakVazenja());
        Set<Vaspitac> vaspitaci = new HashSet<>();
        vaspitaci.add(vaspitac);
        newDnevnik.setVaspitacs(vaspitaci);
        newGrupa.setDnevnik(newDnevnik);
        if (vaspitac.getDnevniks() == null) {
            vaspitac.setDnevniks(new HashSet<>());
        }
        this.grupaRepository.save(newGrupa);
        vaspitac.getDnevniks().add(newDnevnik);
        this.vaspitacRepository.save(vaspitac);
        this.dnevnikRepository.save(newDnevnik);
        return newGrupa;
    }
}
