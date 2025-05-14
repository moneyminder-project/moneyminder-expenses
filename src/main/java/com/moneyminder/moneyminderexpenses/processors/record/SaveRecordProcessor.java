package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.processors.budget.SaveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import com.moneyminder.moneyminderexpenses.utils.MoneyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveRecordProcessor {
    private final RecordRepository recordRepository;
    private final BudgetRepository budgetRepository;
    private final RecordMapper recordMapper;
    private final SaveBudgetProcessor saveBudgetProcessor;
    private final BudgetMapper budgetMapper;

    public Record saveRecord(final Record record) {
        Assert.isNull(record.getId(), "Record id must be null");
        this.checkRecordAttributes(record);
        record.setMoney(MoneyUtils.round(record.getMoney()));

        if (record.getOwner() == null) {
            record.setOwner(AuthUtils.getUsername());
        }

        final RecordEntity recordEntity = this.recordMapper.toEntity(record);

        if (record.getBudgets() != null && !record.getBudgets().isEmpty()) {
            recordEntity.setBudgets(this.budgetRepository.findAllByIdIn(record.getBudgets()));
        }

        final Record savedRecord = this.recordMapper.toModel(this.recordRepository.save(recordEntity));

        this.updateBudgets(recordEntity.getBudgets());

        return savedRecord;
    }

    public List<Record> saveRecordList(final List<Record> records) {
        final List<Record> savedRecords = new ArrayList<>();

        records.forEach(record -> savedRecords.add(this.saveRecord(record)));

        return savedRecords;
    }

    public Record updateRecord(final String id, final Record record) {
        Assert.notNull(record.getId(), "Record id must not be null");
        Assert.isTrue(record.getId().equals(id), "Records don't match");
        this.checkRecordAttributes(record);
        final RecordEntity recordFromDb = this.recordRepository.findById(id).orElse(new RecordEntity());
        Assert.isTrue(recordFromDb.getOwner().equals(AuthUtils.getUsername()), "Owner is not the owner of the record");
        record.setMoney(MoneyUtils.round(record.getMoney()));

        if (record.getOwner() == null) {
            record.setOwner(AuthUtils.getUsername());
        }

        final RecordEntity recordEntity = this.recordMapper.toEntity(record);

        recordEntity.setBudgets(new ArrayList<>());

        if (record.getBudgets() != null && !record.getBudgets().isEmpty()) {
            recordEntity.setBudgets(this.budgetRepository.findAllByIdIn(record.getBudgets()));
        }

        recordEntity.getBudgets().forEach(budget -> {

        });

        final Record savedRecord = this.recordMapper.toModel(this.recordRepository.save(recordEntity));

        this.updateBudgets(recordEntity.getBudgets());

        return savedRecord;
    }

    public List<Record> updateRecordList(final List<Record> records) {
        final List<Record> updatedRecords = new ArrayList<>();

        records.forEach(record -> updatedRecords.add(this.updateRecord(record.getId(), record)));

        return updatedRecords;
    }

    private void checkRecordAttributes(final Record record) {
        Assert.notNull(record, "Record must not be null");
        Assert.notNull(record.getType(), "Record type must not be null");
        Assert.notNull(record.getMoney(), "Record money must not be null");
        Assert.notNull(record.getName(), "Record name must not be null");
        Assert.hasLength(record.getName(), "Record name must not be empty");
        Assert.notNull(record.getDate(), "Record date must not be null");
    }

    private void updateBudgets(final List<BudgetEntity> budgets) {
        budgets.forEach(budget -> {
            this.saveBudgetProcessor.updateBudget(budget.getId(), this.budgetMapper.toModel(budget));
        });
    }
}
