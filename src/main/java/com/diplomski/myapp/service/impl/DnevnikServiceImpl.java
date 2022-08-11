package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.domain.Grupa;
import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.repository.DeteRepository;
import com.diplomski.myapp.repository.DnevnikRepository;
import com.diplomski.myapp.repository.VaspitacRepository;
import com.diplomski.myapp.service.DnevnikService;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dnevnik}.
 */
@Service
@Transactional
public class DnevnikServiceImpl implements DnevnikService {

    private final Logger log = LoggerFactory.getLogger(DnevnikServiceImpl.class);

    private final DnevnikRepository dnevnikRepository;

    private final VaspitacRepository vaspitacRepository;

    private final DeteRepository deteRepository;

    public DnevnikServiceImpl(DnevnikRepository dnevnikRepository, VaspitacRepository vaspitacRepository, DeteRepository deteRepository) {
        this.dnevnikRepository = dnevnikRepository;
        this.vaspitacRepository = vaspitacRepository;
        this.deteRepository = deteRepository;
    }

    @Override
    public Dnevnik save(Dnevnik dnevnik) {
        log.debug("Request to save Dnevnik : {}", dnevnik);
        return dnevnikRepository.save(dnevnik);
    }

    @Override
    public Dnevnik update(Dnevnik dnevnik) {
        log.debug("Request to save Dnevnik : {}", dnevnik);
        return dnevnikRepository.save(dnevnik);
    }

    @Override
    public Optional<Dnevnik> partialUpdate(Dnevnik dnevnik) {
        log.debug("Request to partially update Dnevnik : {}", dnevnik);

        return dnevnikRepository
            .findById(dnevnik.getId())
            .map(existingDnevnik -> {
                if (dnevnik.getPocetakVazenja() != null) {
                    existingDnevnik.setPocetakVazenja(dnevnik.getPocetakVazenja());
                }
                if (dnevnik.getKrajVazenja() != null) {
                    existingDnevnik.setKrajVazenja(dnevnik.getKrajVazenja());
                }

                return existingDnevnik;
            })
            .map(dnevnikRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dnevnik> findAll(Pageable pageable) {
        log.debug("Request to get all Dnevniks");
        return dnevnikRepository.findAll(pageable);
    }

    public Page<Dnevnik> findAllWithEagerRelationships(Pageable pageable) {
        return dnevnikRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dnevnik> findOne(Long id) {
        log.debug("Request to get Dnevnik : {}", id);
        return dnevnikRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dnevnik : {}", id);
        dnevnikRepository.deleteById(id);
    }

    @Override
    public Set<Dete> findAllChildren(Long id) {
        return dnevnikRepository.findById(id).get().getGrupa().getDetes();
    }

    @Override
    public List<Dete> getDecaByUsername(String username) {
        Vaspitac vaspitac = this.vaspitacRepository.getVaspitacIdByUsername(username);
        for (Dnevnik d : vaspitac.getDnevniks()) {
            if (d.getKrajVazenja().isAfter(LocalDate.now())) {
                Grupa grupa = dnevnikRepository.getGrupa(d.getId());
                if (grupa != null) {
                    return this.deteRepository.findAllByGrupa(grupa.getId());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Page<Dnevnik> findAllByUsername(String username) {
        Vaspitac vaspitac = this.vaspitacRepository.getVaspitacIdByUsername(username);
        List<Dnevnik> dnevniks = new ArrayList<>();
        for (Dnevnik d : vaspitac.getDnevniks()) {
            if (d.getKrajVazenja().isBefore(LocalDate.now())) {
                dnevniks.add(d);
            }
        }
        return new PageImpl<>(dnevniks);
    }
}
