package com.moneyminder.moneyminderexpenses.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BudgetTest {

    private static Validator validator;

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        Budget budget = Budget.builder()
                .id("budget1")
                .name("budget")
                .comment("comment")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .expensesLimit(5000.0)
                .totalExpenses(1200.0)
                .totalIncomes(2000.0)
                .groupId("groupId123")
                .favorite(true)
                .records(List.of("record1", "record2"))
                .build();

        assertEquals("budget1", budget.getId());
        assertEquals("budget", budget.getName());
        assertEquals("comment", budget.getComment());
        assertEquals(LocalDate.of(2025, 1, 1), budget.getStartDate());
        assertEquals(LocalDate.of(2025, 12, 31), budget.getEndDate());
        assertEquals(5000.0, budget.getExpensesLimit());
        assertEquals(1200.0, budget.getTotalExpenses());
        assertEquals(2000.0, budget.getTotalIncomes());
        assertEquals("groupId123", budget.getGroupId());
        assertTrue(budget.getFavorite());
        assertEquals(2, budget.getRecords().size());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        Budget budget = new Budget();

        budget.setId("budget2");
        budget.setName("budget");
        budget.setComment("comment");
        budget.setStartDate(LocalDate.of(2025, 2, 1));
        budget.setEndDate(LocalDate.of(2025, 11, 30));
        budget.setExpensesLimit(10000.0);
        budget.setTotalExpenses(3000.0);
        budget.setTotalIncomes(4000.0);
        budget.setGroupId("groupId456");
        budget.setFavorite(false);
        budget.setRecords(List.of("record3"));

        assertEquals("budget2", budget.getId());
        assertEquals("budget", budget.getName());
        assertEquals("comment", budget.getComment());
        assertEquals(LocalDate.of(2025, 2, 1), budget.getStartDate());
        assertEquals(LocalDate.of(2025, 11, 30), budget.getEndDate());
        assertEquals(10000.0, budget.getExpensesLimit());
        assertEquals(3000.0, budget.getTotalExpenses());
        assertEquals(4000.0, budget.getTotalIncomes());
        assertEquals("groupId456", budget.getGroupId());
        assertFalse(budget.getFavorite());
        assertEquals(1, budget.getRecords().size());
    }

    @Test
    @DisplayName("Budget name blank test")
    void testValidationFailWhenNameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Budget budget = Budget.builder()
                .id("budget3")
                .name(" ")
                .comment("No comment")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .expensesLimit(1000.0)
                .totalExpenses(100.0)
                .totalIncomes(200.0)
                .groupId("group789")
                .favorite(false)
                .records(List.of())
                .build();

        Set<ConstraintViolation<Budget>> violations = validator.validate(budget);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Se debe especificar el nombre del presupuesto")));
    }

    @Test
    @DisplayName("Budget name not blank test")
    void testValidationPassWhenNameIsValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Budget budget = Budget.builder()
                .id("budget4")
                .name("budget")
                .comment("comment")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(2))
                .expensesLimit(2500.0)
                .totalExpenses(500.0)
                .totalIncomes(800.0)
                .groupId("group321")
                .favorite(true)
                .records(List.of("recordX", "recordY"))
                .build();

        Set<ConstraintViolation<Budget>> violations = validator.validate(budget);

        assertTrue(violations.isEmpty());
    }

}