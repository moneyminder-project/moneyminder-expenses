package com.moneyminder.moneyminderexpenses.mappers;

import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BudgetMapper {


    @Mapping(source = "records", target = "records", qualifiedByName = "mapRecordsToUUIDs")
    @Mapping(target = "favorite", expression = "java(isFavorite(entity))")
    Budget toModel(BudgetEntity entity);

    @Mapping(source = "records", target = "records", qualifiedByName = "mapUUIDsToRecords")
    @Mapping(target = "usernames", expression = "java(updateFavorites(model))")
    BudgetEntity toEntity(Budget model);

    List<Budget> toModelList(List<BudgetEntity> entities);

    List<BudgetEntity> toEntityList(List<Budget> models);

    @Named("mapRecordsToUUIDs")
    default List<String> mapRecordsToUUIDs(List<RecordEntity> records) {
        return records.stream().map(RecordEntity::getId).collect(Collectors.toList());
    }

    @Named("mapUUIDsToRecords")
    default List<RecordEntity> mapUUIDsToRecords(List<String> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            return new ArrayList<>();
        }

        return recordIds.stream().map(id -> {
            RecordEntity record = new RecordEntity();
            record.setId(id);
            return record;
        }).collect(Collectors.toList());
    }

    default Boolean isFavorite(BudgetEntity entity) {
        String username = AuthUtils.getUsername();
        return entity.getUsernames() != null && username != null && entity.getUsernames().contains(username);
    }

    default List<String> updateFavorites(Budget model) {
        String currentUser = AuthUtils.getUsername();
        List<String> usernames = new ArrayList<>();

        if (Boolean.TRUE.equals(model.getFavorite()) && currentUser != null) {
            usernames.add(currentUser);
        }

        return usernames;
    }
}
