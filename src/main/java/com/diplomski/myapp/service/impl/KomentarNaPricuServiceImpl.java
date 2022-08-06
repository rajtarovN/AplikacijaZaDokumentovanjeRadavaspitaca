package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.KomentarNaPricu;
import com.diplomski.myapp.repository.KomentarNaPricuRepository;
import com.diplomski.myapp.service.KomentarNaPricuService;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link KomentarNaPricu}.
 */
@Service
@Transactional
public class KomentarNaPricuServiceImpl implements KomentarNaPricuService {

    private final Logger log = LoggerFactory.getLogger(KomentarNaPricuServiceImpl.class);

    private final KomentarNaPricuRepository komentarNaPricuRepository;

    public KomentarNaPricuServiceImpl(KomentarNaPricuRepository komentarNaPricuRepository) {
        this.komentarNaPricuRepository = komentarNaPricuRepository;
    }

    @Override
    public KomentarNaPricu save(KomentarNaPricu komentarNaPricu) {
        log.debug("Request to save KomentarNaPricu : {}", komentarNaPricu);
        if (komentarNaPricu.getDatum() == null) {
            komentarNaPricu.setDatum(LocalDate.now());
        }
        return komentarNaPricuRepository.save(komentarNaPricu);
    }

    @Override
    public KomentarNaPricu update(KomentarNaPricu komentarNaPricu) {
        log.debug("Request to save KomentarNaPricu : {}", komentarNaPricu);
        return komentarNaPricuRepository.save(komentarNaPricu);
    }

    @Override
    public Optional<KomentarNaPricu> partialUpdate(KomentarNaPricu komentarNaPricu) {
        log.debug("Request to partially update KomentarNaPricu : {}", komentarNaPricu);

        return komentarNaPricuRepository
            .findById(komentarNaPricu.getId())
            .map(existingKomentarNaPricu -> {
                if (komentarNaPricu.getTekst() != null) {
                    existingKomentarNaPricu.setTekst(komentarNaPricu.getTekst());
                }
                if (komentarNaPricu.getDatum() != null) {
                    existingKomentarNaPricu.setDatum(komentarNaPricu.getDatum());
                }

                return existingKomentarNaPricu;
            })
            .map(komentarNaPricuRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KomentarNaPricu> findAll(Pageable pageable) {
        log.debug("Request to get all KomentarNaPricus");
        return komentarNaPricuRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KomentarNaPricu> findOne(Long id) {
        log.debug("Request to get KomentarNaPricu : {}", id);
        return komentarNaPricuRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete KomentarNaPricu : {}", id);
        komentarNaPricuRepository.deleteById(id);
    }
}
