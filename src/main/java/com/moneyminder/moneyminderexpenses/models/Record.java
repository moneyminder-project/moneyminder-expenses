package com.moneyminder.moneyminderexpenses.models;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
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

    private String id;
    @NotNull(message = "El registro debe tener un tipo (ingreso o gasto)")
    private RecordType type;
    @NotBlank(message = "El registro debe tener un nombre")
    private String name;
    @Positive(message = "El registro debe tener un importe positivo")
    private Double money;
    @NotNull(message = "El registro debe tener una fecha")
    private LocalDate date;
    private String comment;
    @NotBlank(message = "El registro debe estar asociado a un usuario")
    private String owner;
    private List<String> details;
    private List<String> budgets;

}
