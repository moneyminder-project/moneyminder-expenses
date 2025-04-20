package com.moneyminder.moneyminderexpenses.processors.record;

import com.moneyminder.moneyminderexpenses.dto.RecordWithoutDetails;
import com.moneyminder.moneyminderexpenses.mappers.RecordMapper;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import com.moneyminder.moneyminderexpenses.persistence.specifications.RecordSpecification;
import com.moneyminder.moneyminderexpenses.requestParams.RecordRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrieveRecordProcessor {
    private final RecordRepository recordRepository;
    private final BudgetRepository budgetRepository;
    private final RecordMapper recordMapper;

    public List<Record> retrieveRecords(final RecordRequestParams params, final String username) {
        params.setOwner(username);
        return this.recordMapper.toModelList(this.recordRepository.findAll(this.buildSpecification(params)));
    }

    public Record retrieveRecordById(final String id) {
        final RecordEntity recordEntity = this.recordRepository.findById(id).orElse(new RecordEntity());
        Assert.isTrue(recordEntity.getOwner().equals(AuthUtils.getUsername()), "Owner is not the owner of the record");
        return this.recordMapper.toModel(this.recordRepository.findById(id).orElseThrow());
    }

    public List<RecordWithoutDetails> retrieveRecordByBudgetId(final String budgetId) {
        final BudgetEntity budgetEntity = this.budgetRepository.findById(budgetId).orElse(new BudgetEntity());
        final List<RecordEntity> recordsByBudget = this.recordRepository.findAllByBudgets_Id(budgetId);
        final List<RecordWithoutDetails> recordWithoutDetails = new ArrayList<>();
        recordsByBudget.forEach(recordEntity -> {
            final RecordWithoutDetails tempRecord = new RecordWithoutDetails();
            tempRecord.setId(recordEntity.getId());
            tempRecord.setType(recordEntity.getType());
            tempRecord.setName(recordEntity.getName());
            tempRecord.setMoney(recordEntity.getMoney());
            tempRecord.setDate(recordEntity.getDate());
            tempRecord.setComment(recordEntity.getComment());
            tempRecord.setOwner(recordEntity.getOwner());
            recordWithoutDetails.add(tempRecord);
        });
        
        return recordWithoutDetails;
    }

    private Specification<RecordEntity> buildSpecification(final RecordRequestParams params) {
        return RecordSpecification.filterByMoneyGreaterOrEqualThan(params.getMoneyGreaterOrEqualThan())
                .and(RecordSpecification.filterByMoneyLessOrEqualThan(params.getMoneyLowerOrEqualThan()))
                .and(RecordSpecification.filterByDateBeforeOrEqualThan(params.getDateBeforeOrEqualThan()))
                .and(RecordSpecification.filterByDateAfterOrEqualThan(params.getDateAfterOrEqualThan()))
                .and(RecordSpecification.filterByBudgetsIn(params.getBudgetsIn()))
                .and(RecordSpecification.filterByOwner(params.getOwner()))
                .and(RecordSpecification.filterByType(params.getRecordType()))
                .and(RecordSpecification.searchByName(params.getName()))
                .and(RecordSpecification.searchByComment(params.getComment()));
    }
}
