package com.moneyminder.moneyminderexpenses.processors.budget;

import com.moneyminder.moneyminderexpenses.feignClients.GroupFeignClient;
import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.specifications.BudgetSpecification;
import com.moneyminder.moneyminderexpenses.requestParams.BudgetRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrieveBudgetProcessor {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final GroupFeignClient groupFeignClient;

    public List<Budget> retrieveBudgets(final BudgetRequestParams params) {
        params.setGroupsIn(groupFeignClient.getGroupIdsByUsername(AuthUtils.getUsername()));
        return this.budgetMapper.toModelList(this.budgetRepository.findAll(this.buidlSpecification(params)));
    }

    public Budget retrieveBudgetById(final String budgetId) {
        return this.budgetMapper.toModel(this.budgetRepository.findById(budgetId).orElseThrow());
    }

    public String retrieveBudgetNameById(final String budgetId) {
        final BudgetEntity budgetEntity = this.budgetRepository.findById(budgetId).orElseThrow();
        return budgetEntity.getName();
    }

    private Specification<BudgetEntity> buidlSpecification(final BudgetRequestParams params) {
        return BudgetSpecification.filterByExpensesLimitGreaterOrEqualThan(params.getExpensesLimitGreaterOrEqualThan())
                .and(BudgetSpecification.filterByExpensesLimitLowerOrEqualThan(params.getExpensesLimitLowerOrEqualThan()))
                .and(BudgetSpecification.filterByStartDateBeforeOrEqualThan(params.getStartDateBeforeOrEqualThan()))
                .and(BudgetSpecification.filterByStartDateAfterOrEqualThan(params.getStartDateAfterOrEqualThan()))
                .and(BudgetSpecification.filterByEndDateBeforeOrEqualThan(params.getEndDateBeforeOrEqualThan()))
                .and(BudgetSpecification.filterByEndDateAfterOrEqualThan(params.getEndDateAfterOrEqualThan()))
                .and(BudgetSpecification.searchByName(params.getName()))
                .and(BudgetSpecification.searchByComment(params.getComment()))
                .and(BudgetSpecification.filterByFavorite(params.getFavorite()));
    }
}
