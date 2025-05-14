package com.moneyminder.moneyminderexpenses.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DETAIL")
public class DetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PRICE_PER_UNIT", nullable = false)
    private Double pricePerUnit;

    @Column(name = "UNITS", nullable = false)
    private Integer units;

    @Column(name = "TOTAL_PRICE")
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECORD_ID", nullable = false)
    private RecordEntity record;
}
