package com.ankit.expense_tracker.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SplitExpenseDTO {
    private String borrowerName;
    private BigDecimal amountOwed;
    private Long splitExpenseId;
}
