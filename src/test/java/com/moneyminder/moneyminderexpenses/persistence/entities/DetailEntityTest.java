package com.moneyminder.moneyminderexpenses.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetailEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        DetailEntity detailEntity = new DetailEntity();
        detailEntity.setId("detail-id-123");
        detailEntity.setName("detail");
        detailEntity.setPricePerUnit(1200.0);
        detailEntity.setUnits(1);
        detailEntity.setTotalPrice(1200.0);

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId("record-id-456");
        detailEntity.setRecord(recordEntity);

        assertEquals("detail-id-123", detailEntity.getId());
        assertEquals("detail", detailEntity.getName());
        assertEquals(1200.0, detailEntity.getPricePerUnit());
        assertEquals(1, detailEntity.getUnits());
        assertEquals(1200.0, detailEntity.getTotalPrice());
        assertNotNull(detailEntity.getRecord());
        assertEquals("record-id-456", detailEntity.getRecord().getId());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        DetailEntity detailEntity = new DetailEntity();

        detailEntity.setId("detail-id-789");
        detailEntity.setName("detail");
        detailEntity.setPricePerUnit(800.0);
        detailEntity.setUnits(2);
        detailEntity.setTotalPrice(1600.0);

        assertEquals("detail-id-789", detailEntity.getId());
        assertEquals("detail", detailEntity.getName());
        assertEquals(800.0, detailEntity.getPricePerUnit());
        assertEquals(2, detailEntity.getUnits());
        assertEquals(1600.0, detailEntity.getTotalPrice());
    }

}