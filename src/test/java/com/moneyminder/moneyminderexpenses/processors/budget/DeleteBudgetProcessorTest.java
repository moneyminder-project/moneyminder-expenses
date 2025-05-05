package com.moneyminder.moneyminderexpenses.processors.budget;

import com.moneyminder.moneyminderexpenses.feignClients.GroupFeignClient;
import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.persistence.repositories.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteBudgetProcessorTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private GroupFeignClient groupFeignClient;

    @InjectMocks
    private DeleteBudgetProcessor deleteBudgetProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("delete budget test")
    void deleteBudgetTest() {
        String budgetId = UUID.randomUUID().toString();
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setGroupId(UUID.randomUUID().toString());
        budgetEntity.setRecords(new ArrayList<>());

        when(budgetRepository.existsById(budgetId)).thenReturn(true);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budgetEntity));

        deleteBudgetProcessor.deleteBudget(budgetId);

        verify(budgetRepository).existsById(budgetId);
        verify(budgetRepository).findById(budgetId);
        verify(groupFeignClient).deleteGroup(budgetEntity.getGroupId());
        verify(budgetRepository).deleteById(budgetId);
    }

    @Test
    @DisplayName("delete budget not found test")
    void deleteBudgetNotFoundTest() {
        String budgetId = UUID.randomUUID().toString();

        when(budgetRepository.existsById(budgetId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteBudgetProcessor.deleteBudget(budgetId));

        assertEquals("Budget does not exist", exception.getMessage());
        verify(budgetRepository).existsById(budgetId);
    }

    @Test
    @DisplayName("delete budget list test")
    void deleteBudgetListTest() {
        String budgetId1 = UUID.randomUUID().toString();
        String budgetId2 = UUID.randomUUID().toString();
        List<String> budgetIds = List.of(budgetId1, budgetId2);

        DeleteBudgetProcessor spyProcessor = spy(deleteBudgetProcessor);
        doNothing().when(spyProcessor).deleteBudget(anyString());

        spyProcessor.deleteBudgetList(budgetIds);

        verify(spyProcessor, times(2)).deleteBudget(anyString());
    }

}