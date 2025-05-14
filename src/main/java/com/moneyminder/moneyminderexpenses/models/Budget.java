package com.moneyminder.moneyminderexpenses.models;

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

    private String id;
    @NotBlank(message = "Se debe especificar el nombre del presupuesto")
    private String name;
    private String comment;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double expensesLimit;
    private Double totalExpenses;
    private Double totalIncomes;
    private String groupId;
    private Boolean favorite;
    private List<String> records;

}
