package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.repository.DeteRepository;
import com.diplomski.myapp.repository.VaspitacRepository;
import com.diplomski.myapp.service.DeteService;
import com.diplomski.myapp.web.rest.dto.ProfilDetetaDTO;
import java.time.LocalDate;
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
 * Service Implementation for managing {@link Dete}.
 */
@Service
@Transactional
public class DeteServiceImpl implements DeteService {

    private final Logger log = LoggerFactory.getLogger(DeteServiceImpl.class);

    private final DeteRepository deteRepository;

    private final VaspitacRepository vaspitacRepository;

    public DeteServiceImpl(DeteRepository deteRepository, VaspitacRepository vaspitacRepository) {
        this.deteRepository = deteRepository;
        this.vaspitacRepository = vaspitacRepository;
    }

    @Override
    public Dete save(Dete dete) {
        log.debug("Request to save Dete : {}", dete);
        return deteRepository.save(dete);
    }

    @Override
    public Dete update(Dete dete) {
        log.debug("Request to save Dete : {}", dete);
        return deteRepository.save(dete);
    }

    @Override
    public Optional<Dete> partialUpdate(Dete dete) {
        log.debug("Request to partially update Dete : {}", dete);

        return deteRepository
            .findById(dete.getId())
            .map(existingDete -> {
                if (dete.getVisina() != null) {
                    existingDete.setVisina(dete.getVisina());
                }
                if (dete.getTezina() != null) {
                    existingDete.setTezina(dete.getTezina());
                }

                return existingDete;
            })
            .map(deteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dete> findAll(Pageable pageable) {
        log.debug("Request to get all Detes");
        return deteRepository.findAll(pageable);
    }

    /**
     *  Get all the detes where NeDolasci is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Dete> findAllWhereNeDolasciIsNull() {
        log.debug("Request to get all detes where NeDolasci is null");
        return StreamSupport
            .stream(deteRepository.findAll().spliterator(), false)
            .filter(dete -> dete.getNeDolasci() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dete> findOne(Long id) {
        log.debug("Request to get Dete : {}", id);
        return deteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dete : {}", id);
        deteRepository.deleteById(id);
    }

    @Override
    public ProfilDetetaDTO findProfil(Long id) {
        Optional<Dete> dete = deteRepository.findById(id);
        if (dete.isPresent()) {
            ProfilDetetaDTO profilDTO = new ProfilDetetaDTO(dete.get());
            //int izostanci = dete.get().getNeDolasci().stream().filter(nd -> nd.)
            profilDTO.setBrojIzostanaka(0);
            //profilDTO.setVaspitac(dete.get().getGrupa().getDnevnik().getVaspitacs()[0]);
            profilDTO.setVaspitac("Mira Lazic");
            return profilDTO;
        }

        return null;
    }

    @Override
    public Page<Dete> findAllByGrupa(Pageable pageable, Long id) {
        List<Dete> deca = this.deteRepository.findAllByGrupa(id); //.subList(
        //                (pageable.getPageNumber())*pageable.getPageSize(),
        //            (pageable.getPageNumber()+1)*pageable.getPageSize());

        Page<Dete> page = new PageImpl<>(deca);
        return page;
    }

    @Override
    public Page<Dete> findAllByRoditelj(Pageable pageable, String username) {
        List<Dete> deca =
            this.deteRepository.findAllByRoditelj(username)
                .subList((pageable.getPageNumber()) * pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());

        Page<Dete> page = new PageImpl<>(deca);
        return page;
    }

    @Override
    public Page<Dete> findAllForVaspitac(Pageable pageable, String username) {
        Set<Dnevnik> dnevniks = this.vaspitacRepository.getVaspitacIdByUsername(username).getDnevniks();
        Dnevnik dnevnik = null;

        for (Dnevnik d : dnevniks) {
            if (d.getPocetakVazenja().isBefore(LocalDate.now()) && d.getKrajVazenja().isAfter(LocalDate.now())) {
                dnevnik = d;
                List<Dete> deca =
                    this.deteRepository.findAllByGrupa(dnevnik.getGrupa().getId())
                        .subList((pageable.getPageNumber()) * pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());

                Page<Dete> page = new PageImpl<>(deca);
                return page;
            }
        }
        Page<Dete> page = new PageImpl<>(new ArrayList<>());
        return page;
    }
}
