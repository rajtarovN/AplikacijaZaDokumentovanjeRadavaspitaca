package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Direktor;
import com.diplomski.myapp.repository.DirektorRepository;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.service.DirektorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Direktor}.
 */
@Service
@Transactional
public class DirektorServiceImpl implements DirektorService {

    private final Logger log = LoggerFactory.getLogger(DirektorServiceImpl.class);

    private final DirektorRepository direktorRepository;
    private final UserRepository userRepository;

    public DirektorServiceImpl(DirektorRepository direktorRepository, UserRepository userRepository) {
        this.direktorRepository = direktorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Direktor save(Direktor direktor) {
        log.debug("Request to save Direktor : {}", direktor);
        direktor.setUser(this.userRepository.getById(direktor.getUser().getId()));
        return direktorRepository.save(direktor);
    }

    @Override
    public Direktor update(Direktor direktor) {
        log.debug("Request to save Direktor : {}", direktor);
        // no save call needed as we have no fields that can be updated
        return direktor;
    }

    @Override
    public Optional<Direktor> partialUpdate(Direktor direktor) {
        log.debug("Request to partially update Direktor : {}", direktor);

        return direktorRepository
            .findById(direktor.getId())
            .map(existingDirektor -> {
                return existingDirektor;
            }); // .map(direktorRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Direktor> findAll(Pageable pageable) {
        log.debug("Request to get all Direktors");
        return direktorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Direktor> findOne(Long id) {
        log.debug("Request to get Direktor : {}", id);
        return direktorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Direktor : {}", id);
        direktorRepository.deleteById(id);
    }
}
