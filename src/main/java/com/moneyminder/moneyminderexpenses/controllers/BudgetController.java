package com.moneyminder.moneyminderexpenses.controllers;

import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.processors.budget.DeleteBudgetProcessor;
import com.moneyminder.moneyminderexpenses.processors.budget.RetrieveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.processors.budget.SaveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.BudgetRequestParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/budget")
@RestController
@RequiredArgsConstructor
@Tag(name = "Controller para presupuestos", description = "Controller para operaciones CRUD de presupuestos")
public class BudgetController {
    private final RetrieveBudgetProcessor retrieveBudgetProcessor;
    private final SaveBudgetProcessor saveBudgetProcessor;
    private final DeleteBudgetProcessor deleteBudgetProcessor;

    @GetMapping
    @Operation(
            summary = "Obtiene todos los presupuestos",
            description = "Método para obtener todos los presupuestos, filtrados por  los params pasados",
            tags = {"budgets"}
    )
    public ResponseEntity<List<Budget>> getBudgets(final BudgetRequestParams params) {
        return ResponseEntity.ok(this.retrieveBudgetProcessor.retrieveBudgets(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudget(@PathVariable final String id) {
        return ResponseEntity.ok(this.retrieveBudgetProcessor.retrieveBudgetById(id));
    }

    @GetMapping("/budget-name/{id}")
    public ResponseEntity<String> getBudgetNameByGroupId(@PathVariable String id) {
        return ResponseEntity.ok(this.retrieveBudgetProcessor.retrieveBudgetNameByGroupId(id));
    }

    @PostMapping()
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody final Budget budget) {
        return ResponseEntity.ok(this.saveBudgetProcessor.saveBudget(budget));
    }

    @PutMapping( "/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable final String id, @Valid @RequestBody final Budget budget) {
        return ResponseEntity.ok(this.saveBudgetProcessor.updateBudget(id, budget));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable final String id) {
        this.deleteBudgetProcessor.deleteBudget(id);
        return ResponseEntity.ok().build();
    }


}
