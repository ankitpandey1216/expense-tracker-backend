package com.ankit.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class SplitExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long splitExpenseId;
    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expenseId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    private BigDecimal amount;
    private boolean isSettled;

}
