package com.moneyminder.moneyminderexpenses.persistence.repositories;

import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends CrudRepository<RecordEntity, String>, JpaSpecificationExecutor<RecordEntity> {
    boolean existsById(String id);

    List<RecordEntity> findAllByIdIn(List<String> ids);
    List<RecordEntity> findAllByBudgets_Id(String id);

}
