package com.moneyminder.moneyminderexpenses.persistence.entities;

import com.moneyminder.moneyminderexpenses.utils.RecordType;
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
@Table(name = "RECORD")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", columnDefinition = "CHAR(36)")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private RecordType type;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name ="MONEY", nullable = false)
    private Double money;

    @Column(name = "DATE", nullable = false, columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "OWNER", nullable = false)
    private String owner;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<DetailEntity> details;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "RECORDS_BUDGETS",
            joinColumns = @JoinColumn(name = "RECORD_ID"),
            inverseJoinColumns = @JoinColumn(name = "BUDGET_ID")
    )
    private List<BudgetEntity> budgets;

}
