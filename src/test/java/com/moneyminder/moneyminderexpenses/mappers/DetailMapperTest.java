package com.moneyminder.moneyminderexpenses.mappers;

import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.persistence.entities.DetailEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class DetailMapperTest {

    private final DetailMapper mapper = Mappers.getMapper(DetailMapper.class);

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId("record-uuid-123");

        DetailEntity entity = new DetailEntity();
        entity.setId("detail-uuid-123");
        entity.setName("detail");
        entity.setPricePerUnit(1500.0);
        entity.setUnits(1);
        entity.setTotalPrice(1500.0);
        entity.setRecord(recordEntity);

        Detail model = mapper.toModel(entity);

        assertEquals("detail-uuid-123", model.getId());
        assertEquals("detail", model.getName());
        assertEquals(1500.0, model.getPricePerUnit());
        assertEquals(1, model.getUnits());
        assertEquals(1500.0, model.getTotalPrice());
        assertEquals("record-uuid-123", model.getRecord());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        Detail model = Detail.builder()
                .id("detail-uuid-456")
                .name("detail")
                .pricePerUnit(800.0)
                .units(2)
                .totalPrice(1600.0)
                .record("record-uuid-456")
                .build();

        DetailEntity entity = mapper.toEntity(model);

        assertEquals("detail-uuid-456", entity.getId());
        assertEquals("detail", entity.getName());
        assertEquals(800.0, entity.getPricePerUnit());
        assertEquals(2, entity.getUnits());
        assertEquals(1600.0, entity.getTotalPrice());
        assertNotNull(entity.getRecord());
        assertEquals("record-uuid-456", entity.getRecord().getId());
    }

    @Test
    @DisplayName("Map String ID to RecordEntity")
    void testMapStringIdToRecordEntity() {
        RecordEntity recordEntity = mapper.mapStringIdToRecordEntity("record-uuid-789");

        assertNotNull(recordEntity);
        assertEquals("record-uuid-789", recordEntity.getId());
    }

}