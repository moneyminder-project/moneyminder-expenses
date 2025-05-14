package com.moneyminder.moneyminderexpenses.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setId("budget-id-123");
        budgetEntity.setName("budget");
        budgetEntity.setComment("budget comment");
        budgetEntity.setStartDate(LocalDate.of(2025, 1, 1));
        budgetEntity.setEndDate(LocalDate.of(2025, 12, 31));
        budgetEntity.setExpensesLimit(5000.0);
        budgetEntity.setTotalExpenses(1200.0);
        budgetEntity.setTotalIncomes(2000.0);
        budgetEntity.setGroupId("group-id-456");

        budgetEntity.setUsernames(List.of("user1", "user2"));

        RecordEntity record1 = new RecordEntity();
        record1.setId("record1");

        RecordEntity record2 = new RecordEntity();
        record2.setId("record2");

        budgetEntity.setRecords(List.of(record1, record2));

        assertEquals("budget-id-123", budgetEntity.getId());
        assertEquals("budget", budgetEntity.getName());
        assertEquals("budget comment", budgetEntity.getComment());
        assertEquals(LocalDate.of(2025, 1, 1), budgetEntity.getStartDate());
        assertEquals(LocalDate.of(2025, 12, 31), budgetEntity.getEndDate());
        assertEquals(5000.0, budgetEntity.getExpensesLimit());
        assertEquals(1200.0, budgetEntity.getTotalExpenses());
        assertEquals(2000.0, budgetEntity.getTotalIncomes());
        assertEquals("group-id-456", budgetEntity.getGroupId());
        assertEquals(2, budgetEntity.getUsernames().size());
        assertEquals(2, budgetEntity.getRecords().size());
        assertEquals("record1", budgetEntity.getRecords().get(0).getId());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        BudgetEntity budgetEntity = new BudgetEntity();

        budgetEntity.setId("budget-id-789");
        budgetEntity.setName("budget");
        budgetEntity.setComment("budget comment");
        budgetEntity.setStartDate(LocalDate.of(2025, 3, 1));
        budgetEntity.setEndDate(LocalDate.of(2025, 9, 30));
        budgetEntity.setExpensesLimit(8000.0);
        budgetEntity.setTotalExpenses(0.0);
        budgetEntity.setTotalIncomes(0.0);
        budgetEntity.setGroupId("group-id-999");

        assertEquals("budget-id-789", budgetEntity.getId());
        assertEquals("budget", budgetEntity.getName());
        assertEquals("budget comment", budgetEntity.getComment());
        assertEquals(LocalDate.of(2025, 3, 1), budgetEntity.getStartDate());
        assertEquals(LocalDate.of(2025, 9, 30), budgetEntity.getEndDate());
        assertEquals(8000.0, budgetEntity.getExpensesLimit());
        assertEquals(0.0, budgetEntity.getTotalExpenses());
        assertEquals(0.0, budgetEntity.getTotalIncomes());
        assertEquals("group-id-999", budgetEntity.getGroupId());
    }

}