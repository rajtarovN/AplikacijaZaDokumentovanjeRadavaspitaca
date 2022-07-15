package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Objekat;
import com.diplomski.myapp.repository.ObjekatRepository;
import com.diplomski.myapp.service.ObjekatService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Objekat}.
 */
@Service
@Transactional
public class ObjekatServiceImpl implements ObjekatService {

    private final Logger log = LoggerFactory.getLogger(ObjekatServiceImpl.class);

    private final ObjekatRepository objekatRepository;

    public ObjekatServiceImpl(ObjekatRepository objekatRepository) {
        this.objekatRepository = objekatRepository;
    }

    @Override
    public Objekat save(Objekat objekat) {
        log.debug("Request to save Objekat : {}", objekat);
        return objekatRepository.save(objekat);
    }

    @Override
    public Objekat update(Objekat objekat) {
        log.debug("Request to save Objekat : {}", objekat);
        return objekatRepository.save(objekat);
    }

    @Override
    public Optional<Objekat> partialUpdate(Objekat objekat) {
        log.debug("Request to partially update Objekat : {}", objekat);

        return objekatRepository
            .findById(objekat.getId())
            .map(existingObjekat -> {
                if (objekat.getOpis() != null) {
                    existingObjekat.setOpis(objekat.getOpis());
                }

                return existingObjekat;
            })
            .map(objekatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Objekat> findAll(Pageable pageable) {
        log.debug("Request to get all Objekats");
        return objekatRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Objekat> findOne(Long id) {
        log.debug("Request to get Objekat : {}", id);
        return objekatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Objekat : {}", id);
        objekatRepository.deleteById(id);
    }
}
