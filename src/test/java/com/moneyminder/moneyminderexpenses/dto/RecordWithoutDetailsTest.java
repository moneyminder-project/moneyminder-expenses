package com.moneyminder.moneyminderexpenses.dto;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RecordWithoutDetailsTest {

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        RecordWithoutDetails record = RecordWithoutDetails.builder()
                .id("record1")
                .type(RecordType.EXPENSE)
                .name("expense")
                .money(25.0)
                .date(LocalDate.of(2025, 6, 15))
                .comment("expense comment")
                .owner("user123")
                .build();

        assertEquals("record1", record.getId());
        assertEquals(RecordType.EXPENSE, record.getType());
        assertEquals("expense", record.getName());
        assertEquals(25.0, record.getMoney());
        assertEquals(LocalDate.of(2025, 6, 15), record.getDate());
        assertEquals("expense comment", record.getComment());
        assertEquals("user123", record.getOwner());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        RecordWithoutDetails record = new RecordWithoutDetails();

        record.setId("record2");
        record.setType(RecordType.INCOME);
        record.setName("income");
        record.setMoney(500.0);
        record.setDate(LocalDate.of(2025, 7, 1));
        record.setComment("income comment");
        record.setOwner("user456");

        assertEquals("record2", record.getId());
        assertEquals(RecordType.INCOME, record.getType());
        assertEquals("income", record.getName());
        assertEquals(500.0, record.getMoney());
        assertEquals(LocalDate.of(2025, 7, 1), record.getDate());
        assertEquals("income comment", record.getComment());
        assertEquals("user456", record.getOwner());
    }

}