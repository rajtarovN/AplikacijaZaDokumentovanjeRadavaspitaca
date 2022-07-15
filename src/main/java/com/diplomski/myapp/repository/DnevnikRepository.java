package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Dnevnik;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dnevnik entity.
 */
@Repository
public interface DnevnikRepository extends DnevnikRepositoryWithBagRelationships, JpaRepository<Dnevnik, Long> {
    default Optional<Dnevnik> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Dnevnik> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Dnevnik> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
