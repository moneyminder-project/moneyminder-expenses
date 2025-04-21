package com.moneyminder.moneyminderexpenses.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BUDGET")
public class BudgetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "COMMENT")
    private String comment;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "EXPENSES_LIMIT")
    private Double expensesLimit;

    @Column(name = "TOTAL_EXPENSES")
    private Double totalExpenses = 0.0;

    @Column(name = "TOTAL_INCOMES")
    private Double totalIncomes = 0.0;

    @Column(name = "GROUP_ID", nullable = false)
    private String groupId;

    @ElementCollection
    @CollectionTable(name = "BUDGET_FAVOURITES", joinColumns = @JoinColumn(name = "BUDGET_ID"))
    @Column(name = "USERNAME")
    private List<String> usernames;

    @ManyToMany(mappedBy = "budgets", fetch = FetchType.LAZY)
    private List<RecordEntity> records;
}
