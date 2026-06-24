package com.ankit.expense_tracker.DTO;

import com.ankit.expense_tracker.entities.SplitType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseDTO {
    private Long expenseId;

    private BigDecimal amount;

    private String description;

    private String paidBy;
    private Long paidById;

    private SplitType splitType;

    private LocalDateTime createdAt;
}
