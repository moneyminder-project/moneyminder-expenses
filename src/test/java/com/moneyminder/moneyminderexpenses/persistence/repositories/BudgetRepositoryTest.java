package com.moneyminder.moneyminderexpenses.persistence.repositories;

import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository budgetRepository;

    private BudgetEntity budget;

    @BeforeEach
    void setup() {
        budget = new BudgetEntity();
        budget.setName("Test Budget");
        budget.setComment("This is a test budget.");
        budget.setStartDate(LocalDate.now());
        budget.setEndDate(LocalDate.now().plusMonths(1));
        budget.setExpensesLimit(1000.0);
        budget.setTotalExpenses(100.0);
        budget.setTotalIncomes(200.0);
        budget.setGroupId("group-1");
        budget.setUsernames(List.of("user1", "user2"));
    }

    @Test
    @DisplayName("save and find by ID test")
    void saveAndFindById() {
        BudgetEntity savedBudget = budgetRepository.save(budget);

        Optional<BudgetEntity> found = budgetRepository.findById(savedBudget.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Budget", found.get().getName());
        assertEquals("group-1", found.get().getGroupId());
    }

    @Test
    @DisplayName("existsById test")
    void existsByIdTest() {
        BudgetEntity savedBudget = budgetRepository.save(budget);

        boolean exists = budgetRepository.existsById(savedBudget.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("findAllByIdIn test")
    void findAllByIdInTest() {
        BudgetEntity savedBudget1 = budgetRepository.save(budget);

        BudgetEntity budget2 = new BudgetEntity();
        budget2.setName("Second Budget");
        budget2.setComment("Second test budget.");
        budget2.setStartDate(LocalDate.now());
        budget2.setEndDate(LocalDate.now().plusMonths(2));
        budget2.setExpensesLimit(2000.0);
        budget2.setTotalExpenses(150.0);
        budget2.setTotalIncomes(250.0);
        budget2.setGroupId("group-2");
        budget2.setUsernames(List.of("user3", "user4"));

        BudgetEntity savedBudget2 = budgetRepository.save(budget2);

        List<String> ids = List.of(savedBudget1.getId(), savedBudget2.getId());

        List<BudgetEntity> budgets = budgetRepository.findAllByIdIn(ids);

        assertEquals(2, budgets.size());
    }

    @Test
    @DisplayName("findAllByGroupIdIn test")
    void findAllByGroupIdInTest() {
        budgetRepository.save(budget);

        BudgetEntity budget2 = new BudgetEntity();
        budget2.setName("Second Budget");
        budget2.setComment("Second test budget.");
        budget2.setStartDate(LocalDate.now());
        budget2.setEndDate(LocalDate.now().plusMonths(2));
        budget2.setExpensesLimit(2000.0);
        budget2.setTotalExpenses(150.0);
        budget2.setTotalIncomes(250.0);
        budget2.setGroupId("group-2");
        budget2.setUsernames(List.of("user3", "user4"));

        budgetRepository.save(budget2);

        List<String> groupIds = List.of("group-1", "group-2");

        List<BudgetEntity> budgets = budgetRepository.findAllByGroupIdIn(groupIds);

        assertEquals(2, budgets.size());
    }
}