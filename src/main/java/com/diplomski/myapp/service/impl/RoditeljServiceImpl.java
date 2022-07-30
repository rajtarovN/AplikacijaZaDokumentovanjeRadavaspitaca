package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Roditelj;
import com.diplomski.myapp.repository.RoditeljRepository;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.service.RoditeljService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Roditelj}.
 */
@Service
@Transactional
public class RoditeljServiceImpl implements RoditeljService {

    private final Logger log = LoggerFactory.getLogger(RoditeljServiceImpl.class);

    private final RoditeljRepository roditeljRepository;
    private final UserRepository userRepository;

    public RoditeljServiceImpl(RoditeljRepository roditeljRepository, UserRepository userRepository) {
        this.roditeljRepository = roditeljRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Roditelj save(Roditelj roditelj) {
        log.debug("Request to save Roditelj : {}", roditelj);
        roditelj.setUser(this.userRepository.getById(roditelj.getUser().getId()));
        return roditeljRepository.save(roditelj);
    }

    @Override
    public Roditelj update(Roditelj roditelj) {
        log.debug("Request to save Roditelj : {}", roditelj);
        // no save call needed as we have no fields that can be updated
        return roditelj;
    }

    @Override
    public Optional<Roditelj> partialUpdate(Roditelj roditelj) {
        log.debug("Request to partially update Roditelj : {}", roditelj);

        return roditeljRepository
            .findById(roditelj.getId())
            .map(existingRoditelj -> {
                return existingRoditelj;
            }); // .map(roditeljRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Roditelj> findAll(Pageable pageable) {
        log.debug("Request to get all Roditeljs");
        return roditeljRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Roditelj> findOne(Long id) {
        log.debug("Request to get Roditelj : {}", id);
        return roditeljRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Roditelj : {}", id);
        roditeljRepository.deleteById(id);
    }
}
