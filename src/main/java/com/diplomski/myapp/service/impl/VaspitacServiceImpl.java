package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.domain.enumeration.Status;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.repository.VaspitacRepository;
import com.diplomski.myapp.service.VaspitacService;
import com.diplomski.myapp.web.rest.dto.VaspitacDTO;
import java.util.ArrayList;
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
 * Service Implementation for managing {@link Vaspitac}.
 */
@Service
@Transactional
public class VaspitacServiceImpl implements VaspitacService {

    private final Logger log = LoggerFactory.getLogger(VaspitacServiceImpl.class);

    private final VaspitacRepository vaspitacRepository;
    private final UserRepository userRepository;

    public VaspitacServiceImpl(VaspitacRepository vaspitacRepository, UserRepository userRepository) {
        this.vaspitacRepository = vaspitacRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vaspitac save(Vaspitac vaspitac) {
        log.debug("Request to save Vaspitac : {}", vaspitac);
        vaspitac.setUser(this.userRepository.getById(vaspitac.getUser().getId()));
        vaspitac.setStatus(Status.RADI);
        return vaspitacRepository.save(vaspitac);
    }

    @Override
    public Vaspitac update(Vaspitac vaspitac) {
        log.debug("Request to save Vaspitac : {}", vaspitac);
        return vaspitacRepository.save(vaspitac);
    }

    @Override
    public Optional<Vaspitac> partialUpdate(Vaspitac vaspitac) {
        log.debug("Request to partially update Vaspitac : {}", vaspitac);

        return vaspitacRepository
            .findById(vaspitac.getId())
            .map(existingVaspitac -> {
                if (vaspitac.getDatumZaposlenja() != null) {
                    existingVaspitac.setDatumZaposlenja(vaspitac.getDatumZaposlenja());
                }
                if (vaspitac.getGodineIskustva() != null) {
                    existingVaspitac.setGodineIskustva(vaspitac.getGodineIskustva());
                }
                if (vaspitac.getOpis() != null) {
                    existingVaspitac.setOpis(vaspitac.getOpis());
                }

                return existingVaspitac;
            })
            .map(vaspitacRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vaspitac> findAll(Pageable pageable) {
        log.debug("Request to get all Vaspitacs");
        return vaspitacRepository.findAll(pageable);
    }

    /**
     *  Get all the vaspitacs where ZapazanjeUVeziDeteta is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Vaspitac> findAllWhereZapazanjeUVeziDetetaIsNull() {
        log.debug("Request to get all vaspitacs where ZapazanjeUVeziDeteta is null");
        return StreamSupport
            .stream(vaspitacRepository.findAll().spliterator(), false)
            .filter(vaspitac -> vaspitac.getZapazanjeUVeziDeteta() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vaspitac> findOne(Long id) {
        log.debug("Request to get Vaspitac : {}", id);
        return vaspitacRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vaspitac : {}", id);
        vaspitacRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vaspitac> getByObjekat(Long id) {
        log.debug("Request to get all Vaspitacs");
        return vaspitacRepository.getByObjekat(id);
    }

    @Override
    public List<VaspitacDTO> getImena() {
        List<Vaspitac> listImena = vaspitacRepository.findAllByStatus();
        List<VaspitacDTO> dtos = new ArrayList<>();
        listImena.forEach(f -> {
            dtos.add(new VaspitacDTO(f));
        });
        return dtos;
    }
}
