package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Korisnik;
import com.diplomski.myapp.repository.KorisnikRepository;
import com.diplomski.myapp.service.KorisnikService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Korisnik}.
 */
@Service
@Transactional
public class KorisnikServiceImpl implements KorisnikService {

    private final Logger log = LoggerFactory.getLogger(KorisnikServiceImpl.class);

    private final KorisnikRepository korisnikRepository;

    public KorisnikServiceImpl(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    @Override
    public Korisnik save(Korisnik korisnik) {
        log.debug("Request to save Korisnik : {}", korisnik);
        return korisnikRepository.save(korisnik);
    }

    @Override
    public Korisnik update(Korisnik korisnik) {
        log.debug("Request to save Korisnik : {}", korisnik);
        return korisnikRepository.save(korisnik);
    }

    @Override
    public Optional<Korisnik> partialUpdate(Korisnik korisnik) {
        log.debug("Request to partially update Korisnik : {}", korisnik);

        return korisnikRepository
            .findById(korisnik.getId())
            .map(existingKorisnik -> {
                if (korisnik.getKorisnickoIme() != null) {
                    existingKorisnik.setKorisnickoIme(korisnik.getKorisnickoIme());
                }
                if (korisnik.getSifra() != null) {
                    existingKorisnik.setSifra(korisnik.getSifra());
                }
                if (korisnik.getIme() != null) {
                    existingKorisnik.setIme(korisnik.getIme());
                }
                if (korisnik.getPrezime() != null) {
                    existingKorisnik.setPrezime(korisnik.getPrezime());
                }

                return existingKorisnik;
            })
            .map(korisnikRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Korisnik> findAll(Pageable pageable) {
        log.debug("Request to get all Korisniks");
        return korisnikRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Korisnik> findOne(Long id) {
        log.debug("Request to get Korisnik : {}", id);
        return korisnikRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Korisnik : {}", id);
        korisnikRepository.deleteById(id);
    }
}
