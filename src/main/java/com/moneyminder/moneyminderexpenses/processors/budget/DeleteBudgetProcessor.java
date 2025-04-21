package com.moneyminder.moneyminderexpenses.processors.budget;

import com.moneyminder.moneyminderexpenses.feignClients.GroupFeignClient;
import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteBudgetProcessor {
    private final BudgetRepository budgetRepository;
    private final RecordRepository recordRepository;
    private final BudgetMapper budgetMapper;
    private final GroupFeignClient groupFeignClient;

    @Transactional
    public void deleteBudget(String id) {
        Assert.isTrue(this.budgetRepository.existsById(id), "Budget does not exist");
        final BudgetEntity budgetEntity = this.budgetRepository.findById(id).orElse(new BudgetEntity());

        budgetEntity.getRecords().forEach(record -> {
            record.setBudgets(record.getBudgets().stream().filter(budgetInd ->
                    !budgetInd.getId().equals(budgetEntity.getId())).collect(Collectors.toList()));
            this.recordRepository.save(record);
        });

        this.groupFeignClient.deleteGroup(budgetEntity.getGroupId());

        this.budgetRepository.deleteById(id);
    }

    public void deleteBudgetList(List<String> ids) {
        ids.forEach(this::deleteBudget);
    }
}
