package com.moneyminder.moneyminderexpenses.mappers;

import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    @Mapping(source = "details", target = "details", qualifiedByName = "mapDetailsToUUIDs")
    @Mapping(source = "budgets", target = "budgets", qualifiedByName = "mapBudgetsToUUIDs")
    Record toModel(RecordEntity entity);

    @Mapping(source = "details", target = "details", qualifiedByName = "mapUUIDsToDetails")
    @Mapping(source = "budgets", target = "budgets", qualifiedByName = "mapUUIDsToBudgets")
    RecordEntity toEntity(Record model);

    List<Record> toModelList(List<RecordEntity> entities);

    List<RecordEntity> toEntityList(List<Record> models);

    @Named("mapDetailsToUUIDs")
    default List<String> mapDetailsToUUIDs(List<DetailEntity> details) {
        return details.stream().map(detail -> detail.getId().toString()).collect(Collectors.toList());
    }

    @Named("mapBudgetsToUUIDs")
    default List<String> mapBudgetsToUUIDs(List<BudgetEntity> budgets) {
        return budgets.stream().map(budget -> budget.getId().toString()).collect(Collectors.toList());
    }

    @Named("mapUUIDsToDetails")
    default List<DetailEntity> mapUUIDsToDetails(List<String> detailIds) {
        if (detailIds == null || detailIds.isEmpty()) {
            return new ArrayList<>();
        }

        return detailIds.stream().map(id -> {
            DetailEntity detail = new DetailEntity();
            detail.setId(id);
            return detail;
        }).collect(Collectors.toList());
    }

    @Named("mapUUIDsToBudgets")
    default List<BudgetEntity> mapUUIDsToBudgets(List<String> budgetIds) {
        if (budgetIds == null || budgetIds.isEmpty()) {
            return new ArrayList<>();
        }

        return budgetIds.stream().map(id -> {
            BudgetEntity budget = new BudgetEntity();
            budget.setId(id);
            return budget;
        }).collect(Collectors.toList());
    }

}
