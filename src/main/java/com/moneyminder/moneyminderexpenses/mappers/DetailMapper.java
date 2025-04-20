package com.moneyminder.moneyminderexpenses.mappers;

import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetailMapper {

    @Mapping(source = "record.id", target = "record")
    Detail toModel(DetailEntity entity);

    @Mapping(source = "record", target = "record", qualifiedByName = "mapStringIdToRecordEntity")
    DetailEntity toEntity(Detail model);

    List<Detail> toModelList(List<DetailEntity> entities);

    List<DetailEntity> toEntityList(List<Detail> models);

    @Named("mapStringIdToRecordEntity")
    default RecordEntity mapStringIdToRecordEntity(String recordId) {
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(recordId);
        return recordEntity;
    }
}
