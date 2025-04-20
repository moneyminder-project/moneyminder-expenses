package com.moneyminder.moneyminderexpenses.requestParams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetRequestParams {
    private Double expensesLimitGreaterOrEqualThan;
    private Double expensesLimitLowerOrEqualThan;
    private LocalDate startDateBeforeOrEqualThan;
    private LocalDate startDateAfterOrEqualThan;
    private LocalDate endDateBeforeOrEqualThan;
    private LocalDate endDateAfterOrEqualThan;
    private List<String> groupsIn;
    private String name;
    private String comment;
    private Boolean favorite;
}

