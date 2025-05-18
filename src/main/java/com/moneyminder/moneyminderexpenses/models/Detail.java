package com.moneyminder.moneyminderexpenses.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Detail {

    @Schema(description = "ID único del detalle", example = "d23acb76-9f3e-4d6e-a0c4-5a3f8c0e5e4b")
    private String id;

    @NotBlank(message = "El detalle debe tener un nombre")
    @Schema(description = "Nombre del detalle", example = "Pan integral")
    private String name;

    @NotNull(message = "El detalle debe tener un precio por unidad")
    @Schema(description = "Precio por unidad del detalle", example = "2.50")
    private Double pricePerUnit;

    @PositiveOrZero(message = "El número de unidades debe ser un número positivo")
    @Schema(description = "Cantidad de unidades del detalle", example = "4")
    private Integer units;

    @Schema(description = "Precio total del detalle (precio por unidad * unidades)", example = "10.00")
    private Double totalPrice;

    @NotBlank(message = "El detalle debe estar asociado a un ingreso o gasto")
    @Schema(description = "ID del registro (ingreso o gasto) al que pertenece el detalle", example = "d23acb76-9f3e-4d6e-a0c4-5a3f8c0e5e4b")
    private String record;

}
