package com.moneyminder.moneyminderexpenses.requestParams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetRequestParams {
    @Schema(description = "Gastos mayores o iguales a este valor", example = "500.0")
    private Double expensesLimitGreaterOrEqualThan;

    @Schema(description = "Gastos menores o iguales a este valor", example = "2000.0")
    private Double expensesLimitLowerOrEqualThan;

    @Schema(description = "Fecha de inicio antes o igual a", type = "string", format = "date", example = "2025-03-01")
    private LocalDate startDateBeforeOrEqualThan;

    @Schema(description = "Fecha de inicio después o igual a", type = "string", format = "date", example = "2025-02-01")
    private LocalDate startDateAfterOrEqualThan;

    @Schema(description = "Fecha de fin antes o igual a", type = "string", format = "date", example = "2025-04-01")
    private LocalDate endDateBeforeOrEqualThan;

    @Schema(description = "Fecha de fin después o igual a", type = "string", format = "date", example = "2025-03-01")
    private LocalDate endDateAfterOrEqualThan;

    @Schema(description = "IDs de grupos a los que pertenece el presupuesto")
    private List<String> groupsIn;

    @Schema(description = "Nombre del presupuesto", example = "Presupuesto Vacaciones")
    private String name;

    @Schema(description = "Comentario del presupuesto", example = "Viaje a la playa")
    private String comment;

    @Schema(description = "Filtra por presupuestos favoritos", example = "true")
    private Boolean favorite;
}

