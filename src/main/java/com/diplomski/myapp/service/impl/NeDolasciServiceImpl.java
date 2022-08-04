package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.domain.NeDolasci;
import com.diplomski.myapp.domain.PotrebanMaterijal;
import com.diplomski.myapp.repository.DeteRepository;
import com.diplomski.myapp.repository.DnevnikRepository;
import com.diplomski.myapp.repository.NeDolasciRepository;
import com.diplomski.myapp.service.NeDolasciService;
import com.diplomski.myapp.web.rest.dto.NeDolasciDTO;
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
 * Service Implementation for managing {@link NeDolasci}.
 */
@Service
@Transactional
public class NeDolasciServiceImpl implements NeDolasciService {

    private final Logger log = LoggerFactory.getLogger(NeDolasciServiceImpl.class);

    private final NeDolasciRepository neDolasciRepository;

    private final DeteRepository deteRepository;
    private final DnevnikRepository dnevnikRepository;

    public NeDolasciServiceImpl(
        NeDolasciRepository neDolasciRepository,
        DeteRepository deteRepository,
        DnevnikRepository dnevnikRepository
    ) {
        this.neDolasciRepository = neDolasciRepository;
        this.deteRepository = deteRepository;
        this.dnevnikRepository = dnevnikRepository;
    }

    @Override
    public NeDolasci save(NeDolasci neDolasci) {
        log.debug("Request to save NeDolasci : {}", neDolasci);
        return neDolasciRepository.save(neDolasci);
    }

    @Override
    public NeDolasci update(NeDolasci neDolasci) {
        log.debug("Request to save NeDolasci : {}", neDolasci);
        return neDolasciRepository.save(neDolasci);
    }

    @Override
    public Optional<NeDolasci> partialUpdate(NeDolasci neDolasci) {
        log.debug("Request to partially update NeDolasci : {}", neDolasci);

        return neDolasciRepository
            .findById(neDolasci.getId())
            .map(existingNeDolasci -> {
                if (neDolasci.getDatum() != null) {
                    existingNeDolasci.setDatum(neDolasci.getDatum());
                }
                if (neDolasci.getRazlog() != null) {
                    existingNeDolasci.setRazlog(neDolasci.getRazlog());
                }

                return existingNeDolasci;
            })
            .map(neDolasciRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NeDolasci> findAll(Pageable pageable) {
        log.debug("Request to get all NeDolascis");
        return neDolasciRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NeDolasci> findOne(Long id) {
        log.debug("Request to get NeDolasci : {}", id);
        return neDolasciRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NeDolasci : {}", id);
        neDolasciRepository.deleteById(id);
    }

    @Override
    public String saveList(List<NeDolasciDTO> neDolasci) {
        log.debug("Request to save NeDolasci : {}", neDolasci);
        for (NeDolasciDTO dto : neDolasci) {
            Optional<Dete> dete = this.deteRepository.findById(dto.getIdDeteta());
            Optional<Dnevnik> dnevnik = this.dnevnikRepository.findById(dto.getIdDeteta());
            neDolasciRepository.save(new NeDolasci(dto, dete.get(), dnevnik.get()));
        }
        return "Successfully saved nedolasci";
    }

    @Override
    public Page<NeDolasci> findAllByGrupa(Pageable pageable, Long id) {
        List<NeDolasci> izostanci =
            this.neDolasciRepository.findByGrupaId(id)
                .subList((pageable.getPageNumber()) * pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());

        Page<NeDolasci> page = new PageImpl<>(izostanci);
        return page;
    }
}
