package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.ZapazanjeUVeziDeteta;
import com.diplomski.myapp.repository.DeteRepository;
import com.diplomski.myapp.repository.UserRepository;
import com.diplomski.myapp.repository.VaspitacRepository;
import com.diplomski.myapp.repository.ZapazanjeUVeziDetetaRepository;
import com.diplomski.myapp.service.ZapazanjeUVeziDetetaService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ZapazanjeUVeziDeteta}.
 */
@Service
@Transactional
public class ZapazanjeUVeziDetetaServiceImpl implements ZapazanjeUVeziDetetaService {

    private final Logger log = LoggerFactory.getLogger(ZapazanjeUVeziDetetaServiceImpl.class);

    private final ZapazanjeUVeziDetetaRepository zapazanjeUVeziDetetaRepository;

    private final DeteRepository deteRepository;

    private final VaspitacRepository vaspitacRepository;

    private final UserRepository userRepository;

    public ZapazanjeUVeziDetetaServiceImpl(
        ZapazanjeUVeziDetetaRepository zapazanjeUVeziDetetaRepository,
        DeteRepository deteRepository,
        VaspitacRepository vaspitacRepository,
        UserRepository userRepository
    ) {
        this.zapazanjeUVeziDetetaRepository = zapazanjeUVeziDetetaRepository;
        this.deteRepository = deteRepository;
        this.vaspitacRepository = vaspitacRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ZapazanjeUVeziDeteta save(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        log.debug("Request to save ZapazanjeUVeziDeteta : {}", zapazanjeUVeziDeteta);
        zapazanjeUVeziDeteta.setDatum(LocalDate.now());
        zapazanjeUVeziDeteta.setDete(this.deteRepository.getById(zapazanjeUVeziDeteta.getDete().getId()));
        //if()
        zapazanjeUVeziDeteta.setUser(this.userRepository.findOneByLogin(zapazanjeUVeziDeteta.getUser().getLogin()).get());
        return zapazanjeUVeziDetetaRepository.save(zapazanjeUVeziDeteta);
    }

    @Override
    public ZapazanjeUVeziDeteta update(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        log.debug("Request to save ZapazanjeUVeziDeteta : {}", zapazanjeUVeziDeteta);
        return zapazanjeUVeziDetetaRepository.save(zapazanjeUVeziDeteta);
    }

    @Override
    public Optional<ZapazanjeUVeziDeteta> partialUpdate(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        log.debug("Request to partially update ZapazanjeUVeziDeteta : {}", zapazanjeUVeziDeteta);

        return zapazanjeUVeziDetetaRepository
            .findById(zapazanjeUVeziDeteta.getId())
            .map(existingZapazanjeUVeziDeteta -> {
                if (zapazanjeUVeziDeteta.getInteresovanja() != null) {
                    existingZapazanjeUVeziDeteta.setInteresovanja(zapazanjeUVeziDeteta.getInteresovanja());
                }
                if (zapazanjeUVeziDeteta.getPrednosti() != null) {
                    existingZapazanjeUVeziDeteta.setPrednosti(zapazanjeUVeziDeteta.getPrednosti());
                }
                if (zapazanjeUVeziDeteta.getMane() != null) {
                    existingZapazanjeUVeziDeteta.setMane(zapazanjeUVeziDeteta.getMane());
                }
                if (zapazanjeUVeziDeteta.getPredlozi() != null) {
                    existingZapazanjeUVeziDeteta.setPredlozi(zapazanjeUVeziDeteta.getPredlozi());
                }
                if (zapazanjeUVeziDeteta.getDatum() != null) {
                    existingZapazanjeUVeziDeteta.setDatum(zapazanjeUVeziDeteta.getDatum());
                }

                return existingZapazanjeUVeziDeteta;
            })
            .map(zapazanjeUVeziDetetaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZapazanjeUVeziDeteta> findAll(Pageable pageable) {
        log.debug("Request to get all ZapazanjeUVeziDetetas");
        return zapazanjeUVeziDetetaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ZapazanjeUVeziDeteta> findOne(Long id) {
        log.debug("Request to get ZapazanjeUVeziDeteta : {}", id);
        return zapazanjeUVeziDetetaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ZapazanjeUVeziDeteta : {}", id);
        zapazanjeUVeziDetetaRepository.deleteById(id);
    }

    @Override
    public Page<ZapazanjeUVeziDeteta> findByDete(Long idDeteta) {
        List<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetaList = this.zapazanjeUVeziDetetaRepository.findByDete(idDeteta);
        Page<ZapazanjeUVeziDeteta> page = new PageImpl<>(zapazanjeUVeziDetetaList);
        return page;
    }
}
