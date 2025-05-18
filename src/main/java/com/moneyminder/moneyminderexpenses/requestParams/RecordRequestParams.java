package com.moneyminder.moneyminderexpenses.requestParams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordRequestParams {
    @Schema(description = "Importe mayor o igual a este valor", example = "100.0")
    private Double moneyGreaterOrEqualThan;

    @Schema(description = "Importe menor o igual a este valor", example = "1000.0")
    private Double moneyLowerOrEqualThan;

    @Schema(description = "Fecha del registro antes o igual a", type = "string", format = "date", example = "2025-04-01")
    private LocalDate dateBeforeOrEqualThan;

    @Schema(description = "Fecha del registro después o igual a", type = "string", format = "date", example = "2025-03-01")
    private LocalDate dateAfterOrEqualThan;

    @Schema(description = "IDs de presupuestos en los que se incluyen los registros", example = "")
    private List<String> budgetsIn;

    @Schema(description = "Comentario del registro para filtrar", example = "Pago mensual")
    private String comment;

    @Schema(description = "Nombre del registro para filtrar", example = "Sueldo")
    private String name;

    @Schema(description = "ID del propietario del registro", example = "user1")
    private String owner;

    @Schema(description = "Tipo de registro: INCOME o EXPENSE", example = "EXPENSE")
    private RecordType recordType;
}
