package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.Dnevnik;
import com.diplomski.myapp.domain.PlanPrice;
import com.diplomski.myapp.domain.Prica;
import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.repository.DnevnikRepository;
import com.diplomski.myapp.repository.PlanPriceRepository;
import com.diplomski.myapp.repository.PricaRepository;
import com.diplomski.myapp.repository.VaspitacRepository;
import com.diplomski.myapp.service.PlanPriceService;
import java.time.LocalDate;
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
    private final PricaRepository pricaRepository;
    private final DnevnikRepository dnevnikRepository;
    private final VaspitacRepository vaspitacRepository;

    public PlanPriceServiceImpl(
        PlanPriceRepository planPriceRepository,
        PricaRepository pricaRepository,
        DnevnikRepository dnevnikRepository,
        VaspitacRepository vaspitacRepository
    ) {
        this.planPriceRepository = planPriceRepository;
        this.pricaRepository = pricaRepository;
        this.dnevnikRepository = dnevnikRepository;
        this.vaspitacRepository = vaspitacRepository;
    }

    @Override
    public PlanPrice save(PlanPrice planPrice, String username) {
        log.debug("Request to save PlanPrice : {}", planPrice);

        Prica newPrica = new Prica();
        PlanPrice returnedValue = planPriceRepository.save(planPrice);
        newPrica.setPlanPrice(planPrice);

        Vaspitac vaspitac = vaspitacRepository.getVaspitacIdByUsername(username);

        for (Dnevnik d : vaspitac.getDnevniks()) {
            if (d.getPocetakVazenja().isBefore(LocalDate.now()) && d.getKrajVazenja().isAfter(LocalDate.now())) {
                newPrica.setDnevnik(d);
                d.getPricas().add(newPrica);

                pricaRepository.save(newPrica);
                dnevnikRepository.save(d);
                break;
            }
        }

        return returnedValue;
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
