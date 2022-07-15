package com.diplomski.myapp.repository;

import com.diplomski.myapp.domain.Dnevnik;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DnevnikRepositoryWithBagRelationships {
    Optional<Dnevnik> fetchBagRelationships(Optional<Dnevnik> dnevnik);

    List<Dnevnik> fetchBagRelationships(List<Dnevnik> dnevniks);

    Page<Dnevnik> fetchBagRelationships(Page<Dnevnik> dnevniks);
}
