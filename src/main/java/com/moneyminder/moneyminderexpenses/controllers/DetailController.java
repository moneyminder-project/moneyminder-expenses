package com.moneyminder.moneyminderexpenses.controllers;

import com.moneyminder.moneyminderexpenses.models.Detail;
import com.moneyminder.moneyminderexpenses.processors.detail.DeleteDetailProcessor;
import com.moneyminder.moneyminderexpenses.processors.detail.RetrieveDetailProcessor;
import com.moneyminder.moneyminderexpenses.processors.detail.SaveDetailProcessor;
import com.moneyminder.moneyminderexpenses.requestParams.DetailRequestParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/detail")
@RestController
@RequiredArgsConstructor
@Tag(name = "Controller para detalles", description = "Controller para operaciones CRUD de detalles")
public class DetailController {
    private final RetrieveDetailProcessor retrieveDetailProcessor;
    private final SaveDetailProcessor saveDetailProcessor;
    private final DeleteDetailProcessor deleteDetailProcessor;

    @GetMapping()
    @Operation(
            summary = "Obtiene todos los detalles de registros",
            description = "Obtiene todos los detalles disponibles, filtrados por los parámetros pasados"
    )
    public ResponseEntity<List<Detail>> getDetails(@ParameterObject final DetailRequestParams params) {
        return ResponseEntity.ok(this.retrieveDetailProcessor.retrieveDetails(params));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un detalle de registro por ID",
            description = "Devuelve un detalle específico dado su ID"
    )
    public ResponseEntity<Detail> getDetail(
            @Parameter(description = "ID del detalle a obtener")
            @PathVariable final String id
    ) {
        return ResponseEntity.ok(this.retrieveDetailProcessor.retrieveDetailById(id));
    }

    @PostMapping()
    @Operation(
            summary = "Crea un nuevo detalle de registro",
            description = "Crea un nuevo detalle a partir de los datos enviados"
    )
    public ResponseEntity<Detail> createDetail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto detalle a crear",
                    required = true
            )
            @Valid @RequestBody final Detail detail
    ) {
        return ResponseEntity.ok(this.saveDetailProcessor.saveDetail(detail));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un detalle de registro existente",
            description = "Actualiza un detalle específico dado su ID"
    )
    public ResponseEntity<Detail> updateDetail(
            @Parameter(description = "ID del detalle a modificar")
            @PathVariable final String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto detalle a modificar",
                    required = true
            )
            @Valid @RequestBody final Detail detail
    ) {
        return ResponseEntity.ok(this.saveDetailProcessor.updateDetail(id, detail));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un detalle",
            description = "Elimina un detalle específico dado su ID"
    )
    public ResponseEntity<Void> deleteDetail(
            @Parameter(description = "ID del detalle a eliminar")
            @PathVariable final String id
    ) {
        this.deleteDetailProcessor.deleteDetail(id);
        return ResponseEntity.ok().build();
    }
}
