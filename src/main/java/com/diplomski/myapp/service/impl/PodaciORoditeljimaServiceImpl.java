package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.PodaciORoditeljima;
import com.diplomski.myapp.repository.PodaciORoditeljimaRepository;
import com.diplomski.myapp.service.PodaciORoditeljimaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PodaciORoditeljima}.
 */
@Service
@Transactional
public class PodaciORoditeljimaServiceImpl implements PodaciORoditeljimaService {

    private final Logger log = LoggerFactory.getLogger(PodaciORoditeljimaServiceImpl.class);

    private final PodaciORoditeljimaRepository podaciORoditeljimaRepository;

    public PodaciORoditeljimaServiceImpl(PodaciORoditeljimaRepository podaciORoditeljimaRepository) {
        this.podaciORoditeljimaRepository = podaciORoditeljimaRepository;
    }

    @Override
    public PodaciORoditeljima save(PodaciORoditeljima podaciORoditeljima) {
        log.debug("Request to save PodaciORoditeljima : {}", podaciORoditeljima);
        return podaciORoditeljimaRepository.save(podaciORoditeljima);
    }

    @Override
    public PodaciORoditeljima update(PodaciORoditeljima podaciORoditeljima) {
        log.debug("Request to save PodaciORoditeljima : {}", podaciORoditeljima);
        return podaciORoditeljimaRepository.save(podaciORoditeljima);
    }

    @Override
    public Optional<PodaciORoditeljima> partialUpdate(PodaciORoditeljima podaciORoditeljima) {
        log.debug("Request to partially update PodaciORoditeljima : {}", podaciORoditeljima);

        return podaciORoditeljimaRepository
            .findById(podaciORoditeljima.getId())
            .map(existingPodaciORoditeljima -> {
                if (podaciORoditeljima.getJmbg() != null) {
                    existingPodaciORoditeljima.setJmbg(podaciORoditeljima.getJmbg());
                }
                if (podaciORoditeljima.getIme() != null) {
                    existingPodaciORoditeljima.setIme(podaciORoditeljima.getIme());
                }
                if (podaciORoditeljima.getPrezime() != null) {
                    existingPodaciORoditeljima.setPrezime(podaciORoditeljima.getPrezime());
                }
                if (podaciORoditeljima.getTelefon() != null) {
                    existingPodaciORoditeljima.setTelefon(podaciORoditeljima.getTelefon());
                }
                if (podaciORoditeljima.getFirma() != null) {
                    existingPodaciORoditeljima.setFirma(podaciORoditeljima.getFirma());
                }
                if (podaciORoditeljima.getRadnoVreme() != null) {
                    existingPodaciORoditeljima.setRadnoVreme(podaciORoditeljima.getRadnoVreme());
                }
                if (podaciORoditeljima.getRadniStatus() != null) {
                    existingPodaciORoditeljima.setRadniStatus(podaciORoditeljima.getRadniStatus());
                }

                return existingPodaciORoditeljima;
            })
            .map(podaciORoditeljimaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PodaciORoditeljima> findAll(Pageable pageable) {
        log.debug("Request to get all PodaciORoditeljimas");
        return podaciORoditeljimaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PodaciORoditeljima> findOne(Long id) {
        log.debug("Request to get PodaciORoditeljima : {}", id);
        return podaciORoditeljimaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PodaciORoditeljima : {}", id);
        podaciORoditeljimaRepository.deleteById(id);
    }
}
