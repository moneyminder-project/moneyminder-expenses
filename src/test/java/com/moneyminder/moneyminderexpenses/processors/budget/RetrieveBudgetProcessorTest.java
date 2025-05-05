package com.moneyminder.moneyminderexpenses.processors.budget;

import com.moneyminder.moneyminderexpenses.feignClients.GroupFeignClient;
import com.moneyminder.moneyminderexpenses.mappers.BudgetMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.persistence.entities.BudgetEntity;
import com.moneyminder.moneyminderexpenses.persistence.repositories.BudgetRepository;
import com.moneyminder.moneyminderexpenses.requestParams.BudgetRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RetrieveBudgetProcessorTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private GroupFeignClient groupFeignClient;

    @InjectMocks
    private RetrieveBudgetProcessor retrieveBudgetProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("retrieve budgets test with params")
    void retrieveBudgetsTest() {
        BudgetRequestParams params = new BudgetRequestParams();
        List<String> groups = List.of(UUID.randomUUID().toString());
        List<BudgetEntity> budgetEntities = List.of(new BudgetEntity());
        List<Budget> budgets = List.of(new Budget());

        try (MockedStatic<AuthUtils> mockedAuthUtils = mockStatic(AuthUtils.class)) {
            mockedAuthUtils.when(AuthUtils::getUsername).thenReturn("testUser");

            when(groupFeignClient.getGroupIdsByUsername("testUser")).thenReturn(groups);
            when(budgetRepository.findAll(any(Specification.class))).thenReturn(budgetEntities);
            when(budgetMapper.toModelList(budgetEntities)).thenReturn(budgets);

            List<Budget> result = retrieveBudgetProcessor.retrieveBudgets(params);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(groups, params.getGroupsIn());

            verify(groupFeignClient).getGroupIdsByUsername("testUser");
            verify(budgetRepository).findAll(any(Specification.class));
            verify(budgetMapper).toModelList(budgetEntities);
        }
    }


    @Test
    @DisplayName("retrieve budget by id test")
    void retrieveBudgetByIdTest() {
        String budgetId = UUID.randomUUID().toString();
        BudgetEntity budgetEntity = new BudgetEntity();
        Budget budget = new Budget();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budgetEntity));
        when(budgetMapper.toModel(budgetEntity)).thenReturn(budget);

        Budget result = retrieveBudgetProcessor.retrieveBudgetById(budgetId);

        assertNotNull(result);
        verify(budgetRepository).findById(budgetId);
        verify(budgetMapper).toModel(budgetEntity);
    }

    @Test
    @DisplayName("retrieve budget by id not found test")
    void retrieveBudgetByIdNotFoundTest() {
        String budgetId = UUID.randomUUID().toString();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> retrieveBudgetProcessor.retrieveBudgetById(budgetId));
        verify(budgetRepository).findById(budgetId);
    }

    @Test
    @DisplayName("retrieve budget name by group id test")
    void retrieveBudgetNameByGroupIdTest() {
        String groupId = UUID.randomUUID().toString();
        BudgetEntity budgetEntity = new BudgetEntity();
        budgetEntity.setName("Test Budget Name");

        when(budgetRepository.findAllByGroupIdIn(List.of(groupId))).thenReturn(List.of(budgetEntity));

        String result = retrieveBudgetProcessor.retrieveBudgetNameByGroupId(groupId);

        assertNotNull(result);
        assertEquals("Test Budget Name", result);
        verify(budgetRepository).findAllByGroupIdIn(List.of(groupId));
    }

    @Test
    @DisplayName("retrieve budget name by group id not found test")
    void retrieveBudgetNameByGroupIdNotFoundTest() {
        String groupId = UUID.randomUUID().toString();

        when(budgetRepository.findAllByGroupIdIn(List.of(groupId))).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> retrieveBudgetProcessor.retrieveBudgetNameByGroupId(groupId));
        verify(budgetRepository).findAllByGroupIdIn(List.of(groupId));
    }

}