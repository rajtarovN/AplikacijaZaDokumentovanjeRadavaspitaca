package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Grupa;
import com.diplomski.myapp.repository.GrupaRepository;
import com.diplomski.myapp.service.GrupaService;
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
 * Service Implementation for managing {@link Grupa}.
 */
@Service
@Transactional
public class GrupaServiceImpl implements GrupaService {

    private final Logger log = LoggerFactory.getLogger(GrupaServiceImpl.class);

    private final GrupaRepository grupaRepository;

    public GrupaServiceImpl(GrupaRepository grupaRepository) {
        this.grupaRepository = grupaRepository;
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
}
