package com.moneyminder.moneyminderexpenses.models;

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

    private String id;
    @NotBlank(message = "El detalle debe tener un nombre")
    private String name;
    @NotNull(message = "El detalle debe tener un precio por unidad")
    private Double pricePerUnit;
    @PositiveOrZero(message = "El número de unidades debe ser un número positivo")
    private Integer units;
    private Double totalPrice;
    @NotBlank(message = "El detalle debe estar asociado a un ingreso o gasto")
    private String record;

}
