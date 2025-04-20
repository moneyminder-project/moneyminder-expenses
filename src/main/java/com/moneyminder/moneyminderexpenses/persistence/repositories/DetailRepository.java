package com.moneyminder.moneyminderexpenses.persistence.repositories;

import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DetailRepository extends CrudRepository<DetailEntity, String>, JpaSpecificationExecutor<DetailEntity> {
    boolean existsById(String id);

    List<DetailEntity> findAllByIdIn(List<String> ids);
}
