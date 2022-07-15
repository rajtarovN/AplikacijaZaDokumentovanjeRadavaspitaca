package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.PotrebanMaterijal;
import com.diplomski.myapp.repository.PotrebanMaterijalRepository;
import com.diplomski.myapp.service.PotrebanMaterijalService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PotrebanMaterijal}.
 */
@Service
@Transactional
public class PotrebanMaterijalServiceImpl implements PotrebanMaterijalService {

    private final Logger log = LoggerFactory.getLogger(PotrebanMaterijalServiceImpl.class);

    private final PotrebanMaterijalRepository potrebanMaterijalRepository;

    public PotrebanMaterijalServiceImpl(PotrebanMaterijalRepository potrebanMaterijalRepository) {
        this.potrebanMaterijalRepository = potrebanMaterijalRepository;
    }

    @Override
    public PotrebanMaterijal save(PotrebanMaterijal potrebanMaterijal) {
        log.debug("Request to save PotrebanMaterijal : {}", potrebanMaterijal);
        return potrebanMaterijalRepository.save(potrebanMaterijal);
    }

    @Override
    public PotrebanMaterijal update(PotrebanMaterijal potrebanMaterijal) {
        log.debug("Request to save PotrebanMaterijal : {}", potrebanMaterijal);
        return potrebanMaterijalRepository.save(potrebanMaterijal);
    }

    @Override
    public Optional<PotrebanMaterijal> partialUpdate(PotrebanMaterijal potrebanMaterijal) {
        log.debug("Request to partially update PotrebanMaterijal : {}", potrebanMaterijal);

        return potrebanMaterijalRepository
            .findById(potrebanMaterijal.getId())
            .map(existingPotrebanMaterijal -> {
                if (potrebanMaterijal.getNaziv() != null) {
                    existingPotrebanMaterijal.setNaziv(potrebanMaterijal.getNaziv());
                }
                if (potrebanMaterijal.getKolicina() != null) {
                    existingPotrebanMaterijal.setKolicina(potrebanMaterijal.getKolicina());
                }
                if (potrebanMaterijal.getStatusMaterijala() != null) {
                    existingPotrebanMaterijal.setStatusMaterijala(potrebanMaterijal.getStatusMaterijala());
                }

                return existingPotrebanMaterijal;
            })
            .map(potrebanMaterijalRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PotrebanMaterijal> findAll(Pageable pageable) {
        log.debug("Request to get all PotrebanMaterijals");
        return potrebanMaterijalRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PotrebanMaterijal> findOne(Long id) {
        log.debug("Request to get PotrebanMaterijal : {}", id);
        return potrebanMaterijalRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PotrebanMaterijal : {}", id);
        potrebanMaterijalRepository.deleteById(id);
    }
}
