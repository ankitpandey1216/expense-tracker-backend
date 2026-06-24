package com.ankit.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    @Column(nullable = false)
    private BigDecimal expenseAmount;
    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;
    @Enumerated(EnumType.STRING)
    private SplitType splitType;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
