package com.moneyminder.moneyminderexpenses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    @Schema(description = "ID único del presupuesto en formato UUID", example = "3470bb88-6738-4e8e-8825-187c40dc4015")
    private String id;

    @NotBlank(message = "Se debe especificar el nombre del presupuesto")
    @Schema(description = "Nombre del presupuesto", example = "Presupuesto Mensual")
    private String name;

    @Schema(description = "Comentario adicional sobre el presupuesto", example = "Presupuesto para marzo")
    private String comment;

    @Schema(description = "Fecha de inicio del presupuesto", type = "string", format = "date", example = "2025-03-01")
    private LocalDate startDate;

    @Schema(description = "Fecha de fin del presupuesto", type = "string", format = "date", example = "2025-03-31")
    private LocalDate endDate;

    @Schema(description = "Límite máximo de gastos permitidos", example = "1500.00")
    private Double expensesLimit;

    @Schema(description = "Total de gastos realizados", example = "1250.00")
    private Double totalExpenses;

    @Schema(description = "Total de ingresos recibidos", example = "2000.00")
    private Double totalIncomes;

    @Schema(description = "ID del grupo al que pertenece el presupuesto", example = "6928c017-6459-4603-9932-38c864232248")
    private String groupId;

    @Schema(description = "Indica si el presupuesto está marcado como favorito", example = "true")
    private Boolean favorite;

    @Schema(description = "Lista de IDs de registros asociados al presupuesto")
    private List<String> records;

}
