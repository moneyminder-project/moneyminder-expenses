package com.moneyminder.moneyminderexpenses.persistence.entities;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId("record-id-123");
        recordEntity.setType(RecordType.EXPENSE);
        recordEntity.setName("record");
        recordEntity.setMoney(120.0);
        recordEntity.setDate(LocalDate.of(2025, 4, 20));
        recordEntity.setComment("comment");
        recordEntity.setOwner("user123");

        DetailEntity detail1 = new DetailEntity();
        detail1.setId("detail1");

        DetailEntity detail2 = new DetailEntity();
        detail2.setId("detail2");

        BudgetEntity budget1 = new BudgetEntity();
        budget1.setId("budget1");

        BudgetEntity budget2 = new BudgetEntity();
        budget2.setId("budget2");

        recordEntity.setDetails(List.of(detail1, detail2));
        recordEntity.setBudgets(List.of(budget1, budget2));

        assertEquals("record-id-123", recordEntity.getId());
        assertEquals(RecordType.EXPENSE, recordEntity.getType());
        assertEquals("record", recordEntity.getName());
        assertEquals(120.0, recordEntity.getMoney());
        assertEquals(LocalDate.of(2025, 4, 20), recordEntity.getDate());
        assertEquals("comment", recordEntity.getComment());
        assertEquals("user123", recordEntity.getOwner());
        assertEquals(2, recordEntity.getDetails().size());
        assertEquals(2, recordEntity.getBudgets().size());
        assertEquals("detail1", recordEntity.getDetails().get(0).getId());
        assertEquals("budget1", recordEntity.getBudgets().get(0).getId());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        RecordEntity recordEntity = new RecordEntity();

        recordEntity.setId("record-id-456");
        recordEntity.setType(RecordType.INCOME);
        recordEntity.setName("income");
        recordEntity.setMoney(3000.0);
        recordEntity.setDate(LocalDate.of(2025, 5, 1));
        recordEntity.setComment("comment");
        recordEntity.setOwner("user456");

        assertEquals("record-id-456", recordEntity.getId());
        assertEquals(RecordType.INCOME, recordEntity.getType());
        assertEquals("income", recordEntity.getName());
        assertEquals(3000.0, recordEntity.getMoney());
        assertEquals(LocalDate.of(2025, 5, 1), recordEntity.getDate());
        assertEquals("comment", recordEntity.getComment());
        assertEquals("user456", recordEntity.getOwner());
    }

}