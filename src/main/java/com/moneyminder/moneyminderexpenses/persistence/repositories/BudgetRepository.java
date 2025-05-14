package com.moneyminder.moneyminderexpenses.persistence.repositories;

import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BudgetRepository extends CrudRepository<BudgetEntity, String>, JpaSpecificationExecutor<BudgetEntity> {
    boolean existsById(String id);

    List<BudgetEntity> findAllByIdIn(List<String> ids);
    List<BudgetEntity> findAllByGroupIdIn(List<String> ids);
}
