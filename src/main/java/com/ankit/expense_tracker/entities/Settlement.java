package com.ankit.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;
    @ManyToOne
    @JoinColumn(name = "paid_to")
    private User paidTo;
    @Column(nullable = false)
    private BigDecimal amount;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
