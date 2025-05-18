package com.moneyminder.moneyminderexpenses.controllers;

import com.moneyminder.moneyminderexpenses.dto.RecordWithoutDetails;
import com.moneyminder.moneyminderexpenses.models.Record;
import com.moneyminder.moneyminderexpenses.processors.record.DeleteRecordProcessor;
import com.moneyminder.moneyminderexpenses.processors.record.RetrieveRecordProcessor;
import com.moneyminder.moneyminderexpenses.processors.record.SaveRecordProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.RecordRequestParams;
import com.moneyminder.moneyminderexpenses.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/record")
@RestController
@RequiredArgsConstructor
@Tag(name = "Controller para registros", description = "Controller para operaciones CRUD de registros")
public class RecordController {
    private final RetrieveRecordProcessor retrieveRecordProcessor;
    private final SaveRecordProcessor saveRecordProcessor;
    private final DeleteRecordProcessor deleteRecordProcessor;

    @GetMapping()
    @Operation(
            summary = "Obtiene todos los registros",
            description = "Devuelve todos los registros disponibles, filtrados por los parámetros pasados"
    )
    public ResponseEntity<List<Record>> getRecords(@ParameterObject final RecordRequestParams params) {
        return ResponseEntity.ok(this.retrieveRecordProcessor.retrieveRecords(params, AuthUtils.getUsername()));
    }

    @GetMapping( "/{id}")
    @Operation(
            summary = "Obtiene un registro por ID",
            description = "Devuelve un registro específico dado su ID"
    )
    public ResponseEntity<Record> getRecord(
            @Parameter(description = "ID del registro a obtener")
            @PathVariable final String id
    ) {
        return ResponseEntity.ok(this.retrieveRecordProcessor.retrieveRecordById(id));
    }

    @GetMapping("/by-budget/{id}")
    @Operation(
            summary = "Obtiene registros por ID de presupuesto",
            description = "Devuelve todos los registros asociados a un presupuesto específico"
    )
    public ResponseEntity<List<RecordWithoutDetails>> getRecordsByBudget(
            @Parameter(description = "ID del presupuesto del que se quieren obtener los registros")
            @PathVariable final String id) {
        return ResponseEntity.ok(this.retrieveRecordProcessor.retrieveRecordByBudgetId(id));
    }

    @PostMapping()
    @Operation(
            summary = "Crea un nuevo registro",
            description = "Crea un nuevo registro a partir de los datos proporcionados"
    )
    public ResponseEntity<Record> createRecord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto registro a crear",
                    required = true
            )
            @Valid @RequestBody final Record record
    ) {
        return ResponseEntity.ok(this.saveRecordProcessor.saveRecord(record));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un registro existente",
            description = "Actualiza un registro específico dado su ID"
    )
    public ResponseEntity<Record> updateRecord(
            @Parameter(description = "ID del registro a modificar")
            @PathVariable final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto registro a modificar",
                    required = true
            )
            @Valid @RequestBody final Record record) {
        return ResponseEntity.ok(this.saveRecordProcessor.updateRecord(id, record));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un registro",
            description = "Elimina un registro específico dado su ID"
    )
    public ResponseEntity<Void> deleteRecord(
            @Parameter(description = "ID del registro a eliminar")
            @PathVariable final String id
    ) {
        this.deleteRecordProcessor.deleteRecord(id);
        return ResponseEntity.ok().build();
    }

}
