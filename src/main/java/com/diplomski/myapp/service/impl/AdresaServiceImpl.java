package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Adresa;
import com.diplomski.myapp.repository.AdresaRepository;
import com.diplomski.myapp.service.AdresaService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Adresa}.
 */
@Service
@Transactional
public class AdresaServiceImpl implements AdresaService {

    private final Logger log = LoggerFactory.getLogger(AdresaServiceImpl.class);

    private final AdresaRepository adresaRepository;

    public AdresaServiceImpl(AdresaRepository adresaRepository) {
        this.adresaRepository = adresaRepository;
    }

    @Override
    public Adresa save(Adresa adresa) {
        log.debug("Request to save Adresa : {}", adresa);
        return adresaRepository.save(adresa);
    }

    @Override
    public Adresa update(Adresa adresa) {
        log.debug("Request to save Adresa : {}", adresa);
        return adresaRepository.save(adresa);
    }

    @Override
    public Optional<Adresa> partialUpdate(Adresa adresa) {
        log.debug("Request to partially update Adresa : {}", adresa);

        return adresaRepository
            .findById(adresa.getId())
            .map(existingAdresa -> {
                if (adresa.getMesto() != null) {
                    existingAdresa.setMesto(adresa.getMesto());
                }
                if (adresa.getUlica() != null) {
                    existingAdresa.setUlica(adresa.getUlica());
                }
                if (adresa.getBroj() != null) {
                    existingAdresa.setBroj(adresa.getBroj());
                }

                return existingAdresa;
            })
            .map(adresaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Adresa> findAll(Pageable pageable) {
        log.debug("Request to get all Adresas");
        return adresaRepository.findAll(pageable);
    }

    /**
     *  Get all the adresas where Formular is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Adresa> findAllWhereFormularIsNull() {
        log.debug("Request to get all adresas where Formular is null");
        return StreamSupport
            .stream(adresaRepository.findAll().spliterator(), false)
            .filter(adresa -> adresa.getFormular() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Adresa> findOne(Long id) {
        log.debug("Request to get Adresa : {}", id);
        return adresaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Adresa : {}", id);
        adresaRepository.deleteById(id);
    }
}
