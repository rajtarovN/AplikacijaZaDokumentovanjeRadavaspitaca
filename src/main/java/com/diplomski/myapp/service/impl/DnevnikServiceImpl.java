package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.repository.DnevnikRepository;
import com.diplomski.myapp.service.DnevnikService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    public DnevnikServiceImpl(DnevnikRepository dnevnikRepository) {
        this.dnevnikRepository = dnevnikRepository;
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
}
