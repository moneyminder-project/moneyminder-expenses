package com.moneyminder.moneyminderexpenses.requestParams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moneyminder.moneyminderexpenses.utils.RecordType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordRequestParams {
    private Double moneyGreaterOrEqualThan;
    private Double moneyLowerOrEqualThan;
    private LocalDate dateBeforeOrEqualThan;
    private LocalDate dateAfterOrEqualThan;
    private List<String> budgetsIn;
    private String comment;
    private String name;
    private String owner;
    private RecordType recordType;
}
