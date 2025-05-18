package com.moneyminder.moneyminderexpenses.controllers;

import com.moneyminder.moneyminderexpenses.models.Budget;
import com.moneyminder.moneyminderexpenses.processors.budget.DeleteBudgetProcessor;
import com.moneyminder.moneyminderexpenses.processors.budget.RetrieveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.processors.budget.SaveBudgetProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.BudgetRequestParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
            description = "Obtiene todos los presupuestos, filtrados por  los params pasados"
    )
    public ResponseEntity<List<Budget>> getBudgets(@ParameterObject final BudgetRequestParams params) {
        return ResponseEntity.ok(this.retrieveBudgetProcessor.retrieveBudgets(params));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un presupuesto por ID",
            description = "Devuelve un presupuesto específico dado su ID"
    )
    public ResponseEntity<Budget> getBudget(
            @Parameter(description = "ID del presupuesto a obtener")
            @PathVariable final String id
    ) {
        return ResponseEntity.ok(this.retrieveBudgetProcessor.retrieveBudgetById(id));
    }

    @GetMapping("/budget-name/{id}")
    @Operation(
            summary = "Obtiene el nombre del presupuesto por ID de grupo",
            description = "Devuelve el nombre del presupuesto asociado a un grupo específico"
    )
    public ResponseEntity<String> getBudgetNameByGroupId(
            @Parameter(description = "ID del presupuesto del que se quiere el nombre")
            @PathVariable String id) {
        return ResponseEntity.ok(this.retrieveBudgetProcessor.retrieveBudgetNameByGroupId(id));
    }

    @PostMapping()
    @Operation(
            summary = "Crea un nuevo presupuesto",
            description = "Crea un presupuesto a partir de los datos proporcionados"
    )
    public ResponseEntity<Budget> createBudget(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto presupuesto a crear",
                    required = true
            )
            @Valid @RequestBody final Budget budget
    ) {
        return ResponseEntity.ok(this.saveBudgetProcessor.saveBudget(budget));
    }

    @PutMapping( "/{id}")
    @Operation(
            summary = "Actualiza un presupuesto existente",
            description = "Actualiza un presupuesto existente con el ID proporcionado"
    )
    public ResponseEntity<Budget> updateBudget(
            @Parameter(description = "ID del presupuesto a modificar")
            @PathVariable final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto presupuesto a modificar",
                    required = true
            )
            @Valid @RequestBody final Budget budget
    ) {
        return ResponseEntity.ok(this.saveBudgetProcessor.updateBudget(id, budget));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un presupuesto",
            description = "Elimina un presupuesto a partir del ID proporcionado"
    )
    public ResponseEntity<Void> deleteBudget(
            @Parameter(description = "ID del presupuesto a eliminar")
            @PathVariable final String id
    ) {
        this.deleteBudgetProcessor.deleteBudget(id);
        return ResponseEntity.ok().build();
    }
}
