package com.moneyminder.moneyminderexpenses.models;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Record {

    @Schema(description = "ID único del registro", example = "96a035da-41fb-4fa1-ab8e-1a605409a576")
    private String id;

    @NotNull(message = "El registro debe tener un tipo (ingreso o gasto)")
    @Schema(description = "Tipo de registro: INCOME o EXPENSE", example = "INCOME")
    private RecordType type;

    @NotBlank(message = "El registro debe tener un nombre")
    @Schema(description = "Nombre del registro", example = "Sueldo Marzo")
    private String name;

    @Positive(message = "El registro debe tener un importe positivo")
    @Schema(description = "Importe del registro", example = "2500.00")
    private Double money;

    @NotNull(message = "El registro debe tener una fecha")
    @Schema(description = "Fecha del registro", type = "string", format = "date", example = "2025-03-15")
    private LocalDate date;

    @Schema(description = "Comentario adicional sobre el registro", example = "Pago mensual")
    private String comment;

    @NotBlank(message = "El registro debe estar asociado a un usuario")
    @Schema(description = "ID del usuario propietario del registro", example = "user1")
    private String owner;

    @Schema(description = "Lista de IDs de detalles asociados al registro")
    private List<String> details;

    @Schema(description = "Lista de IDs de presupuestos asociados al registro")
    private List<String> budgets;

}
