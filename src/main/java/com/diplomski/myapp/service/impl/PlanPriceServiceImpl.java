package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.PlanPrice;
import com.diplomski.myapp.repository.PlanPriceRepository;
import com.diplomski.myapp.service.PlanPriceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlanPrice}.
 */
@Service
@Transactional
public class PlanPriceServiceImpl implements PlanPriceService {

    private final Logger log = LoggerFactory.getLogger(PlanPriceServiceImpl.class);

    private final PlanPriceRepository planPriceRepository;

    public PlanPriceServiceImpl(PlanPriceRepository planPriceRepository) {
        this.planPriceRepository = planPriceRepository;
    }

    @Override
    public PlanPrice save(PlanPrice planPrice) {
        log.debug("Request to save PlanPrice : {}", planPrice);
        return planPriceRepository.save(planPrice);
    }

    @Override
    public PlanPrice update(PlanPrice planPrice) {
        log.debug("Request to save PlanPrice : {}", planPrice);
        return planPriceRepository.save(planPrice);
    }

    @Override
    public Optional<PlanPrice> partialUpdate(PlanPrice planPrice) {
        log.debug("Request to partially update PlanPrice : {}", planPrice);

        return planPriceRepository
            .findById(planPrice.getId())
            .map(existingPlanPrice -> {
                if (planPrice.getIzvoriSaznanja() != null) {
                    existingPlanPrice.setIzvoriSaznanja(planPrice.getIzvoriSaznanja());
                }
                if (planPrice.getNazivTeme() != null) {
                    existingPlanPrice.setNazivTeme(planPrice.getNazivTeme());
                }
                if (planPrice.getPocetnaIdeja() != null) {
                    existingPlanPrice.setPocetnaIdeja(planPrice.getPocetnaIdeja());
                }
                if (planPrice.getDatumPocetka() != null) {
                    existingPlanPrice.setDatumPocetka(planPrice.getDatumPocetka());
                }
                if (planPrice.getNacinUcescaVaspitaca() != null) {
                    existingPlanPrice.setNacinUcescaVaspitaca(planPrice.getNacinUcescaVaspitaca());
                }
                if (planPrice.getMaterijali() != null) {
                    existingPlanPrice.setMaterijali(planPrice.getMaterijali());
                }
                if (planPrice.getUcescePorodice() != null) {
                    existingPlanPrice.setUcescePorodice(planPrice.getUcescePorodice());
                }
                if (planPrice.getMesta() != null) {
                    existingPlanPrice.setMesta(planPrice.getMesta());
                }
                if (planPrice.getDatumZavrsetka() != null) {
                    existingPlanPrice.setDatumZavrsetka(planPrice.getDatumZavrsetka());
                }

                return existingPlanPrice;
            })
            .map(planPriceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanPrice> findAll(Pageable pageable) {
        log.debug("Request to get all PlanPrices");
        return planPriceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanPrice> findOne(Long id) {
        log.debug("Request to get PlanPrice : {}", id);
        return planPriceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlanPrice : {}", id);
        planPriceRepository.deleteById(id);
    }
}
