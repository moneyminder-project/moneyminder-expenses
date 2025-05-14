package com.moneyminder.moneyminderexpenses.processors.budget;

import com.moneyminder.moneyminderexpenses.dto.CreateGroupByUsernameDto;
import com.moneyminder.moneyminderexpenses.feignClients.GroupFeignClient;
import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SaveBudgetProcessorTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private GroupFeignClient groupFeignClient;

    @InjectMocks
    private SaveBudgetProcessor saveBudgetProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save budget test")
    void saveBudgetTest() {
        Budget budget = new Budget();
        budget.setName("New Budget");

        BudgetEntity budgetEntity = new BudgetEntity();
        BudgetEntity savedEntity = new BudgetEntity();
        Budget savedBudget = new Budget();

        when(budgetMapper.toEntity(any(Budget.class))).thenReturn(budgetEntity);
        when(budgetRepository.save(any(BudgetEntity.class))).thenReturn(savedEntity);
        when(budgetMapper.toModel(savedEntity)).thenReturn(savedBudget);
        when(groupFeignClient.createGroupForBudget(any(CreateGroupByUsernameDto.class))).thenReturn(UUID.randomUUID().toString());

        Budget result = saveBudgetProcessor.saveBudget(budget);

        assertNotNull(result);
        verify(budgetMapper).toEntity(budget);
        verify(budgetRepository).save(budgetEntity);
    }

    @Test
    @DisplayName("save budget with null id test")
    void saveBudgetWithIdThrowsException() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID().toString());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveBudgetProcessor.saveBudget(budget));

        assertEquals("Budget id must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("save budget list test")
    void saveBudgetListTest() {
        List<Budget> budgets = List.of(new Budget(), new Budget());

        SaveBudgetProcessor spyProcessor = spy(saveBudgetProcessor);
        doReturn(new Budget()).when(spyProcessor).saveBudget(any(Budget.class));

        List<Budget> result = spyProcessor.saveBudgetList(budgets);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spyProcessor, times(2)).saveBudget(any(Budget.class));
    }

    @Test
    @DisplayName("update budget test")
    void updateBudgetTest() {
        String budgetId = UUID.randomUUID().toString();
        Budget budget = new Budget();
        budget.setId(budgetId);
        budget.setName("Updated Budget");
        budget.setExpensesLimit(100.0);
        budget.setStartDate(LocalDate.now());
        budget.setEndDate(LocalDate.now().plusDays(5));
        budget.setFavorite(false);
        budget.setRecords(new ArrayList<>());

        BudgetEntity dbEntity = new BudgetEntity();
        dbEntity.setGroupId(UUID.randomUUID().toString());
        dbEntity.setUsernames(new ArrayList<>());

        BudgetEntity entityToSave = new BudgetEntity();
        Budget updatedBudget = new Budget();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(dbEntity));
        when(budgetMapper.toEntity(budget)).thenReturn(entityToSave);
        when(recordRepository.findAllByIdIn(anyList())).thenReturn(new ArrayList<>());
        when(budgetRepository.save(entityToSave)).thenReturn(entityToSave);
        when(budgetMapper.toModel(entityToSave)).thenReturn(updatedBudget);

        Budget result = saveBudgetProcessor.updateBudget(budgetId, budget);

        assertNotNull(result);
        verify(budgetRepository).findById(budgetId);
        verify(budgetMapper).toEntity(budget);
        verify(budgetRepository).save(entityToSave);
    }


    @Test
    @DisplayName("update budget id mismatch test")
    void updateBudgetIdMismatchTest() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID().toString());

        String differentId = UUID.randomUUID().toString();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveBudgetProcessor.updateBudget(differentId, budget));

        assertEquals("Budget don't match", exception.getMessage());
    }

    @Test
    @DisplayName("update budget list test")
    void updateBudgetListTest() {
        List<Budget> budgets = List.of(new Budget(), new Budget());
        budgets.get(0).setId(UUID.randomUUID().toString());
        budgets.get(1).setId(UUID.randomUUID().toString());

        SaveBudgetProcessor spyProcessor = spy(saveBudgetProcessor);
        doReturn(new Budget()).when(spyProcessor).updateBudget(anyString(), any(Budget.class));

        List<Budget> result = spyProcessor.updateBudgetList(budgets);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spyProcessor, times(2)).updateBudget(anyString(), any(Budget.class));
    }

}