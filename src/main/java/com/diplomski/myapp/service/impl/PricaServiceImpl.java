package com.diplomski.myapp.service.impl;

import com.diplomski.myapp.domain.KonacnaPrica;
import com.diplomski.myapp.domain.PlanPrice;
import com.diplomski.myapp.domain.Prica;
import com.diplomski.myapp.domain.Vaspitac;
import com.diplomski.myapp.repository.PlanPriceRepository;
import com.diplomski.myapp.repository.PricaRepository;
import com.diplomski.myapp.service.PricaService;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prica}.
 */
@Service
@Transactional
public class PricaServiceImpl implements PricaService {

    private final Logger log = LoggerFactory.getLogger(PricaServiceImpl.class);

    private final PricaRepository pricaRepository;

    private final PlanPriceRepository planPriceRepository;

    public PricaServiceImpl(PricaRepository pricaRepository, PlanPriceRepository planPriceRepository) {
        this.pricaRepository = pricaRepository;
        this.planPriceRepository = planPriceRepository;
    }

    @Override
    public Prica save(Prica prica) {
        log.debug("Request to save Prica : {}", prica);
        return pricaRepository.save(prica);
    }

    @Override
    public Prica update(Prica prica) {
        log.debug("Request to save Prica : {}", prica);
        return pricaRepository.save(prica);
    }

    @Override
    public Optional<Prica> partialUpdate(Prica prica) {
        log.debug("Request to partially update Prica : {}", prica);

        return pricaRepository
            .findById(prica.getId())
            .map(existingPrica -> {
                return existingPrica;
            })
            .map(pricaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Prica> findAll(Pageable pageable) {
        log.debug("Request to get all Pricas");
        return pricaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prica> findOne(Long id) {
        log.debug("Request to get Prica : {}", id);
        return pricaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prica : {}", id);
        pricaRepository.deleteById(id);
    }

    @Override
    public KonacnaPrica startWriting(Long id) {
        Prica prica = this.pricaRepository.getById(id);
        PlanPrice plan = prica.getPlanPrice();
        StringBuilder text = new StringBuilder();
        text.append("Prica: Prica o projektu ");
        text.append(plan.getNazivTeme());
        text.append("/n");
        text.append("Vaspitno-obrazovna grupa " + prica.getDnevnik().getGrupa().getTipGrupe());
        text.append("/n");
        text.append("E-Vrtic Leptiric");
        text.append("/n");
        text.append("Projekat " + plan.getNazivTeme());
        text.append(" razvijan je sa decom od ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.mm.yyyy.");
        text.append(" razvijan je sa decom od ");
        text.append(plan.getDatumPocetka().format(formatter));
        text.append(" do ");
        text.append(plan.getDatumZavrsetka().format(formatter));
        text.append(".");
        text.append("Pocetna ideja za projekat " + plan.getPocetnaIdeja());

        for (int i = 0; i < plan.getMaterijali().split("\\*").length; i++) {
            text.append("Posetili smo " + plan.getMesta().split("\\*")[i] + ".");
            text.append("To nas je podstaklo da napravimo   pri cemu smo koristili " + plan.getMaterijali().split("\\*")[i] + ".");
            text.append("U ovom projektu sam ucestvovao/la tako sto " + plan.getNacinUcescaVaspitaca().split("\\*")[i] + ".");
            text.append("Takodje i roditelji su ucestvovali u ovom projektu. " + plan.getUcescePorodice().split("\\*")[i] + ".");
        }
        KonacnaPrica konacnaPrica = new KonacnaPrica();
        konacnaPrica.setTekst(text.toString()); //todo mozda ce ovde biti  problem
        return konacnaPrica;
    }
}
