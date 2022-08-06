package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.KonacnaPrica;
import com.diplomski.myapp.domain.Prica;
import com.diplomski.myapp.repository.KonacnaPricaRepository;
import com.diplomski.myapp.repository.PricaRepository;
import com.diplomski.myapp.service.KonacnaPricaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link KonacnaPrica}.
 */
@Service
@Transactional
public class KonacnaPricaServiceImpl implements KonacnaPricaService {

    private final Logger log = LoggerFactory.getLogger(KonacnaPricaServiceImpl.class);

    private final KonacnaPricaRepository konacnaPricaRepository;
    private final PricaRepository pricaRepository;

    public KonacnaPricaServiceImpl(KonacnaPricaRepository konacnaPricaRepository, PricaRepository pricaRepository) {
        this.konacnaPricaRepository = konacnaPricaRepository;
        this.pricaRepository = pricaRepository;
    }

    @Override
    public KonacnaPrica save(KonacnaPrica konacnaPrica, Long idprice) {
        log.debug("Request to save KonacnaPrica : {}", konacnaPrica);
        Prica prica = this.pricaRepository.getById(idprice);
        prica.setKonacnaPrica(konacnaPrica);
        KonacnaPrica retValue = konacnaPricaRepository.save(konacnaPrica);
        this.pricaRepository.save(prica);
        return retValue;
    }

    @Override
    public KonacnaPrica update(KonacnaPrica konacnaPrica) {
        log.debug("Request to save KonacnaPrica : {}", konacnaPrica);
        return konacnaPricaRepository.save(konacnaPrica);
    }

    @Override
    public Optional<KonacnaPrica> partialUpdate(KonacnaPrica konacnaPrica) {
        log.debug("Request to partially update KonacnaPrica : {}", konacnaPrica);

        return konacnaPricaRepository
            .findById(konacnaPrica.getId())
            .map(existingKonacnaPrica -> {
                if (konacnaPrica.getTekst() != null) {
                    existingKonacnaPrica.setTekst(konacnaPrica.getTekst());
                }

                return existingKonacnaPrica;
            })
            .map(konacnaPricaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KonacnaPrica> findAll(Pageable pageable) {
        log.debug("Request to get all KonacnaPricas");
        return konacnaPricaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KonacnaPrica> findOne(Long id) {
        log.debug("Request to get KonacnaPrica : {}", id);
        return konacnaPricaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete KonacnaPrica : {}", id);
        konacnaPricaRepository.deleteById(id);
    }
}
