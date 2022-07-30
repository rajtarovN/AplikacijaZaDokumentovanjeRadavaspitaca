package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Pedagog;
import com.diplomski.myapp.repository.PedagogRepository;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.service.PedagogService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pedagog}.
 */
@Service
@Transactional
public class PedagogServiceImpl implements PedagogService {

    private final Logger log = LoggerFactory.getLogger(PedagogServiceImpl.class);

    private final PedagogRepository pedagogRepository;

    private final UserRepository userRepository;

    public PedagogServiceImpl(PedagogRepository pedagogRepository, UserRepository userRepository) {
        this.pedagogRepository = pedagogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Pedagog save(Pedagog pedagog) {
        log.debug("Request to save Pedagog : {}", pedagog);
        pedagog.setUser(this.userRepository.getById(pedagog.getUser().getId()));
        return pedagogRepository.save(pedagog);
    }

    @Override
    public Pedagog update(Pedagog pedagog) {
        log.debug("Request to save Pedagog : {}", pedagog);
        // no save call needed as we have no fields that can be updated
        return pedagog;
    }

    @Override
    public Optional<Pedagog> partialUpdate(Pedagog pedagog) {
        log.debug("Request to partially update Pedagog : {}", pedagog);

        return pedagogRepository
            .findById(pedagog.getId())
            .map(existingPedagog -> {
                return existingPedagog;
            }); // .map(pedagogRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pedagog> findAll(Pageable pageable) {
        log.debug("Request to get all Pedagogs");
        return pedagogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedagog> findOne(Long id) {
        log.debug("Request to get Pedagog : {}", id);
        return pedagogRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pedagog : {}", id);
        pedagogRepository.deleteById(id);
    }
}
