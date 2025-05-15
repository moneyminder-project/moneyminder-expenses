package com.moneyminder.moneyminderexpenses.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.processors.budget.DeleteBudgetProcessor;
import com.moneyminder.moneyminderexpenses.processors.budget.RetrieveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.processors.budget.SaveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.BudgetRequestParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(BudgetControllerTest.class)
@AutoConfigureMockMvc(addFilters = false)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveBudgetProcessor retrieveBudgetProcessor;

    @MockBean
    private SaveBudgetProcessor saveBudgetProcessor;

    @MockBean
    private DeleteBudgetProcessor deleteBudgetProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all budgets test")
    void getBudgets() throws Exception {
        Budget budget = new Budget();
        budget.setId("budget1");
        budget.setName("Test Budget");

        Mockito.when(retrieveBudgetProcessor.retrieveBudgets(any(BudgetRequestParams.class)))
                .thenReturn(List.of(budget));

        mockMvc.perform(get("/budget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("budget1"))
                .andExpect(jsonPath("$[0].name").value("Test Budget"));
    }

    @Test
    @DisplayName("get budget by id test")
    void getBudgetById() throws Exception {
        Budget budget = new Budget();
        budget.setId("budget1");
        budget.setName("Test Budget");

        Mockito.when(retrieveBudgetProcessor.retrieveBudgetById("budget1"))
                .thenReturn(budget);

        mockMvc.perform(get("/budget/budget1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("budget1"))
                .andExpect(jsonPath("$.name").value("Test Budget"));
    }

    @Test
    @DisplayName("get budget name by group id test")
    void getBudgetNameByGroupId() throws Exception {
        Mockito.when(retrieveBudgetProcessor.retrieveBudgetNameByGroupId("group1"))
                .thenReturn("Group Budget Name");

        mockMvc.perform(get("/budget/budget-name/group1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Group Budget Name"));
    }

    @Test
    @DisplayName("create budget test")
    void createBudget() throws Exception {
        Budget budget = new Budget();
        budget.setName("New Budget");
        budget.setStartDate(LocalDate.now());
        budget.setEndDate(LocalDate.now().plusDays(30));

        Budget savedBudget = new Budget();
        savedBudget.setId("budget1");
        savedBudget.setName("New Budget");

        Mockito.when(saveBudgetProcessor.saveBudget(any(Budget.class))).thenReturn(savedBudget);

        mockMvc.perform(post("/budget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("budget1"))
                .andExpect(jsonPath("$.name").value("New Budget"));
    }

    @Test
    @DisplayName("update budget test")
    void updateBudget() throws Exception {
        Budget budget = new Budget();
        budget.setId("budget1");
        budget.setName("Updated Budget");

        Mockito.when(saveBudgetProcessor.updateBudget(eq("budget1"), any(Budget.class)))
                .thenReturn(budget);

        mockMvc.perform(put("/budget/budget1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("budget1"))
                .andExpect(jsonPath("$.name").value("Updated Budget"));
    }

    @Test
    @DisplayName("delete budget test")
    void deleteBudget() throws Exception {
        mockMvc.perform(delete("/budget/budget1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteBudgetProcessor).deleteBudget("budget1");
    }
}