package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Dnevnik;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DnevnikRepositoryWithBagRelationshipsImpl implements DnevnikRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Dnevnik> fetchBagRelationships(Optional<Dnevnik> dnevnik) {
        return dnevnik.map(this::fetchVaspitacs);
    }

    @Override
    public Page<Dnevnik> fetchBagRelationships(Page<Dnevnik> dnevniks) {
        return new PageImpl<>(fetchBagRelationships(dnevniks.getContent()), dnevniks.getPageable(), dnevniks.getTotalElements());
    }

    @Override
    public List<Dnevnik> fetchBagRelationships(List<Dnevnik> dnevniks) {
        return Optional.of(dnevniks).map(this::fetchVaspitacs).orElse(Collections.emptyList());
    }

    Dnevnik fetchVaspitacs(Dnevnik result) {
        return entityManager
            .createQuery("select dnevnik from Dnevnik dnevnik left join fetch dnevnik.vaspitacs where dnevnik is :dnevnik", Dnevnik.class)
            .setParameter("dnevnik", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Dnevnik> fetchVaspitacs(List<Dnevnik> dnevniks) {
        return entityManager
            .createQuery(
                "select distinct dnevnik from Dnevnik dnevnik left join fetch dnevnik.vaspitacs where dnevnik in :dnevniks",
                Dnevnik.class
            )
            .setParameter("dnevniks", dnevniks)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
