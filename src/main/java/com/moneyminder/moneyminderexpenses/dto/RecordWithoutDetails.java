package com.moneyminder.moneyminderexpenses.dto;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
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
    private String id;
    private RecordType type;
    private String name;
    private Double money;
    private LocalDate date;
    private String comment;
    private String owner;
}
