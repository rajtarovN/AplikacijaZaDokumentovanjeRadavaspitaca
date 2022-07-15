package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Prica;
import com.diplomski.myapp.repository.PricaRepository;
import com.diplomski.myapp.service.PricaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prica}.
 */
@Service
@Transactional
public class PricaServiceImpl implements PricaService {

    private final Logger log = LoggerFactory.getLogger(PricaServiceImpl.class);

    private final PricaRepository pricaRepository;

    public PricaServiceImpl(PricaRepository pricaRepository) {
        this.pricaRepository = pricaRepository;
    }

    @Override
    public Prica save(Prica prica) {
        log.debug("Request to save Prica : {}", prica);
        return pricaRepository.save(prica);
    }

    @Override
    public Prica update(Prica prica) {
        log.debug("Request to save Prica : {}", prica);
        return pricaRepository.save(prica);
    }

    @Override
    public Optional<Prica> partialUpdate(Prica prica) {
        log.debug("Request to partially update Prica : {}", prica);

        return pricaRepository
            .findById(prica.getId())
            .map(existingPrica -> {
                return existingPrica;
            })
            .map(pricaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Prica> findAll(Pageable pageable) {
        log.debug("Request to get all Pricas");
        return pricaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prica> findOne(Long id) {
        log.debug("Request to get Prica : {}", id);
        return pricaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prica : {}", id);
        pricaRepository.deleteById(id);
    }
}
