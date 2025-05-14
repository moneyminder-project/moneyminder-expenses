package com.moneyminder.moneyminderexpenses.persistence.repositories;

import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.entities.RecordEntity;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    private RecordEntity record;

    @BeforeEach
    void setup() {
        record = new RecordEntity();
        record.setType(RecordType.EXPENSE);
        record.setName("Test Record");
        record.setDate(LocalDate.now());
        record.setMoney(200.00);
        record.setOwner("test-owner");
        record = recordRepository.save(record);
    }

    @Test
    @DisplayName("save and findById test")
    void saveAndFindByIdTest() {
        Optional<RecordEntity> found = recordRepository.findById(record.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Record", found.get().getName());
        assertEquals(200.00, found.get().getMoney());
        assertEquals(record.getId(), found.get().getId());
    }

    @Test
    @DisplayName("existsById test")
    void existsByIdTest() {
        boolean exists = recordRepository.existsById(record.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsById no result test")
    void existsByIdFalseTest() {
        boolean exists = recordRepository.existsById("non-existent-id");

        assertFalse(exists);
    }

    @Test
    @DisplayName("findAllByIdIn test")
    void findAllByIdInTest() {
        RecordEntity secondRecord = new RecordEntity();
        secondRecord.setType(RecordType.INCOME);
        secondRecord.setName("Second Record");
        secondRecord.setDate(LocalDate.now());
        secondRecord.setMoney(500.00);
        secondRecord.setOwner("test-owner");
        secondRecord = recordRepository.save(secondRecord);

        List<String> ids = List.of(record.getId(), secondRecord.getId());
        List<RecordEntity> records = recordRepository.findAllByIdIn(ids);

        assertEquals(2, records.size());
    }

    @Test
    @DisplayName("findAllByBudgets_Id test")
    void findAllByBudgetsIdTest() {
        BudgetEntity budget = new BudgetEntity();
        budget.setName("Test Budget");
        budget.setGroupId("Group id");
        budget = budgetRepository.save(budget);

        record.setBudgets(new ArrayList<>(List.of(budget)));
        record = recordRepository.save(record);

        List<RecordEntity> records = recordRepository.findAllByBudgets_Id(budget.getId());

        assertNotNull(records);
        assertFalse(records.isEmpty());
        assertEquals(record.getId(), records.get(0).getId());
    }

}