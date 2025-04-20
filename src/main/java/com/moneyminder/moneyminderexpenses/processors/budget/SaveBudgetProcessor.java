package com.moneyminder.moneyminderexpenses.processors.budget;

import com.moneyminder.moneyminderexpenses.dto.CreateGroupByUsernameDto;
import com.moneyminder.moneyminderexpenses.feignClients.GroupFeignClient;
import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import com.moneyminder.moneyminderexpenses.utils.MoneyUtils;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveBudgetProcessor {
    private final BudgetRepository budgetRepository;
    private final RecordRepository recordRepository;
    private final BudgetMapper budgetMapper;
    private final GroupFeignClient groupFeignClient;

    public Budget saveBudget(final Budget budget) {
        Assert.isNull(budget.getId(), "Budget id must not be null");
        this.checkBudgetAttributes(budget);

        if (budget.getExpensesLimit() != null) {
            budget.setExpensesLimit(MoneyUtils.round(budget.getExpensesLimit()));
        }

        if (budget.getFavorite() == null) {
            budget.setFavorite(false);
        }

        if (budget.getStartDate() != null && budget.getEndDate() != null && budget.getEndDate().isBefore(budget.getStartDate())) {
            budget.setEndDate(budget.getStartDate());
        }

        if (budget.getGroupId() == null) {
            String groupId = groupFeignClient.createGroupForBudget(new CreateGroupByUsernameDto(AuthUtils.getUsername(), budget.getName()));
            budget.setGroupId(groupId);
        }

        final BudgetEntity budgetEntity = this.budgetMapper.toEntity(budget);

        if (budget.getRecords() != null && !budget.getRecords().isEmpty()) {
            budgetEntity.setRecords(this.recordRepository.findAllByIdIn(budget.getRecords()));
            this.calculateIncomesAndExpenses(budgetEntity);
        }

        return budgetMapper.toModel(this.budgetRepository.save(budgetEntity));
    }

    public List<Budget> saveBudgetList(final List<Budget> budgets) {
        List<Budget> savedBudgets = new ArrayList<>();

        if (budgets != null && !budgets.isEmpty()) {
            budgets.forEach(budget -> savedBudgets.add(this.saveBudget(budget)));
        }

        return savedBudgets;
    }

    @Transactional
    public Budget updateBudget(final String id, final Budget budget) {
        Assert.isTrue(id.equals(budget.getId()), "Budget don't match");
        this.checkBudgetAttributes(budget);

        if (budget.getStartDate() != null && budget.getEndDate() != null && budget.getEndDate().isBefore(budget.getStartDate())) {
            budget.setEndDate(budget.getStartDate());
        }

        final BudgetEntity dbBudgetEntity = this.budgetRepository.findById(id).orElse(null);

        budget.setExpensesLimit(MoneyUtils.round(budget.getExpensesLimit()));

        final BudgetEntity budgetEntity = this.budgetMapper.toEntity(budget);

        budgetEntity.setTotalExpenses(0.0);
        budgetEntity.setTotalIncomes(0.0);

        budgetEntity.setRecords(new ArrayList<>());

        List<RecordEntity> records = this.recordRepository.findAllByIdIn(budget.getRecords());
        budgetEntity.setRecords(records);

        if (dbBudgetEntity != null) {
            budgetEntity.setUsernames(dbBudgetEntity.getUsernames());
        }

        if (budget.getFavorite() && !budgetEntity.getUsernames().contains(AuthUtils.getUsername())) {
            budgetEntity.getUsernames().add(AuthUtils.getUsername());
        }

        if (!budget.getFavorite() && budgetEntity.getUsernames().contains(AuthUtils.getUsername())) {
            budgetEntity.getUsernames().remove(AuthUtils.getUsername());
        }

        final boolean modifiedRecordsCondition = dbBudgetEntity.getRecords() == null ||
                dbBudgetEntity.getRecords().size() != budget.getRecords().size() ||
                dbBudgetEntity.getRecords().stream().map(RecordEntity::getId).toList().stream()
                        .anyMatch(budget.getRecords()::contains);

        if (modifiedRecordsCondition && (budget.getRecords() == null || budget.getRecords().isEmpty())) {
            records.forEach(record -> {
                record.setBudgets(record.getBudgets().stream().filter(budgetInd ->
                        !budgetInd.getId().equals(budgetEntity.getId())).collect(Collectors.toList()));
                this.recordRepository.save(record);
            });
            budgetEntity.setRecords(new ArrayList<>());
        }

        if (modifiedRecordsCondition && budget.getRecords() != null && !budget.getRecords().isEmpty()) {
            records.forEach(record -> {
                record.setBudgets(record.getBudgets().stream().filter(budgetInd -> !budgetInd.getId()
                        .equals(budgetEntity.getId())).collect(Collectors.toList()));
                record.getBudgets().add(budgetEntity);
                this.recordRepository.save(record);
            });
            budgetEntity.setRecords(records);
            this.calculateIncomesAndExpenses(budgetEntity);
        }

        return budgetMapper.toModel(this.budgetRepository.save(budgetEntity));
    }

    public List<Budget> updateBudgetList(final List<Budget> budgets) {
        List<Budget> updatedBudgets = new ArrayList<>();

        if (budgets != null && !budgets.isEmpty()) {
            budgets.forEach(budget -> updatedBudgets.add(this.updateBudget(budget.getId(), budget)));
        }

        return updatedBudgets;
    }

    private void checkBudgetAttributes(final Budget budget) {
        Assert.notNull(budget, "Budget must not be null");
        Assert.notNull(budget.getName(), "Budget name must not be null");
    }

    private void calculateIncomesAndExpenses(final BudgetEntity budget) {
        budget.setTotalIncomes(budget.getRecords().stream().filter(record -> record.getType()
                .equals(RecordType.INCOME)).map(RecordEntity::getMoney).reduce(0.0, Double::sum));
        budget.setTotalExpenses(budget.getRecords().stream().filter(record -> record.getType()
                .equals(RecordType.EXPENSE)).map(RecordEntity::getMoney).reduce(0.0, Double::sum));
    }
}