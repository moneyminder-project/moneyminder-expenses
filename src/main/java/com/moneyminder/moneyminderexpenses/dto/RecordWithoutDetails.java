package com.moneyminder.moneyminderexpenses.dto;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordWithoutDetails {
    @Schema(description = "ID único del registro", example = "178f3c7a-4ad6-4dc7-b338-ca507dfb8f5e")
    private String id;

    @Schema(description = "Tipo de registro: INCOME o EXPENSE", example = "INCOME")
    private RecordType type;

    @Schema(description = "Nombre del registro", example = "Pago supermercado")
    private String name;

    @Schema(description = "Importe del registro", example = "300.00")
    private Double money;

    @Schema(description = "Fecha del registro", type = "string", format = "date", example = "2025-03-20")
    private LocalDate date;

    @Schema(description = "Comentario adicional sobre el registro", example = "Alimentaria")
    private String comment;

    @Schema(description = "ID del usuario propietario del registro", example = "user7")
    private String owner;
}
